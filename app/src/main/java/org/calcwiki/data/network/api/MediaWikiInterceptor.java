package org.calcwiki.data.network.api;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 发送请求前在请求中加入固定参数的拦截器
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class MediaWikiInterceptor implements Interceptor {

    static MediaWikiInterceptor interceptor;

    private MediaWikiInterceptor() {

    }

    public static MediaWikiInterceptor getInstance() {
        if (interceptor == null) {
            interceptor = new MediaWikiInterceptor();
        }
        return interceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();

        Request.Builder requestBuilder = originRequest.newBuilder();

        if (originRequest.body() instanceof FormBody) {
            FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
            FormBody originFormBody = (FormBody) originRequest.body();
            for (int i = 0; i < originFormBody.size(); i++) {
                newFormBodyBuilder.addEncoded(originFormBody.encodedName(i), originFormBody.encodedValue(i));
            }

            // Add external Api params here
            newFormBodyBuilder.add("format", "json");

            // Add usually needed Api params here
            if (token != null) newFormBodyBuilder.add("token", token);
            if (captchaid != null) newFormBodyBuilder.add("captchaid", captchaid);
            if (captchaword != null) newFormBodyBuilder.add("captchaword", captchaword);
            if (isRedirect) newFormBodyBuilder.add("redirects" , "true");

            requestBuilder.method(originRequest.method(), newFormBodyBuilder.build());
        }

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    public void clearAll() {
        token = null;
        captchaid = null;
        captchaword = null;
    }

    String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void clearToken() {
        this.token = null;
    }

    String captchaid;

    public void setCaptchaid(String captchaid) {
        this.captchaid = captchaid;
    }

    public void clearCaptchaid() {
        this.captchaid = null;
    }

    String captchaword;

    public void setCaptchaword(String captchaword) {
        this.captchaword = captchaword;
    }

    public void clearCaptchaword() {
        this.captchaword = null;
    }

    public void clearCaptcha() {
        captchaid = null;
        captchaword = null;
    }

    public boolean isRedirect;

    public void setIsRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
    }

}
