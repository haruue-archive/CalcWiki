package org.calcwiki.data.storage;

import com.bumptech.glide.util.Util;

import org.calcwiki.data.model.ParseModel;
import org.calcwiki.data.model.QueryModel;
import org.calcwiki.util.Utils;

import java.io.Serializable;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentPage implements Serializable {

    public static CurrentPage currentPage;

    public ParseModel.Page pageData;

    public QueryModel.PageInfo pageInfo;

    public long refreshTime;

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

    public static void setCurrentPageFromCache(CurrentPage currentPage) {
        CurrentPage.currentPage = currentPage;
    }

    public void storagePage(ParseModel.Page pageData) {
        this.pageData = pageData;
        this.refreshTime = Utils.getCurrentTimeStamp();
    }

    public void storagePageInfo(QueryModel.PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        this.refreshTime = Utils.getCurrentTimeStamp();
    }
}
