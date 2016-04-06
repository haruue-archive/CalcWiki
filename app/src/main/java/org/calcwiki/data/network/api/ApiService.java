package org.calcwiki.data.network.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {

    @POST("api.php?action=login")
    @FormUrlEncoded
    Observable<String> login(@Field("lgname") String lgname, @Field("lgpassword") String lgpassword, @Field("lgtoken") String lgtoken);

    @POST("https://calcwiki.org/api.php?action=query&meta=userinfo")
    @FormUrlEncoded
    Observable<String> getCurrentUserInfo(@Field("uiprop") String uiprop);

    @POST("https://calcwiki.org/api.php?action=logout")
    Observable<String> logout();

    @POST("https://calcwiki.org/api.php?action=query&meta=tokens")
    @FormUrlEncoded
    Observable<String> getToken(@Field("type") String type);

    @POST("https://calcwiki.org/api.php?action=createaccount")
    @FormUrlEncoded
    Observable<String> register(@Field("name") String name, @Field("password") String password, @Field("email") String email, @Field("realname") String realname);

    @POST("https://calcwiki.org/api.php?action=query&list=search")
    @FormUrlEncoded
    Observable<String> search(@Field("srsearch") String srsearch, @Field("srwhat") String srwhat, @Field("sroffset") String sroffset);

    @POST("https://calcwiki.org/api.php?action=mobileview&sections=0-|references&noheadings=true&notransform=true")
    @FormUrlEncoded
    Observable<String> getPage(@Field("page") String pageName, @Field("redirect") String isRedirect, @Field("prop") String prop);

    @POST("https://calcwiki.org/api.php?action=mobileview&sections=-1&maxlen=1&onlyrequestedsections=true")
    @FormUrlEncoded
    Observable<String> checkPageExist(@Field("page") String pageName);

    @POST("https://calcwiki.org/index.php?action=raw")
    @FormUrlEncoded
    Observable<String> getPageSource(@Field("title") String title);

    @POST("https://calcwiki.org/index.php?action=render")
    @FormUrlEncoded
    Observable<String> getPageHtml(@Field("title") String title);

}
