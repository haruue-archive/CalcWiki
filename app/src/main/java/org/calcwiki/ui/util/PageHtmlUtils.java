package org.calcwiki.ui.util;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageHtmlUtils {

    public static String combinePageHtml(String head, String body) {
        return head + body + "</body>" + "</html>";
    }

}
