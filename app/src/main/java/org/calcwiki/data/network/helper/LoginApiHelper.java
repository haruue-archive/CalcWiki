package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;
import com.jude.utils.JUtils;

import org.calcwiki.data.model.LoginModel;
import org.calcwiki.data.network.api.RestApi;
import org.calcwiki.data.storage.CurrentLogin;
import org.calcwiki.data.storage.CurrentUser;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 协助操纵登录 API 的工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class LoginApiHelper {

    public interface LoginApiHelperListener {
        void onLoginSuccess();
        void onLoginFailure(int reason);
    }

    public class LoginFailureReason {
        public final static int EMPTY_USERNAME = 1;
        public final static int EMPTY_PASSWORD = 2;
        public final static int USERNAME_NOT_EXIST = 4;
        public final static int PASSWORD_ERROR = 8;
        public final static int NETWORK_ERROR = 16;
        public final static int SERVER_ERROR = 32;
        public final static int TOKEN_WRONG = 64;

    }

    public static void login(final LoginApiHelperListener listener) {
        if (CurrentLogin.getInstance().username == null || CurrentLogin.getInstance().username.isEmpty()) {
            listener.onLoginFailure(LoginFailureReason.EMPTY_USERNAME);
            return;
        }
        if (CurrentLogin.getInstance().password == null || CurrentLogin.getInstance().password.isEmpty()) {
            listener.onLoginFailure(LoginFailureReason.EMPTY_PASSWORD);
            return;
        }
        final Observable<String> loginObservable = RestApi.getCalcWikiApiService().login(CurrentLogin.getInstance().username, CurrentLogin.getInstance().password, "");
        loginObservable.subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(String s) {
                        // Need Token
                        CurrentLogin.getInstance().lgtoken = JSON.parseObject(s, LoginModel.NeedToken.class).login.token;
                        if (CurrentLogin.getInstance().lgtoken != null) {
                            return RestApi.getCalcWikiApiService().login(CurrentLogin.getInstance().username, CurrentLogin.getInstance().password, CurrentLogin.getInstance().lgtoken);
                        }
                        // Success on the first time
                        LoginModel.Success success = JSON.parseObject(s, LoginModel.Success.class);
                        if (success != null && success.login != null && success.login.lgusername != null) {
                            listener.onLoginSuccess();
                            return null;
                        }
                        // Failure on the first time
                        LoginModel.Result result = JSON.parseObject(s, LoginModel.Result.class);
                        if (result.login.result.equals("WrongPass")) {
                            listener.onLoginFailure(LoginFailureReason.PASSWORD_ERROR);
                        } else if (result.login.result.equals("NotExists")) {
                            listener.onLoginFailure(LoginFailureReason.USERNAME_NOT_EXIST);
                        } else if (result.login.result.equals("WrongToken")) {
                            listener.onLoginFailure(LoginFailureReason.TOKEN_WRONG);
                        } else {
                            listener.onLoginFailure(LoginFailureReason.NETWORK_ERROR);
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        JUtils.Log(s);
                        // Success on the second time
                        LoginModel.Success success = JSON.parseObject(s, LoginModel.Success.class);
                        if (success != null && success.login != null && success.login.lgusername != null) {
                            CurrentUser.getInstance().onLoginSuccess(success);
                            listener.onLoginSuccess();
                            CurrentLogin.clear();
                            return;
                        }
                        // Failure on the second time
                        LoginModel.Result result = JSON.parseObject(s, LoginModel.Result.class);
                        if (result.login.result.equals("WrongPass")) {
                            listener.onLoginFailure(LoginFailureReason.PASSWORD_ERROR);
                        } else if (result.login.result.equals("NotExists")) {
                            listener.onLoginFailure(LoginFailureReason.USERNAME_NOT_EXIST);
                        } else if (result.login.result.equals("WrongToken")) {
                            listener.onLoginFailure(LoginFailureReason.TOKEN_WRONG);
                        } else {
                            listener.onLoginFailure(LoginFailureReason.NETWORK_ERROR);
                        }
                    }
                });
    }
}
