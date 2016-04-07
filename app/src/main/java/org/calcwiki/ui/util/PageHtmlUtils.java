package org.calcwiki.ui.util;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageHtmlUtils {

    public static String noTocTitle(String origHtml) {
        return origHtml.replaceAll("^<div id=\"toc\" class=\"toc\">$", "<div id=\"toc\" class=\"toc\" style=\"display: none;\">");
    }

}
