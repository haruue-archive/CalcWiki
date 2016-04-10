package org.calcwiki.ui.client;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.calcwiki.ui.activity.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class MediaWikiWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Pattern.matches("^[Hh][Tt][Tt][Pp]([Ss])*://([Ww][Ww][Ww]\\.)*[Cc][Aa][Ll][Cc][Ww][Ii][Kk][Ii]\\.[Oo][Rr][Gg].*", url)) {
            // url 预处理
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = url.replaceAll("^[Hh][Tt][Tt][Pp]([Ss])*://([Ww][Ww][Ww]\\.)*[Cc][Aa][Ll][Cc][Ww][Ii][Kk][Ii]\\.[Oo][Rr][Gg]", "https://calcwiki.org");
            // 获取页面标题
            String newPageName;
            int start;
            if (Pattern.matches(".*title=.*", url)) {
                start = url.indexOf("title=") + 6;
            } else {
                start = url.indexOf("calcwiki.org/") + 13;
            }
            int end = url.indexOf("&", start);
            if (start == -1) {
                newPageName = "计算器百科:首页";
            } else if (end == -1) {
                newPageName = url.substring(start);
            } else {
                newPageName = url.substring(start, end);
            }
            // 处理 MediaWiki URL 空格转义
            newPageName = newPageName.replaceAll("_", " ");
            // 判断 https://calcwiki.org 或者 https://calcwiki.org/index.php 这种页面
            if (newPageName.isEmpty()) {
                newPageName = "计算器百科:首页";
            }
            // 检查特殊页面
            if (Pattern.matches("^Special:", newPageName) || Pattern.matches("^特殊:", newPageName)) {
                // TODO: 完成特殊页面处理
                return true;
            }
            // 检查重定向状态并在新的 Fragment 里打开
            if (Pattern.matches("redirect=no", url)) {
                ((MainActivity) view.getContext()).showPageWithoutRedirect(newPageName);
            } else {
                ((MainActivity) view.getContext()).showPage(newPageName);
            }
        } else {
            // 使用浏览器打开
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        return true;
    }


    // 在这里加入手机端 JavaScript
    public final static String mobileJavaScript =
            // 页面左右 padding
            "$('body').css('padding-left', '3%');" +
                    "$('body').css('padding-right', '3%');" +
                    // 宽 InfoBox
                    "$('.infoBox').css('width', '90%');" +
                    // 半隐黑幕
                    "$('.heimu').css('background-color', '#aaaaaa');" +
                    // 不要把列表显示成三列表格
                    "$('li').css('width', '100%');" +
                    // 首页列表处理
                    "$('.all-page-div-in-main-page').css('width', '100%');";


    @Override
    public void onPageFinished(WebView view, String url) {
        view.evaluateJavascript(mobileJavaScript, null);
    }
}
