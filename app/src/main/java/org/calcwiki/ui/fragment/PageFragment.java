package org.calcwiki.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.model.ParseModel;
import org.calcwiki.data.network.helper.PageApiHelper;
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
    ProgressBar progressBar;

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
//        PageApiHelper.getPage(pageName, isRedirect, defaultProp, new Listener());
        pageView = (WebView) view.findViewById(R.id.page_view);
        pageView.setWebViewClient(new MediaWikiWebViewClient());
        pageView.getSettings().setDisplayZoomControls(false);
        pageView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        pageView.getSettings().setUseWideViewPort(false);
        pageView.getSettings().setLoadWithOverviewMode(false);
        pageView.getSettings().setJavaScriptEnabled(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_in_page);
        showProgress();
        PageApiHelper.getPage(pageName, isRedirect, new Listener());
        refreshHeader();
        return view;
    }

    public void refreshHeader() {

    }

    public void reloadPage() {
        String head = CurrentPage.getInstance().pageData.parse.headhtml.content;
        String body = CurrentPage.getInstance().pageData.parse.text.content;
        pageView.loadDataWithBaseURL("https://calcwiki.org/", PageHtmlUtils.combinePageHtml(head, body), "text/html", "UTF-8", null);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        }, 300);
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

    public class Listener implements View.OnClickListener, PageApiHelper.GetPageApiHelperListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.redirect_title:
                    ((MainActivity) getActivity()).showPageWithoutRedirect(pageName);
                    break;
            }

        }

        @Override
        public void onGetPageSuccess(ParseModel.Page pageDate) {
            CurrentPage.getInstance().storagePage(pageDate);
            progressBar.stopNestedScroll();
            reloadPage();
        }

        @Override
        public void onGetPageFailure(int reason) {
            switch (reason) {
                case PageApiHelper.GetPageFailureReason.NETWORK_ERROR:
                    JUtils.Toast(getResources().getString(R.string.please_cleck_network));
                    break;
                case PageApiHelper.GetPageFailureReason.SERVER_ERROR:
                    JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
                    break;
                case PageApiHelper.GetPageFailureReason.PAGE_NOT_EXIST:
                    break;
            }
        }
    }

    public void showProgress() {
        pageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void showPage() {
        progressBar.setVisibility(View.GONE);
        pageView.setVisibility(View.VISIBLE);
    }

    public void showNormalHeader() {

    }

    public void showRedirectHeader() {

    }

    public void showNoSuchPageHeader() {

    }
}
