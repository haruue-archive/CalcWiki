package org.calcwiki.ui.drawer;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import org.calcwiki.R;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.ui.adapter.MainDrawerMenuAdapter;
import org.calcwiki.util.Utils;

import rx.functions.Action1;

public class MainDrawer {

    static MainDrawer mainDrawer;
    DrawerLayout drawerLayout;
    EasyRecyclerView menuList;
    TextView usernameView;

    private MainDrawer() {

    }

    public static MainDrawer getInstance() throws NullPointerException {
        if (mainDrawer == null) {
            throw new NullPointerException("MainDrawer may not be initialize.");
        }
        return mainDrawer;
    }

    public static MainDrawer getInstance(DrawerLayout drawerLayout) {
        if (mainDrawer == null) {
            mainDrawer = new MainDrawer();
        }
        if (!drawerLayout.equals(mainDrawer.drawerLayout)) {
            mainDrawer.drawerLayout = drawerLayout;
            mainDrawer.init();
        }
        return mainDrawer;
    }

    private void init() {
        this.menuList = (EasyRecyclerView) drawerLayout.findViewById(R.id.menu_list_in_drawer_main);
        menuList.setLayoutManager(new LinearLayoutManager(drawerLayout.getContext()));
        menuList.setAdapter(new MainDrawerMenuAdapter(drawerLayout.getContext()));
        // Try to get name, if not exist, use IP instead, if not available, use "not login" instead
        usernameView = (TextView) drawerLayout.findViewById(R.id.username_in_drawer_main);
        final String name = CurrentUser.getInstance().name;
        if (name.equals("")) {
            Utils.getIP(new Action1<String>() {
                @Override
                public void call(String s) {
                    if (s.equals("")) {
                        usernameView.setText(Utils.getApplication().getResources().getString(R.string.not_login));
                    } else {
                        usernameView.setText(s);
                    }
                }
            });
        } else {
            usernameView.setText(CurrentUser.getInstance().name);
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void addDrawerListener(DrawerLayout.DrawerListener listener) {
        drawerLayout.addDrawerListener(listener);
    }

    public void refreshUser() {
        usernameView.setText(CurrentUser.getInstance().name);
    }
}
