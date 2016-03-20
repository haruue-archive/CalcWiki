package org.calcwiki.ui.item;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.calcwiki.util.Utils;

public class MainDrawerMenuItem {

    @DrawableRes int icon = -1;
    @StringRes int name = -1;

    public MainDrawerMenuItem(@DrawableRes int icon, @StringRes int name) {
        this.icon = icon;
        this.name = name;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }

    public String getName() {
        return Utils.getApplication().getResources().getString(name);
    }
}
