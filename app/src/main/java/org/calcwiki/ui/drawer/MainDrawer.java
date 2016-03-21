package org.calcwiki.ui.drawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.calcwiki.R;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.ui.adapter.MainDrawerMenuAdapter;
import org.calcwiki.ui.item.MainDrawerMenuItem;
import org.calcwiki.util.Utils;

import rx.functions.Action1;

public class MainDrawer {

    static MainDrawer mainDrawer;
    DrawerLayout drawerLayout;
    EasyRecyclerView menuList;
    MainDrawerMenuAdapter menuAdapter;
    TextView usernameView;
    TextView emailView;
    ActionBarDrawerToggle toggle;

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
        // Try to get name, if not exist, use IP instead, if not available, use "not login" instead
        usernameView = (TextView) drawerLayout.findViewById(R.id.username_in_drawer_main);
        emailView = (TextView) drawerLayout.findViewById(R.id.email_in_drawer_main);
        // Check has login to make header view
        checkLogin();
        // Initialize Menu List
        this.menuList = (EasyRecyclerView) drawerLayout.findViewById(R.id.menu_list_in_drawer_main);
        menuList.setLayoutManager(new LinearLayoutManager(drawerLayout.getContext()));
        menuAdapter = new MainDrawerMenuAdapter(drawerLayout.getContext());
        menuAdapter.setOnItemClickListener(new OnMenuItemClickListener());
        menuList.setAdapter(menuAdapter);
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_home_gray_800_24dp, R.string.main_page));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_restore_gray_800_24dp, R.string.recent_changes));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_photo_filter_gray_800_24dp, R.string.random_article));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_help_outline_gray_800_24dp, R.string.help));
        menuAdapter.notifyDataSetChanged();
    }

    public void setToggle(Activity activity, Toolbar toolbar) {
        this.toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void refreshUser() {
        usernameView.setText(CurrentUser.getInstance().name);
    }

    public void setAccountManageButtonMode(int mode) {
        ButtonFlat loginButton = (ButtonFlat) drawerLayout.findViewById(R.id.login_button_in_drawer_main);
        ButtonFlat registerButton = (ButtonFlat) drawerLayout.findViewById(R.id.register_button_in_drawer_main);
        ButtonFlat logoutButton = (ButtonFlat) drawerLayout.findViewById(R.id.logout_button_in_drawer_main);
        ButtonFlat accountManageButton = (ButtonFlat) drawerLayout.findViewById(R.id.account_manage_button_in_drawer_main);
        switch (mode) {
            case AccountManageButtonMode.HAS_LOGIN:
                loginButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                accountManageButton.setVisibility(View.VISIBLE);
                break;
            case AccountManageButtonMode.NO_LOGIN:
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.GONE);
                accountManageButton.setVisibility(View.GONE);
                break;
            case AccountManageButtonMode.NO_NETWORK:
                loginButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.GONE);
                accountManageButton.setVisibility(View.GONE);
                break;
        }
    }

    public class AccountManageButtonMode {
        final static int HAS_LOGIN = 1;
        final static int NO_LOGIN = 2;
        final static int NO_NETWORK = 3;
    }

    /**
     * 如果当前没有网络，请调用此函数
     */
    public void onNoNetWork() {
        setAccountManageButtonMode(AccountManageButtonMode.NO_NETWORK);
        // set special tip in username and email view
        usernameView.setText(R.string.no_network);
        emailView.setText(R.string.please_cleck_network);
    }

    /**
     * 如果用户没有登录，请调用此函数
     */
    public void onNoLogin() {
        Utils.getIP(new Action1<String>() {
            @Override
            public void call(String s) {
                if (s.equals("")) {
                    onNoNetWork();
                } else {
                    usernameView.setText(s);
                    emailView.setText(R.string.not_login);
                }
            }
        });
    }

    /**
     * 如果认为用户已经登录了，并且数据已经加载到了 CurrentUser ，请调用此函数
     */
    public void onHasLogin() {
        if (CurrentUser.getInstance().isLogin && !CurrentUser.getInstance().name.equals("") && !CurrentUser.getInstance().email.equals("")) {
                usernameView.setText(CurrentUser.getInstance().name);
                usernameView.setText(CurrentUser.getInstance().email);
        } else {
            onNoLogin();
        }
    }

    public void checkLogin() {
        onHasLogin();
    }

    public class OnMenuItemClickListener implements RecyclerArrayAdapter.OnItemClickListener {

        @Override
        public void onItemClick(int position) {

        }
    }

}
