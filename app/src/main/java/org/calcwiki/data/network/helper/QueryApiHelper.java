package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.QueryModel;
import org.calcwiki.data.network.api.RestApi;
import org.calcwiki.data.storage.CurrentUser;
import org.json.JSONArray;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 协助操纵 action=query 的工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class QueryApiHelper {

    public interface OnGetBaseUserInfoListener {
        void onGetBaseUserInfo();
    }

    public static void getBaseUserInfo(OnGetBaseUserInfoListener listener) {
        getBaseUserInfo(null, listener);
    }

    public static void getBaseUserInfo(String uiprop, final OnGetBaseUserInfoListener listener) {
        if (uiprop == null) {
            uiprop = "blockinfo|hasmsg|groups|editcount|email|realname|unreadcount";
        }
        RestApi.getCalcWikiApiService().getCurrentUserInfo(uiprop)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        CurrentUser.getInstance().setBaseUserInfo(JSON.parseObject(s, QueryModel.UserInfo.class).query.userinfo);
                        listener.onGetBaseUserInfo();
                    }
                });
    }
}
