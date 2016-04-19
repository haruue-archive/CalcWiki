package org.calcwiki.ui.util;

import org.calcwiki.util.Utils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageHtmlUtils {

    public static String combinePageHtml(String head, String body) {
        return head + Utils.zhVariantConvert(body) + "</body>" + "</html>";
    }

}
