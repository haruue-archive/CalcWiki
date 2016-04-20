package org.calcwiki.data.network.api;

import org.calcwiki.data.network.converter.StringConverterFactory;
import org.calcwiki.data.network.cookie.PersistentCookieJar;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class RestApi {

    private static ApiService calcWikiApiService;

    public static ApiService getCalcWikiApiService() {
        return calcWikiApiService;
    }

    public static void init() {

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new PersistentCookieJar())
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(MediaWikiInterceptor.getInstance())
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofitBuilderForCalcWikiApi = new Retrofit.Builder()
                .baseUrl("https://calcwiki.org/")
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        calcWikiApiService = retrofitBuilderForCalcWikiApi.create(ApiService.class);
    }
}
