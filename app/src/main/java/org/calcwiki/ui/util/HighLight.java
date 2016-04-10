package org.calcwiki.ui.util;

import android.text.Html;
import android.text.Spanned;

/**
 * 对字符串进行语法高亮处理
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class HighLight {

    public static Spanned highLightSearchResult(String searchResult) {

        searchResult = searchResult.replaceAll("<span class='searchmatch'>(\\w+?)</span>", "<b>$1</b>");

        return Html.fromHtml(searchResult);

    }

}
