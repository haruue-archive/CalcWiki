package org.calcwiki.ui.util;

import com.jude.utils.JUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageHtmlUtils {

    public static String noTocTitle(String origHtml) {
        return origHtml.replace("<div id=\"toc\" class=\"toc\">", "<div id=\"toc\" class=\"toc\" style=\"display: none;\">");
    }

    public static String combinePageHtml(String head, String body) {
        return head + body + "</body>" + "</html>";
    }

}
