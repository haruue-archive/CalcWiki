package org.calcwiki.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.network.controller.PageCacheController;
import org.calcwiki.data.storage.CurrentFragment;
import org.calcwiki.data.storage.CurrentPage;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.client.MediaWikiWebViewClient;
import org.calcwiki.ui.util.PageHtmlUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageFragment extends CurrentFragment.InitializibleFragment {

    String pageName;
    boolean isRedirect;
    WebView pageView;
    FrameLayout headerLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    Listener listener;
    boolean hasDestoryView;
    Handler handler = new Handler(Looper.getMainLooper());

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

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ((MainActivity) getActivity()).setTitle(pageName);
        listener = new Listener();
        // Initialize base
        headerLayout = (FrameLayout) view.findViewById(R.id.header);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_in_page);
        swipeRefreshLayout.setOnRefreshListener(listener);
        // Initialize page
        pageView = (WebView) view.findViewById(R.id.page_view);
        pageView.setWebViewClient(new MediaWikiWebViewClient());
        pageView.getSettings().setDisplayZoomControls(false);
        pageView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        pageView.getSettings().setUseWideViewPort(false);
        pageView.getSettings().setLoadWithOverviewMode(false);
        pageView.getSettings().setJavaScriptEnabled(true);
        showProgress();
        listener.onRefresh();
        return view;
    }

    public void refreshHeader() {

    }

    public void reloadPage() {
        String head = CurrentPage.getInstance().pageData.parse.headhtml.content;
        String body = CurrentPage.getInstance().pageData.parse.text.content;
        pageView.loadDataWithBaseURL("https://calcwiki.org/", PageHtmlUtils.combinePageHtml(head, body), "text/html", "UTF-8", null);
        showPage();
    }

    public void refreshOptionButton() {
        //TODO: refactor these code
/*        int status;
        if (CurrentPage.getInstance().page.mobileview.editable) {
            status = MainActivity.OptionsMenuButtons.ACTION_SEARCH | MainActivity.OptionsMenuButtons.ACTION_EDIT | MainActivity.OptionsMenuButtons.ACTION_MOVE | MainActivity.OptionsMenuButtons.ACTION_HISTORY;
        } else {
            status = MainActivity.OptionsMenuButtons.ACTION_SEARCH | MainActivity.OptionsMenuButtons.ACTION_VIEW_SOURCE | MainActivity.OptionsMenuButtons.ACTION_HISTORY;
        }
        try {
            ((MainActivity) getActivity()).setOptionsMenuStatus(status);
        } catch (Exception ignored) {

        }*/
    }

    public class Listener implements View.OnClickListener, PageCacheController.PageCacheControllerListener, SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.redirect_title:
                    ((MainActivity) getActivity()).showPageWithoutRedirect(pageName);
                    break;
                case R.id.textview_no_such_page_info:
                    ((MainActivity) getActivity()).createPage(pageName);
                    break;
            }

        }

        @Override
        public void onLoadSuccess() {
            if (hasDestoryView) return;
            reloadPage();
        }

        @Override
        public void onLoadFailure(int reason) {
            if (hasDestoryView) return;
            swipeRefreshLayout.setRefreshing(false);
            switch (reason) {
                case PageCacheController.PageCacheControllerFailedReason.NETWORK_ERROR:
                    JUtils.Toast(getResources().getString(R.string.please_cleck_network));
                    showExceptionHeader(getResources().getString(R.string.please_cleck_network));
                    break;
                case PageCacheController.PageCacheControllerFailedReason.SERVER_ERROR:
                    JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
                    showExceptionHeader(getResources().getString(R.string.server_exception_and_try_again));
                    break;
                case PageCacheController.PageCacheControllerFailedReason.PAGE_NOT_EXIST:
                    showNoSuchPageHeader();
                    break;
                case PageCacheController.PageCacheControllerFailedReason.IO_EXCEPTION:
                    JUtils.Toast(getResources().getString(R.string.tip_io_exception));
                    showExceptionHeader(getResources().getString(R.string.tip_io_exception));
                    break;
                case PageCacheController.PageCacheControllerFailedReason.UNKNOW_EXCEPTION:
                    JUtils.Toast(getResources().getString(R.string.unexpected_error_please_try_again));
                    showExceptionHeader(getResources().getString(R.string.unexpected_error_please_try_again));
                    break;
            }
        }

        @Override
        public void onRefresh() {
            PageCacheController.getInstance().loadPageFromNetwork(pageName, isRedirect, this);
        }
    }

    public void showProgress() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    public void showPage() {
        showNormalHeader();
        pageView.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1200);
    }

    public void showNormalHeader() {
        if (pageName.equals("计算器百科:首页")) return;   // Don't show header for main page
        headerLayout.removeAllViews();
        View header = View.inflate(getActivity(), R.layout.header_page, null);
        ((TextView) header.findViewById(R.id.page_title)).setText(CurrentPage.getInstance().pageData.parse.displaytitle);
        headerLayout.addView(header);
    }

    public void showRedirectHeader() {

    }

    public void showNoSuchPageHeader() {
        headerLayout.removeAllViews();
        View header = View.inflate(getActivity(), R.layout.header_page, null);
        header.findViewById(R.id.no_such_page_info).setVisibility(View.VISIBLE);
        ((TextView) header.findViewById(R.id.page_title)).setText(pageName);
        TextView noSuchPageTip = (TextView) header.findViewById(R.id.textview_no_such_page_info);
        noSuchPageTip.setText(Html.fromHtml(getResources().getString(R.string.tip_no_such_page)));
        noSuchPageTip.setOnClickListener(listener);
        headerLayout.addView(header);
    }

    public void showExceptionHeader(String errorMessage) {
        headerLayout.removeAllViews();
        View header = View.inflate(getActivity(), R.layout.header_exception, null);
        ((TextView) header.findViewById(R.id.textview_exception)).setText(errorMessage);
        headerLayout.addView(header);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasDestoryView = true;
    }
}
