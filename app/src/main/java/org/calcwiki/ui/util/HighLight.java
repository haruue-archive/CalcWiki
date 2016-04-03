package org.calcwiki.ui.util;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对字符串进行语法高亮处理
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class HighLight {

    public static Spanned highLightSearchResult(String searchResult) {

        searchResult = searchResult.replaceAll("<span class='searchmatch'>(\\w+?)</span>", "<b>$1</b>");

        return Html.fromHtml(searchResult);

    }

    public static Spanned highLightSectionLine(String line, int toclevel) {

        line = String.format("<h%2$s>%1$s</h%2$s>", line, toclevel + "");
        return Html.fromHtml(line);

    }

}
