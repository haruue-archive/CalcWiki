package org.calcwiki.ui.fragment;

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
import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.data.network.helper.PageApiHelper;
import org.calcwiki.data.storage.CurrentFragment;
import org.calcwiki.data.storage.CurrentPage;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.client.MediaWikiWebViewClient;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageFragment extends CurrentFragment.InitializibleFragment {

    String pageName;
    boolean isRedirect;
    String defaultProp = "id|text|sections|lastmodifiedby|revision|editable|languagecount|hasvariants|displaytitle|description|contentmodel";
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ((MainActivity) getActivity()).setTitle(pageName);
//        PageApiHelper.getPage(pageName, isRedirect, defaultProp, new Listener());
        pageView = (WebView) view.findViewById(R.id.page_view);
        pageView.setWebViewClient(new MediaWikiWebViewClient());
        pageView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_in_page);
        showProgress();
        PageApiHelper.getPageHtml(pageName, new Listener());
        refreshHeader();
        return view;
    }

    public void refreshHeader() {

    }

    public void reloadPage() {
        pageView.loadDataWithBaseURL("https://calcwiki.org/", CurrentPage.getInstance().getHtmlData(), "text/html", "UTF-8", null);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        }, 300);
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

    public class Listener implements PageApiHelper.GetPageApiHelperListener, View.OnClickListener, PageApiHelper.GetPageHtmlApiHelperListener {

        @Override
        public void onGetPageSuccess(MobileViewModel.Page pageDate) {
            CurrentPage.getInstance().storagePage(pageDate);
            refreshHeader();
            refreshOptionButton();
            reloadPage();
        }

        @Override
        public void onGetPageFailure(int reason) {
            switch (reason) {
                case PageApiHelper.GetPageFailureReason.NETWORK_ERROR:
                    JUtils.Toast(getResources().getString(R.string.no_network));
                    break;
                case PageApiHelper.GetPageFailureReason.PAGE_NOT_EXIST:
                    break;
                case PageApiHelper.GetPageFailureReason.SERVER_ERROR:
                    JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
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

        @Override
        public void onGetPageHtmlSuccess(String pageHtml) {
            progressBar.stopNestedScroll();
            CurrentPage.getInstance().storageHtmlData(pageHtml);
            reloadPage();
        }

        @Override
        public void onGetPageHtmlFailure(int reason) {

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
}
