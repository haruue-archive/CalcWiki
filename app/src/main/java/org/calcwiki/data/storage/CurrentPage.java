package org.calcwiki.data.storage;

import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.ui.util.HighLight;
import org.calcwiki.util.Utils;

import java.io.Serializable;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentPage implements Serializable {

    public static CurrentPage currentPage;

    public MobileViewModel.Page page;

    public String htmlData;

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

    public synchronized void storagePage(MobileViewModel.Page page) {
        this.page = page;
        StringBuilder htmlDataBuider = new StringBuilder();
        for (MobileViewModel.Page.MobileviewEntity.SectionsEntity i: page.mobileview.sections) {
            if (i.line != null && !i.line.isEmpty()) {
                htmlDataBuider.append(HighLight.highLightSectionLine(i.line, i.toclevel));
            }
            if (i.text != null && !i.text.isEmpty()) {
                htmlDataBuider.append(i.text);
            }
        }
        htmlData = htmlDataBuider.toString();
    }

    public void storageHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    public String getHtmlData() {
        return htmlData;
    }
}
