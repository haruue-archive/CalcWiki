package org.calcwiki.ui.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.data.network.helper.PageApiHelper;
import org.calcwiki.data.storage.CurrentFragment;
import org.calcwiki.data.storage.CurrentPage;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.adapter.PageSectionAdapter;
import org.w3c.dom.Text;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageFragment extends CurrentFragment.InitializibleFragment {

    String pageName;
    boolean isRedirect;
    EasyRecyclerView contentRecyclerView;
    PageSectionAdapter adapter;
    String defaultProp = "id|text|sections|lastmodifiedby|revision|editable|languagecount|hasvariants|displaytitle|description|contentmodel";

    public String getPageName() {
        return isRedirect ? pageName : pageName + "#NO_REDIRECT";
    }

    @Override
    public void initialize(String pageName) {
        if (pageName.contains("#NO_REDIRECT")) {
            isRedirect = false;
            this.pageName = pageName.replace("#NO_REDIRECT", "");
        } else {
            isRedirect = true;
            this.pageName = pageName;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ((MainActivity) getActivity()).setTitle(pageName);
        contentRecyclerView = (EasyRecyclerView) view.findViewById(R.id.page_content);
        adapter = new PageSectionAdapter(getActivity());
        contentRecyclerView.setAdapter(adapter);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentRecyclerView.setRefreshListener(new Listener());
        contentRecyclerView.showProgress();
        PageApiHelper.getPage(pageName, isRedirect, defaultProp, new Listener());
        refreshHeader();
        return view;
    }

    public void refreshHeader() {
        adapter.removeAllHeader();
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView = getActivity().getLayoutInflater().inflate(R.layout.header_page, parent, false);
                TextView titleTextView = (TextView) headerView.findViewById(R.id.page_title);
                LinearLayout redirectInfoView = (LinearLayout) headerView.findViewById(R.id.redirect_info);
                if (CurrentPage.getInstance().page == null || CurrentPage.getInstance().page.mobileview.redirected == null) {
                    titleTextView.setText(pageName);
                    redirectInfoView.setVisibility(View.GONE);
                } else {
                    titleTextView.setText(CurrentPage.getInstance().page.mobileview.redirected);
                    TextView redirectFromTitle = (TextView) headerView.findViewById(R.id.redirect_title);
                    redirectFromTitle.setText(pageName);
                    redirectFromTitle.setOnClickListener(new Listener());
                    redirectInfoView.setVisibility(View.VISIBLE);
                }
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }

    public void reloadPage() {
        adapter.clear();
        adapter.addAll(CurrentPage.getInstance().page.mobileview.sections);
        adapter.notifyDataSetChanged();
    }

    public void refreshOptionButton() {
        int status;
        if (CurrentPage.getInstance().page.mobileview.editable) {
            status = MainActivity.OptionsMenuButtons.ACTION_SEARCH | MainActivity.OptionsMenuButtons.ACTION_EDIT | MainActivity.OptionsMenuButtons.ACTION_MOVE | MainActivity.OptionsMenuButtons.ACTION_HISTORY;
        } else {
            status = MainActivity.OptionsMenuButtons.ACTION_SEARCH | MainActivity.OptionsMenuButtons.ACTION_VIEW_SOURCE | MainActivity.OptionsMenuButtons.ACTION_HISTORY;
        }
        try {
            ((MainActivity) getActivity()).setOptionsMenuStatus(status);
        } catch (Exception ignored) {

        }
    }

    public class Listener implements RecyclerArrayAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, PageApiHelper.GetPageApiHelperListener, View.OnClickListener {

        /**
         * Item Click not here, find it in {@link org.calcwiki.ui.adapter.PageSectionAdapter.PageSectionViewHolder.ExpandListener}
         * @param position position of item
         */
        @Override
        public void onItemClick(int position) {
        }

        @Override
        public void onRefresh() {
            PageApiHelper.getPage(pageName, isRedirect, defaultProp, this);
        }

        @Override
        public void onGetPageSuccess(MobileViewModel.Page pageDate) {
            contentRecyclerView.showRecycler();
            CurrentPage.getInstance().page = pageDate;
            refreshHeader();
            refreshOptionButton();
            reloadPage();
        }

        @Override
        public void onGetPageFailure(int reason) {
            switch (reason) {
                case PageApiHelper.GetPageFailureReason.NETWORK_ERROR:
                    JUtils.Toast(getResources().getString(R.string.no_network));
                    contentRecyclerView.showError();
                    break;
                case PageApiHelper.GetPageFailureReason.PAGE_NOT_EXIST:
                    contentRecyclerView.showRecycler();
                    adapter.removeAllHeader();
                    adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                        @Override
                        public View onCreateView(ViewGroup parent) {
                            View headerView = PageFragment.this.getActivity().getLayoutInflater().inflate(R.layout.header_search, parent, false);
                            SpannableStringBuilder colorTip;
                                String tip = String.format(getResources().getString(R.string.search_header_page_not_exist_but_createable), pageName);
                                colorTip = new SpannableStringBuilder(tip);
                                colorTip.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_links)), tip.lastIndexOf(pageName), tip.lastIndexOf(pageName) + pageName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                headerView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((MainActivity) getActivity()).createPage(pageName);
                                    }
                                });
                            ((TextView) headerView.findViewById(R.id.page_exist_tip_in_search_header)).setText(colorTip);
                            return headerView;
                        }

                        @Override
                        public void onBindView(View headerView) {

                        }
                    });
                    ((MainActivity) getActivity()).setOptionsMenuStatus(MainActivity.OptionsMenuButtons.ACTION_SEARCH | MainActivity.OptionsMenuButtons.ACTION_CREATE);
                    break;
                case PageApiHelper.GetPageFailureReason.SERVER_ERROR:
                    JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
                    contentRecyclerView.showError();
                    break;
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.redirect_title:
                    ((MainActivity) getActivity()).showPageWithoutRedirect(pageName);
                    break;
            }

        }
    }
}
