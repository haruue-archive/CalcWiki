package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.TokenModel;
import org.calcwiki.data.network.api.RestApi;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 获取 token 的 API 辅助工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class TokenApiHelper {

    public class TokenType{
        public final static int CSRF = 1;
        public final static int PATROL = 2;
        public final static int ROLLBACK = 4;
        public final static int USERRIGHTS = 8;
        public final static int WATCH = 16;
    }

    public class TokenTypeString {
        public final static String CSRF = "csrf";
        public final static String PATROL = "patrol";
        public final static String ROLLBACK = "rollback";
        public final static String USERRIGHTS = "userrignts";
        public final static String WATCH = "watch";
    }

    public interface OnGetTokenListener {
        void onGetTokenSuccess(Map<Integer, String> tokens);
        void onGetTokenFailure(Throwable t);
    }

    public static Observable<Map<Integer, String>> getTokenObservable(int tokenType) {
        StringBuilder typeBuilder = new StringBuilder(4);
        if ((tokenType & TokenType.CSRF) != 0) {
            addSeparator(typeBuilder);
            typeBuilder.append(TokenTypeString.CSRF);
        }
        if ((tokenType & TokenType.PATROL) != 0) {
            addSeparator(typeBuilder);
            typeBuilder.append(TokenTypeString.PATROL);
        }
        if ((tokenType & TokenType.ROLLBACK) != 0) {
            addSeparator(typeBuilder);
            typeBuilder.append(TokenTypeString.ROLLBACK);
        }
        if ((tokenType & TokenType.USERRIGHTS) != 0) {
            addSeparator(typeBuilder);
            typeBuilder.append(TokenTypeString.USERRIGHTS);
        }
        if ((tokenType & TokenType.WATCH) != 0) {
            addSeparator(typeBuilder);
            typeBuilder.append(TokenTypeString.WATCH);
        }
        return RestApi.getCalcWikiApiService().getToken(typeBuilder.toString())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Map<Integer, String>>() {
                    @Override
                    public Map<Integer, String> call(String s) {
                        TokenModel tokens = JSON.parseObject(s, TokenModel.class);
                        HashMap<Integer, String> tokenMap = new HashMap<Integer, String>(0);
                        if (tokens == null || tokens.query == null || tokens.query.tokens == null) {
                            return tokenMap;
                        }
                        if (tokens.query.tokens.csrftoken != null) {
                            tokenMap.put(TokenType.CSRF, tokens.query.tokens.csrftoken);
                        }
                        if (tokens.query.tokens.patroltoken != null) {
                            tokenMap.put(TokenType.PATROL, tokens.query.tokens.patroltoken);
                        }
                        if (tokens.query.tokens.rollbacktoken != null) {
                            tokenMap.put(TokenType.ROLLBACK, tokens.query.tokens.rollbacktoken);
                        }
                        if (tokens.query.tokens.userrightstoken != null) {
                            tokenMap.put(TokenType.USERRIGHTS, tokens.query.tokens.userrightstoken);
                        }
                        if (tokens.query.tokens.watchtoken != null) {
                            tokenMap.put(TokenType.WATCH, tokens.query.tokens.watchtoken);
                        }
                        return tokenMap;
                    }
                });
    }

    public static Observable<String> getSingleTokenObservable(int tokenType) {
        String typeString = "";
        if (tokenType == TokenType.CSRF) {
            typeString = TokenTypeString.CSRF;
        } else if (tokenType == TokenType.PATROL) {
            typeString = TokenTypeString.PATROL;
        } else if (tokenType == TokenType.ROLLBACK) {
            typeString = TokenTypeString.ROLLBACK;
        } else if (tokenType == TokenType.USERRIGHTS) {
            typeString = TokenTypeString.USERRIGHTS;
        } else if (tokenType == TokenType.WATCH) {
            typeString = TokenTypeString.WATCH;
        }
        return RestApi.getCalcWikiApiService().getToken(typeString)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        TokenModel tokens = JSON.parseObject(s, TokenModel.class);
                        if (tokens == null || tokens.query == null || tokens.query.tokens == null) {
                            return "+\\";
                        }
                        if (tokens.query.tokens.csrftoken != null) {
                            return tokens.query.tokens.csrftoken;
                        }
                        if (tokens.query.tokens.patroltoken != null) {
                            return tokens.query.tokens.patroltoken;
                        }
                        if (tokens.query.tokens.rollbacktoken != null) {
                            return tokens.query.tokens.rollbacktoken;
                        }
                        if (tokens.query.tokens.userrightstoken != null) {
                            return tokens.query.tokens.userrightstoken;
                        }
                        if (tokens.query.tokens.watchtoken != null) {
                            return tokens.query.tokens.watchtoken;
                        }
                        return "+\\";
                    }
                });

    }

    public static void getToken(int tokenType, final OnGetTokenListener listener) {

        getTokenObservable(tokenType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<Integer, String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onGetTokenFailure(e);
                    }

                    @Override
                    public void onNext(Map<Integer, String> integerStringMap) {
                        listener.onGetTokenSuccess(integerStringMap);
                    }
                });
    }

    static StringBuilder addSeparator(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.append("|");
        }
        return sb;
    }

}
