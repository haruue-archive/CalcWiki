package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.QueryModel;
import org.calcwiki.data.network.api.MediaWikiInterceptor;
import org.calcwiki.data.network.api.RestApi;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 协助操纵 action=query 的工具
 * 不包括 action=query&meta=tokens 请参考 {@link TokenApiHelper}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class QueryApiHelper {

    public interface OnGetBaseUserInfoListener {
        void onGetBaseUserInfoSuccess(QueryModel.UserInfo.QueryEntity.UserinfoEntity userInfo);
        void onGetBaseUserInfoFailure(int reason);
    }

    public class GetBaseUserInfoFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
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
                        e.printStackTrace();
                        listener.onGetBaseUserInfoFailure(GetPageInfoFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        QueryModel.UserInfo data = JSON.parseObject(s, QueryModel.UserInfo.class);
                        if (data != null && data.query != null && data.query.userinfo != null) {
                            listener.onGetBaseUserInfoSuccess(data.query.userinfo);
                        } else {
                            listener.onGetBaseUserInfoFailure(GetPageInfoFailureReason.SERVER_ERROR);
                        }
                    }
                });
    }

    public interface GetPageInfoApiHelperListener {
        void onGetPageInfoSuccess(QueryModel.PageInfo pageInfo);
        void onGetPageInfoFailure(int reason);
    }

    public class GetPageInfoFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
    }

    public static void getPageInfo(String title, boolean isRedirect, final GetPageInfoApiHelperListener listener) {
        MediaWikiInterceptor.getInstance().setIsRedirect(isRedirect);
        RestApi.getCalcWikiApiService().getPageInfo(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onGetPageInfoFailure(GetPageInfoFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        // replace page id key to 'content'
                        s = s.replaceAll("\"-?[0-9]+\":\\{", "\"content\":{");
                        QueryModel.PageInfo pageInfo = JSON.parseObject(s, QueryModel.PageInfo.class);
                        if (pageInfo != null && pageInfo.query != null && pageInfo.query.pages != null && pageInfo.query.pages.content != null) {
                            listener.onGetPageInfoSuccess(pageInfo);
                            return;
                        }
                        listener.onGetPageInfoFailure(GetPageInfoFailureReason.SERVER_ERROR);
                    }
                });
    }
}
