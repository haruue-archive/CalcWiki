package org.calcwiki.data.storage;

import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.data.model.ParseModel;
import org.calcwiki.ui.util.HighLight;
import org.calcwiki.util.Utils;

import java.io.Serializable;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentPage implements Serializable {

    public static CurrentPage currentPage;

    public ParseModel.Page pageData;

    public static void clear() {
        currentPage.pageData = null;
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

    public void storagePage(ParseModel.Page pageData) {
        this.pageData = pageData;
    }

}
