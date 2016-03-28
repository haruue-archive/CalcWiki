package org.calcwiki.data.network.helper;

import org.calcwiki.data.network.api.RestApi;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 协助操纵登出 API 的工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class LogoutApiHelper {

    public interface OnLogoutListener {
        void onLogoutSuccess();
        void onLogoutFailure();
    }

    public static void logout(final OnLogoutListener listener) {
        RestApi.getCalcWikiApiService().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onLogoutFailure();
                    }

                    @Override
                    public void onNext(String s) {
                        listener.onLogoutSuccess();
                    }
                });
    }

}
