package org.calcwiki.data.network.helper;

import org.calcwiki.data.model.MobileViewModel;

/**
 * 协助获取各种页面内容细节的工具
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageApiHelper {

    public interface GetPageApiHelperListener {
        void onSuccess(MobileViewModel.Page pageDate);

        void onFailure(int reason);
    }

    public class GetPageFailureReason {
        public final static int NETWORK_ERROR = 1;
        public final static int SERVER_ERROR = 2;
    }

    public static void getPage(String pageName, boolean isisRedirect, int prop, GetPageApiHelperListener listener) {

        // TODO: implement this method

    }
}
