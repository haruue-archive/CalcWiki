package org.calcwiki.ui.item;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.calcwiki.util.Utils;

public class MainDrawerMenuItem {

    @DrawableRes int iconId = -1;
    @StringRes int titleId = -1;

    public MainDrawerMenuItem(@StringRes int titleId) {
        this.iconId = -1;
        this.titleId = titleId;
    }

    public MainDrawerMenuItem(@DrawableRes int iconId, @StringRes int titleId) {
        this.iconId = iconId;
        this.titleId = titleId;
    }

    public @DrawableRes int getIconId() {
        return iconId;
    }

    public String getTitle() {
        return Utils.getApplication().getResources().getString(titleId);
    }
}
