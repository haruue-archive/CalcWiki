package org.calcwiki.data.storage;

import org.calcwiki.data.model.MobileViewModel;

import java.io.Serializable;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentPage implements Serializable {

    public static CurrentPage currentPage;

    public MobileViewModel.Page page;

    public static void clear() {
        currentPage.page = null;
        currentPage = null;
    }

    public static CurrentPage getInstance() {
        if (currentPage == null) {
            currentPage = new CurrentPage();
        }
        return currentPage;
    }

    public static void restoreInstance(Serializable instance) {
        currentPage = (CurrentPage) instance;
    }

}
