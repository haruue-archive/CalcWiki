package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.data.model.ParseModel;
import org.calcwiki.data.network.api.MediaWikiInterceptor;
import org.calcwiki.data.network.api.RestApi;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 协助获取各种页面内容细节的工具
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageApiHelper {

    public interface GetPageApiHelperListener {
        void onGetPageSuccess(ParseModel.Page pageDate);

        void onGetPageFailure(int reason);
    }

    public class GetPageFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
        public final static int PAGE_NOT_EXIST = 4;
    }

    public static void getPage(String pageName, boolean isRedirect, final GetPageApiHelperListener listener) {
        MediaWikiInterceptor.getInstance().setIsRedirect(isRedirect);
        RestApi.getCalcWikiApiService().getPage(pageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onGetPageFailure(GetPageFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        ParseModel.Page page = JSON.parseObject(s, ParseModel.Page.class);
                        if (page != null && page.parse != null && page.parse.text != null) {
                            listener.onGetPageSuccess(page);
                            return;
                        }
                        ParseModel.Error error = JSON.parseObject(s, ParseModel.Error.class);
                        if (error != null && error.error != null && error.error.code.equals("missingtitle")) {
                            listener.onGetPageFailure(GetPageFailureReason.PAGE_NOT_EXIST);
                            return;
                        }
                        listener.onGetPageFailure(GetPageFailureReason.SERVER_ERROR);
                    }
                });
    }

    public interface CheckPageExistApiHelperListener {
        void onGetPageExistState(boolean isPageExist);
        void onGetPageExistStateFailure(int reason);
    }

    public class CheckPageExistFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
    }

    public static void checkPageExistState(String pageName, final CheckPageExistApiHelperListener listener) {
        RestApi.getCalcWikiApiService().checkPageExist(pageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onGetPageExistStateFailure(CheckPageExistFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        MobileViewModel.CheckPageExist page = JSON.parseObject(s, MobileViewModel.CheckPageExist.class);
                        if (page != null && page.mobileview != null) {
                            listener.onGetPageExistState(true);
                            return;
                        }
                        MobileViewModel.Error error = JSON.parseObject(s, MobileViewModel.Error.class);
                        if (error != null && error.error.code.equals("missingtitle")) {
                            listener.onGetPageExistState(false);
                        }
                        listener.onGetPageExistStateFailure(CheckPageExistFailureReason.SERVER_ERROR);
                    }
                });
    }

    public interface GetPageSourceApiHelperListener {
        void onGetPageSourceSuccess(String source);
        void onGetPageSourceFailure(int reason);
    }

    public class GetPageSourceFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
        public final static int PAGE_EMPTY = 4;
    }

    public static void getPageSource(String title, final GetPageSourceApiHelperListener listener) {
        RestApi.getCalcWikiApiService().getPageSource(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onGetPageSourceFailure(GetPageSourceFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.isEmpty()) {
                            listener.onGetPageSourceFailure(GetPageSourceFailureReason.PAGE_EMPTY);
                        } else {
                            listener.onGetPageSourceSuccess(s);
                        }
                    }
                });
    }

}
