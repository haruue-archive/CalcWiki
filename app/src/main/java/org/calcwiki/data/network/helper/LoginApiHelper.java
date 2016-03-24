package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.LoginModel;
import org.calcwiki.data.network.api.RestApi;
import org.calcwiki.data.storage.CurrentLogin;
import org.calcwiki.data.storage.CurrentUser;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 协助操纵登录 API 的工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class LoginApiHelper {

    interface LoginApiHelperListener {
        void onSuccess();
        void onFailure(int reason);
    }

    class LoginFailureReason {
        final static int EMPTY_USERNAME = 1;
        final static int EMPTY_PASSWORD = 2;
        final static int USERNAME_NOT_EXIST = 4;
        final static int PASSWORD_ERROR = 8;
        final static int NETWORK_ERROR = 16;
        final static int SERVER_ERROR = 32;
        final static int TOKEN_WRONG = 64;

    }

    public static void login(final LoginApiHelperListener listener) {
        if (CurrentLogin.getInstance().username == null || CurrentLogin.getInstance().username.isEmpty()) {
            listener.onFailure(LoginFailureReason.EMPTY_USERNAME);
            return;
        }
        if (CurrentLogin.getInstance().password == null || CurrentLogin.getInstance().password.isEmpty()) {
            listener.onFailure(LoginFailureReason.EMPTY_PASSWORD);
            return;
        }
        Observable<String> loginObservable = RestApi.getCalcWikiApiService().login(CurrentLogin.getInstance().username, CurrentLogin.getInstance().password, "");
        loginObservable.subscribeOn(Schedulers.io())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            CurrentLogin.getInstance().lgtoken = JSON.parseObject(s, LoginModel.NeedToken.class).login.token;
                        } catch (Exception e) {
                            try {
                                CurrentUser.getInstance().onLoginSuccess(JSON.parseObject(s, LoginModel.Success.class));
                            } catch (Exception e) {
                                LoginModel.Result result = JSON.parseObject(s, LoginModel.Result.class);
                                if (result.login.result.equals("WrongPass")) {
                                    listener.onFailure(LoginFailureReason.PASSWORD_ERROR);
                                } else if (result.login.result.equals("NotExists")) {
                                    listener.onFailure(LoginFailureReason.USERNAME_NOT_EXIST);
                                } else if (result.login.result.equals("WrongToken")) {
                                    listener.onFailure(LoginFailureReason.TOKEN_WRONG);
                                }
                            }
                        }
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                })
                // TODO: Complete it

    }
}
