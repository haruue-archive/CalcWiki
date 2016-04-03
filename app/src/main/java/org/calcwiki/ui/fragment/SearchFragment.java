package org.calcwiki.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.model.SearchModel;
import org.calcwiki.data.network.helper.PageApiHelper;
import org.calcwiki.data.network.helper.SearchApiHelper;
import org.calcwiki.data.storage.CurrentFragment;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.adapter.SearchResultListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示搜索结果的 Fragment
 * 只能放入 {@link MainActivity}
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SearchFragment extends CurrentFragment.InitializibleFragment {

    String keyWord;
    EasyRecyclerView resultRecyclerView;
    SearchResultListAdapter adapter;
    ArrayList<SearchModel.Result.QueryEntity.SearchEntity> results = new ArrayList<SearchModel.Result.QueryEntity.SearchEntity>(0);
    int nextOffset = 0;

    @Override
    public void initialize(String keyWord) {
        this.keyWord = keyWord;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.search) + " \"" + keyWord + "\"");
        resultRecyclerView = (EasyRecyclerView) view.findViewById(R.id.list_result);
        adapter = new SearchResultListAdapter(getActivity());
        resultRecyclerView.setAdapter(adapter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setNoMore(R.layout.view_no_more);
        adapter.setMore(R.layout.view_more, new Listener());
        adapter.setOnItemClickListener(new Listener());
        resultRecyclerView.setRefreshListener(new Listener());
        // Begin load first page
        resultRecyclerView.showProgress();
        SearchApiHelper.search(keyWord, 0, new Listener());
        PageApiHelper.checkPageExistState(keyWord, new Listener());
        // Toolbar
        ((MainActivity) getActivity()).setOptionsMenuStatus(MainActivity.OptionsMenuButtons.ACTION_SEARCH);
        return view;
    }

    public String getKeyWord() {
        return keyWord;
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

    class Listener implements SearchApiHelper.SearchApiHelperListener, RecyclerArrayAdapter.OnLoadMoreListener, PageApiHelper.CheckPageExistApiHelperListener, RecyclerArrayAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

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
                    resultRecyclerView.showRecycler();
                    adapter.stopMore();
                    break;
                case SearchApiHelper.SearchFailureReason.NO_MORE:
                    adapter.stopMore();
                    break;
                case SearchApiHelper.SearchFailureReason.NETWORK_ERROR:
                    resultRecyclerView.showError();
                    JUtils.Toast(getResources().getString(R.string.no_network));
                    break;
                case SearchApiHelper.SearchFailureReason.SERVER_ERROR:
                    resultRecyclerView.showError();
                    JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
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

        @Override
        public void onGetPageExistState(final boolean isPageExist) {
            adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                @Override
                public View onCreateView(ViewGroup parent) {
                    View headerView = SearchFragment.this.getActivity().getLayoutInflater().inflate(R.layout.header_search, parent, false);
                    SpannableStringBuilder colorTip;
                    if (isPageExist) {
                        String tip = String.format(getResources().getString(R.string.search_header_page_exist), keyWord);
                        colorTip = new SpannableStringBuilder(tip);
                        colorTip.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_links)), tip.lastIndexOf(keyWord), tip.lastIndexOf(keyWord) + keyWord.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        headerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((MainActivity) getActivity()).showPage(keyWord);
                            }
                        });
                    } else {
                        String tip = String.format(getResources().getString(R.string.search_header_page_not_exist_but_createable), keyWord);
                        colorTip = new SpannableStringBuilder(tip);
                        colorTip.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_links)), tip.lastIndexOf(keyWord), tip.lastIndexOf(keyWord) + keyWord.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        headerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((MainActivity) getActivity()).createPage(keyWord);
                            }
                        });
                    }
                    ((TextView) headerView.findViewById(R.id.page_exist_tip_in_search_header)).setText(colorTip);
                    return headerView;
                }

                @Override
                public void onBindView(View headerView) {

                }
            });
            adapter.getHeader(0);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onGetPageExistStateFailure(int reason) {

        }

        @Override
        public void onItemClick(int position) {
            ((MainActivity) getActivity()).showPage(adapter.getItem(position).title);
        }

        @Override
        public void onRefresh() {
            resultRecyclerView.showProgress();
            results.clear();
            adapter.clear();
            adapter.removeAllHeader();
            SearchApiHelper.search(keyWord, 0, this);
            PageApiHelper.checkPageExistState(keyWord, this);
        }
    }
}
