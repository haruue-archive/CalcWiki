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
    Observable<String> register(@Field("name") String name, @Field("password") String password, @Field("email") String email, @Field("realname") String realname, @Field("language") String language);

}
