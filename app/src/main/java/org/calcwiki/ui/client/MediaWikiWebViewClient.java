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
            if (newPageName.isEmpty()) {
                newPageName = "计算器百科:首页";
            }
            // 检查特殊页面
            if (Pattern.matches("^Special:", newPageName) || Pattern.matches("^特殊:", newPageName)) {
                // TODO: 完成特殊页面处理
                return true;
            }
            // 检查重定向状态
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
}
