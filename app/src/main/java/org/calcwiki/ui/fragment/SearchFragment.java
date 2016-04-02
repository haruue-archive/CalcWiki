package org.calcwiki.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.calcwiki.R;
import org.calcwiki.data.model.SearchModel;
import org.calcwiki.data.network.helper.SearchApiHelper;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.adapter.SearchResultListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示搜索结果的 Fragment
 * 只能放入 {@link MainActivity}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SearchFragment extends Fragment {

    String keyWord;
    EasyRecyclerView resultRecyclerView;
    SearchResultListAdapter adapter;
    ArrayList<SearchModel.Result.QueryEntity.SearchEntity> results = new ArrayList<SearchModel.Result.QueryEntity.SearchEntity>(0);
    int nextOffset = 0;

    public SearchFragment initialize(String keyWord) {
        this.keyWord = keyWord;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        resultRecyclerView = (EasyRecyclerView) view.findViewById(R.id.list_result);
        adapter = new SearchResultListAdapter(getActivity());
        resultRecyclerView.setAdapter(adapter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setNoMore(R.layout.view_no_more);
        adapter.setMore(R.layout.view_more, new Listener());
        // Begin load first page
        resultRecyclerView.showProgress();
        SearchApiHelper.search(keyWord, 0, new Listener());
        // Toolbar
        ((MainActivity) getActivity()).setOptionsMenuStatus(MainActivity.OptionsMenuButtons.ACTION_SEARCH);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(this.getClass().getName() + "keyWord", keyWord);
        outState.putSerializable(this.getClass().getName() + "results", results);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            keyWord = savedInstanceState.getString(this.getClass().getName() + "keyWord");
            results.addAll((ArrayList<SearchModel.Result.QueryEntity.SearchEntity>) savedInstanceState.getSerializable(this.getClass().getName() + "results"));
            adapter.addAll(results);
        }
    }

    class Listener implements SearchApiHelper.SearchApiHelperListener, RecyclerArrayAdapter.OnLoadMoreListener {

        @Override
        public void onSearchResult(List<SearchModel.Result.QueryEntity.SearchEntity> result, int nextOffset) {
            SearchFragment.this.results.addAll(result);
            adapter.addAll(result);
            SearchFragment.this.nextOffset = nextOffset;
            if (nextOffset == -1) {
                adapter.stopMore();
            }
            adapter.notifyDataSetChanged();
            resultRecyclerView.showRecycler();
        }

        @Override
        public void onSearchFailure(int reason) {
            switch (reason) {
                case SearchApiHelper.SearchFailureReason.EMPTY_RESULT:
                    resultRecyclerView.showEmpty();
                    break;
                case SearchApiHelper.SearchFailureReason.NO_MORE:
                    adapter.stopMore();
                    break;
                case SearchApiHelper.SearchFailureReason.NETWORK_ERROR:
                    resultRecyclerView.showError();
                    break;
                case SearchApiHelper.SearchFailureReason.SERVER_ERROR:
                    resultRecyclerView.showError();
                    break;
            }

        }

        @Override
        public void onLoadMore() {
            if (nextOffset == -1) {
                adapter.stopMore();
            } else {
                SearchApiHelper.search(keyWord, nextOffset, new Listener());
            }
        }
    }
}
