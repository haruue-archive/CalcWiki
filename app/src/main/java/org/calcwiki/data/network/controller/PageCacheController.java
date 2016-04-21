package org.calcwiki.data.network.controller;

import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import org.calcwiki.App;
import org.calcwiki.data.model.ParseModel;
import org.calcwiki.data.model.QueryModel;
import org.calcwiki.data.network.helper.PageApiHelper;
import org.calcwiki.data.network.helper.QueryApiHelper;
import org.calcwiki.data.storage.CurrentPage;
import org.calcwiki.util.Sha1;
import org.calcwiki.util.Utils;

import java.security.NoSuchAlgorithmException;

/**
 * 提供本地页面缓存支持，如果本地找不到或者已过期再联网加载
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageCacheController {

    public static PageCacheController controller;

    JFileManager.Folder folder;

    public interface PageCacheControllerListener {
        void onLoadSuccess();
        void onLoadFailure(int reason);
    }

    public class PageCacheControllerFailedReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
        public final static int PAGE_NOT_EXIST = 4;
        public final static int IO_EXCEPTION = 8;
        public final static int UNKNOWN_EXCEPTION = 16;
        public final static int PERMISSION_DENIED = 32;
    }

    private PageCacheController() {
        folder = JFileManager.getInstance().getFolder(App.ObjectStorageDirs.Page);
    }

    public static void initialize() {
        controller = new PageCacheController();
    }

    public static PageCacheController getInstance() {
        return controller;
    }

    public void loadPage(final String pageName, final boolean isRedirect, final PageCacheControllerListener controllerListener) {
        Utils.runOnNewThread(new Runnable() {
            @Override
            public void run() {
                if (!isRedirect) {
                    CurrentPage data = doLoadFromCache(pageName);
                    if (data != null && Utils.getCurrentTimeStamp() - data.refreshTime <= 43200) {
                        CurrentPage.setCurrentPageFromCache(data);
                        controllerListener.onLoadSuccess();
                        JUtils.Log("PageCache", "Load from cache");
                        return;
                    }
                }
                loadPageFromNetwork(pageName, isRedirect, controllerListener);
            }
        });
    }

    public void loadPageFromNetwork(String pageName, boolean isRedirect, PageCacheControllerListener controllerListener) {
        Listener listener = new Listener(controllerListener);
        PageApiHelper.getPage(pageName, isRedirect, listener);
        QueryApiHelper.getPageInfo(pageName, isRedirect, listener);
    }

    public void refreshCacheFromCurrentPage() {
        Utils.runOnNewThread(new Runnable() {
            @Override
            public void run() {
                doSavePageToCache(CurrentPage.getInstance());
            }
        });
    }

    private CurrentPage doLoadFromCache(String pageName) {
        try {
            return (CurrentPage) folder.readObjectFromFile(hashPageName(pageName));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private void doSavePageToCache(CurrentPage currentPage) {
        try {
            folder.writeObjectToFile(currentPage, hashPageName(currentPage.pageData.parse.title));
        } catch (NoSuchAlgorithmException ignored) {

        }
    }

    private class Listener implements PageApiHelper.GetPageApiHelperListener, QueryApiHelper.GetPageInfoApiHelperListener {

        private PageCacheControllerListener controllerListener;
        private boolean flagGetPageSuccess = false;
        private boolean flagGetInfoSuccess = false;

        public Listener(PageCacheControllerListener controllerListener) {
            this.controllerListener = controllerListener;
        }

        private void checkSuccess() {
            if (flagGetPageSuccess && flagGetInfoSuccess) {
                controllerListener.onLoadSuccess();
                refreshCacheFromCurrentPage();
            }
        }

        @Override
        public void onGetPageSuccess(ParseModel.Page pageDate) {
            CurrentPage.getInstance().storagePage(pageDate);
            flagGetPageSuccess = true;
            checkSuccess();
        }

        @Override
        public void onGetPageFailure(int reason) {
            switch (reason) {
                case PageApiHelper.GetPageFailureReason.NETWORK_ERROR:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.NETWORK_ERROR);
                    break;
                case PageApiHelper.GetPageFailureReason.SERVER_ERROR:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.SERVER_ERROR);
                    break;
                case PageApiHelper.GetPageFailureReason.PAGE_NOT_EXIST:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.PAGE_NOT_EXIST);
                    break;
                default:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.UNKNOWN_EXCEPTION);
                    break;
            }
        }

        @Override
        public void onGetPageInfoSuccess(QueryModel.PageInfo pageInfo) {
            CurrentPage.getInstance().storagePageInfo(pageInfo);
            if (pageInfo.query.pages.content.readable == null) {
                controllerListener.onLoadFailure(PageCacheControllerFailedReason.PERMISSION_DENIED);
                return;
            }
            flagGetInfoSuccess = true;
            checkSuccess();
        }

        @Override
        public void onGetPageInfoFailure(int reason) {
            switch (reason) {
                case QueryApiHelper.GetPageInfoFailureReason.NETWORK_ERROR:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.NETWORK_ERROR);
                    break;
                case QueryApiHelper.GetPageInfoFailureReason.SERVER_ERROR:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.SERVER_ERROR);
                    break;
                default:
                    controllerListener.onLoadFailure(PageCacheControllerFailedReason.UNKNOWN_EXCEPTION);
                    break;
            }
        }
    }

    public String hashPageName(String pageName) throws NoSuchAlgorithmException {
        return Sha1.sha1(pageName);
    }

}
