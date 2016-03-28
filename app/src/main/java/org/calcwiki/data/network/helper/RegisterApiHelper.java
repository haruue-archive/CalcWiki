package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.RegisterModel;
import org.calcwiki.data.network.api.MediaWikiInterceptor;
import org.calcwiki.data.network.api.RestApi;
import org.calcwiki.data.storage.CurrentRegister;

import java.util.regex.Pattern;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 协助操纵注册（创建账户） API 的工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class RegisterApiHelper {

    public interface RegisterApiHelperListener {
        void onRegisterSuccess();
        void onRegisterFailure(int reason);
        void onRegisterCaptcha(String captchaId, String question);
    }

    public class RegisterFailureReason {
        public final static int EMPTY_USERNAME = 1;
        public final static int EMPTY_PASSWORD = 2;
        public final static int USERNAME_EXIST = 4;
        public final static int CAPTCHA_ERROR = 8;
        public final static int INVALID_USERNAME = 16;
        public final static int NETWORK_ERROR = 32;
        public final static int SERVER_ERROR = 64;
        public final static int TOKEN_WRONG = 128;
        public final static int PASSWORD_NAME_MATCH = 256;
        public final static int INVALID_EMAIL_ADDRESS_FORMAT = 512;
        public final static int PASSWORD_TOO_SHORT = 1024;
    }

    public static void register(final RegisterApiHelperListener listener) {
        final CurrentRegister data = CurrentRegister.getInstance();
        if (data.name == null || data.name.isEmpty()) {
            listener.onRegisterFailure(RegisterFailureReason.EMPTY_USERNAME);
            return;
        }
        if (data.password == null || data.password.isEmpty()) {
            listener.onRegisterFailure(RegisterFailureReason.EMPTY_PASSWORD);
            return;
        }
        if (!data.email.isEmpty() && Pattern.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$", data.email)) {
            listener.onRegisterFailure(RegisterFailureReason.INVALID_EMAIL_ADDRESS_FORMAT);
            return;
        }
        RestApi.getCalcWikiApiService().register(data.name, data.password, data.email, data.realname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        RegisterModel.NeedToken result = JSON.parseObject(s, RegisterModel.NeedToken.class);
                        if (result != null && result.createaccount != null && result.createaccount.result.equals("NeedToken")) {
                            MediaWikiInterceptor.getInstance().setToken(result.createaccount.token);
                            return RestApi.getCalcWikiApiService().register(data.name, data.password, data.email, data.realname);
                        } else {
                            return Observable.just(s);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onRegisterFailure(RegisterFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        RegisterModel.Success success = JSON.parseObject(s, RegisterModel.Success.class);
                        MediaWikiInterceptor.getInstance().clearCaptcha();
                        if (success != null && success.createaccount != null && success.createaccount.result.equals("Success")) {
                            listener.onRegisterSuccess();
                            return;
                        }
                        RegisterModel.NeedCaptcha needCaptcha = JSON.parseObject(s, RegisterModel.NeedCaptcha.class);
                        if (needCaptcha != null && needCaptcha.createaccount != null && needCaptcha.createaccount.result.equals("NeedCaptcha")) {
                            listener.onRegisterCaptcha(needCaptcha.createaccount.captcha.id, needCaptcha.createaccount.captcha.question);
                            return;
                        }
                        RegisterModel.Error error = JSON.parseObject(s, RegisterModel.Error.class);
                        if (error != null && error.error != null && error.error.code != null) {
                            int reason = RegisterFailureReason.SERVER_ERROR;
                            String code = error.error.code;
                            if (code.equals("userexists")) {
                                reason = RegisterFailureReason.USERNAME_EXIST;
                            }
                            if (code.equals("password-name-match")) {
                                reason = RegisterFailureReason.PASSWORD_NAME_MATCH;
                            }
                            if (code.equals("captcha-createaccount-fail")) {
                                reason = RegisterFailureReason.CAPTCHA_ERROR;
                            }
                            if (code.equals("invalidemailaddress")) {
                                reason = RegisterFailureReason.INVALID_EMAIL_ADDRESS_FORMAT;
                            }
                            if (code.equals("passwordtooshort")) {
                                reason = RegisterFailureReason.PASSWORD_TOO_SHORT;
                            }
                            if (code.equals("aborted") || code.equals("createaccount-hook-aborted") || code.equals("password-login-forbidden")) {
                                reason = RegisterFailureReason.INVALID_USERNAME;
                            }
                            listener.onRegisterFailure(reason);
                            return;
                        }
                        listener.onRegisterFailure(RegisterFailureReason.SERVER_ERROR);
                    }
                });
    }

    public static void register(RegisterApiHelperListener listener, String captchaId, String answerToCaptcha) {
        MediaWikiInterceptor.getInstance().setCaptchaid(captchaId);
        MediaWikiInterceptor.getInstance().setCaptchaword(answerToCaptcha);
        register(listener);
    }
}
