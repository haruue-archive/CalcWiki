package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.data.model.RegisterModel;
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
        void onSuccess(MobileViewModel.Page pageDate);

        void onFailure(int reason);
    }

    public class GetPageFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
        public final static int PAGE_NOT_EXIST = 4;
    }

    public static void getPage(String pageName, boolean isRedirect, String prop, final GetPageApiHelperListener listener) {

        RestApi.getCalcWikiApiService().getPage(pageName, isRedirect ? "yes" : "no", prop)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onFailure(GetPageFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        MobileViewModel.Page pageData = JSON.parseObject(s, MobileViewModel.Page.class);
                        if (pageData != null && pageData.mobileview != null) {
                            listener.onSuccess(pageData);
                            return;
                        }
                        MobileViewModel.Error error = JSON.parseObject(s, MobileViewModel.Error.class);
                        if (error != null && error.error != null && error.error.code.equals("missingtitle")) {
                            listener.onFailure(GetPageFailureReason.PAGE_NOT_EXIST);
                            return;
                        }
                        listener.onFailure(GetPageFailureReason.SERVER_ERROR);
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
}
