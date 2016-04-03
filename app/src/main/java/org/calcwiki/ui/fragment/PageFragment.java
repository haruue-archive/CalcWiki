package org.calcwiki.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.calcwiki.data.storage.CurrentFragment;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageFragment extends CurrentFragment.InitializibleFragment {

    // TODO: Complete this module

    String pageName;

    public String getPageName() {
        return pageName;
    }

    @Override
    public void initialize(String pageName) {
        this.pageName = pageName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
