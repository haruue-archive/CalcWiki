package org.calcwiki.ui.drawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.data.storage.changecaller.CurrentUserChangeCaller;
import org.calcwiki.ui.activity.LoginActivity;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.activity.RegisterActivity;
import org.calcwiki.ui.adapter.MainDrawerMenuAdapter;
import org.calcwiki.ui.dialog.LogoutDialog;
import org.calcwiki.ui.item.MainDrawerMenuItem;
import org.calcwiki.ui.receiver.NetworkConnectivityReceiver;
import org.calcwiki.util.Utils;

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
        usernameView = (TextView) drawerLayout.findViewById(R.id.username_in_drawer_main);
        emailView = (TextView) drawerLayout.findViewById(R.id.email_in_drawer_main);
        // Check has login to make header view
        checkLogin();
        // set auto refresh listener
        CurrentUserChangeCaller.getInstance().addCurrentUserListener(new CurrentUserChangeCaller.CurrentUserChangeListener() {
            @Override
            public void onCurrentUserChange() {
                checkLogin();
            }
        });
        CurrentUser.getInstance().refreshCurrentUser();
        NetworkConnectivityReceiver.addNetworkStateChangeListener(new NetworkConnectivityReceiver.OnNetworkStateChangeListener() {
            @Override
            public void onNetworkStateChange(boolean hasNetwork) {
                CurrentUser.getInstance().hasNetWork = hasNetwork;
                if (hasNetwork && !CurrentUser.getInstance().isLogin) {
                    CurrentUser.getInstance().refreshCurrentUser();
                } else if (!hasNetwork && !CurrentUser.getInstance().isLogin) {
                    checkLogin();
                }
            }
        });
        // Initialize Menu List
        this.menuList = (EasyRecyclerView) drawerLayout.findViewById(R.id.menu_list_in_drawer_main);
        menuList.setLayoutManager(new LinearLayoutManager(drawerLayout.getContext()));
        menuAdapter = new MainDrawerMenuAdapter(drawerLayout.getContext());
        menuAdapter.setOnItemClickListener(new OnMenuItemClickListener());
        menuList.setAdapter(menuAdapter);
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_home_gray_800_24dp, R.string.main_page));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_schedule_gray_800_24dp, R.string.recent_changes));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_photo_filter_gray_800_24dp, R.string.random_article));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_help_outline_gray_800_24dp, R.string.help));
        menuAdapter.notifyDataSetChanged();
    }

    public void setToggle(Activity activity, Toolbar toolbar) {
        this.toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void setAccountManageButtonMode(int mode) {
        ButtonFlat loginButton = (ButtonFlat) drawerLayout.findViewById(R.id.login_button_in_drawer_main);
        ButtonFlat registerButton = (ButtonFlat) drawerLayout.findViewById(R.id.register_button_in_drawer_main);
        ButtonFlat logoutButton = (ButtonFlat) drawerLayout.findViewById(R.id.logout_button_in_drawer_main);
        ButtonFlat accountManageButton = (ButtonFlat) drawerLayout.findViewById(R.id.account_manage_button_in_drawer_main);
        // Add Listener
        loginButton.setOnClickListener(new AccountManageButtonListener());
        registerButton.setOnClickListener(new AccountManageButtonListener());
        logoutButton.setOnClickListener(new AccountManageButtonListener());
        accountManageButton.setOnClickListener(new AccountManageButtonListener());
        // Set mode
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

    class AccountManageButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_button_in_drawer_main:
                    LoginActivity.startAction(drawerLayout.getContext(), null);
                    break;
                case R.id.logout_button_in_drawer_main:
                    new LogoutDialog(drawerLayout.getContext()).show();
                    break;
                case R.id.account_manage_button_in_drawer_main:
                    break;
                case R.id.register_button_in_drawer_main:
                    RegisterActivity.startAction(drawerLayout.getContext());
                    break;
            }
        }
    }

    public void checkLogin() {
        usernameView.setText(CurrentUser.getInstance().name);
        emailView.setText(CurrentUser.getInstance().email);
        if (CurrentUser.getInstance().isLogin) {
            setAccountManageButtonMode(AccountManageButtonMode.HAS_LOGIN);
        } else if (CurrentUser.getInstance().hasNetWork) {
            setAccountManageButtonMode(AccountManageButtonMode.NO_LOGIN);
        } else if (!CurrentUser.getInstance().hasNetWork) {
            setAccountManageButtonMode(AccountManageButtonMode.NO_NETWORK);
        }
    }

    public class OnMenuItemClickListener implements RecyclerArrayAdapter.OnItemClickListener {

        @Override
        public void onItemClick(int position) {
            String itemTitle = menuAdapter.getItem(position).getTitle();
            if (itemTitle.equals(Utils.getApplication().getResources().getString(R.string.main_page))) {
                ((MainActivity) drawerLayout.getContext()).showPage("计算器百科:首页");
            }
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    /**
     * Drawer 活动时关闭输入法以防止 layout 显示异常
     * @param activity 调用的 Activity
     */
    public void setCloseIMEOnDrawerStateChange(final Activity activity) {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    JUtils.closeInputMethod(activity);
                }
            }
        });
    }

}
