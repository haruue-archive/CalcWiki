package org.calcwiki.ui.util;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对搜索结果字符串进行语法高亮处理
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SearchResultHighLight {

    public static Spanned highLightByClass(String htmlSnippet) {

        htmlSnippet = htmlSnippet.replaceAll("<span class='searchmatch'>(\\w+?)</span>", "<b>$1</b>");

        return Html.fromHtml(htmlSnippet);

    }

}
