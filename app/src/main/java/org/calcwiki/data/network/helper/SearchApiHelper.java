package org.calcwiki.data.network.helper;

import com.alibaba.fastjson.JSON;

import org.calcwiki.data.model.SearchModel;
import org.calcwiki.data.network.api.RestApi;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 协助搜索
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SearchApiHelper {

    public interface SearchApiHelperListener {
        void onSearchResult(List<SearchModel.Result.QueryEntity.SearchEntity> result, int nextOffset);
        void onSearchFailure(int reason);
    }

    public class SearchFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int EMPTY_RESULT = 2;
        public final static int SERVER_ERROR = 4;
        public final static int NO_MORE = 8;
    }

    public static void search(String keyWord, int offset, final SearchApiHelperListener listener) {
        RestApi.getCalcWikiApiService().search(keyWord, "text", offset + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onSearchFailure(SearchFailureReason.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(String s) {
                        SearchModel.Result result = JSON.parseObject(s, SearchModel.Result.class);
                        if (result == null || result.query == null) {
                            listener.onSearchFailure(SearchFailureReason.SERVER_ERROR);
                            return;
                        }
                        if (result.query.search == null || result.query.search.isEmpty()) {
                            if (result.query.searchinfo.totalhits == 0) {
                                listener.onSearchFailure(SearchFailureReason.EMPTY_RESULT);
                            } else {
                                listener.onSearchFailure(SearchFailureReason.NO_MORE);
                            }
                            return;
                        }
                        if (result.continueX != null && result.continueX.continueX != null) {
                            listener.onSearchResult(result.query.search, result.continueX.sroffset);
                        } else {
                            listener.onSearchResult(result.query.search, -1);
                        }
                    }
                });

    }

}
