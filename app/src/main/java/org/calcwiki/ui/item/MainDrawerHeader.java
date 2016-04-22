package org.calcwiki.ui.item;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.calcwiki.R;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.data.storage.changecaller.CurrentUserChangeCaller;
import org.calcwiki.ui.activity.LoginActivity;
import org.calcwiki.ui.activity.RegisterActivity;
import org.calcwiki.ui.dialog.LogoutDialog;
import org.calcwiki.util.Utils;

/**
 * Drawer 的头部视图
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class MainDrawerHeader implements RecyclerArrayAdapter.ItemView {


    static MainDrawerHeader mainDrawerHeader;
    View drawerLayout;
    View headerView;
    TextView usernameTextView;
    TextView emailTextView;
    ButtonFlat loginButton;
    ButtonFlat registerButton;
    ButtonFlat logoutButton;
    ButtonFlat accountManageButton;
    Listener listener = new Listener();

    public static MainDrawerHeader getInstance() {
        if (mainDrawerHeader == null) {
            mainDrawerHeader = new MainDrawerHeader();
        }
        return mainDrawerHeader;
    }

    public void initialize(View drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        headerView = ((Activity) drawerLayout.getContext()).getLayoutInflater().inflate(R.layout.header_drawer_main, parent, false);
        usernameTextView = (TextView) headerView.findViewById(R.id.username_in_drawer_main);
        emailTextView = (TextView) headerView.findViewById(R.id.email_in_drawer_main);
        loginButton = (ButtonFlat) headerView.findViewById(R.id.login_button_in_drawer_main);
        registerButton = (ButtonFlat) headerView.findViewById(R.id.register_button_in_drawer_main);
        logoutButton = (ButtonFlat) headerView.findViewById(R.id.logout_button_in_drawer_main);
        accountManageButton = (ButtonFlat) headerView.findViewById(R.id.account_manage_button_in_drawer_main);
        onBindView(headerView);
        // Add outer listener
        CurrentUserChangeCaller.getInstance().addCurrentUserListener(listener);
        return headerView;
    }

    @Override
    public void onBindView(View headerView) {
        reload();
    }

    public void refresh() {
        CurrentUser.getInstance().refreshCurrentUser();
    }

    public void reload() {
        if (CurrentUser.getInstance().hasNetWork()) {
            if (CurrentUser.getInstance().getName() != null) {
                usernameTextView.setText(CurrentUser.getInstance().getName());
            } else {
                usernameTextView.setText(Utils.getApplication().getResources().getString(R.string.loading_user_info));
            }
            if (CurrentUser.getInstance().hasLogin()) {
                setAccountManageButtonMode(AccountManageButtonMode.HAS_LOGIN);
                emailTextView.setText(CurrentUser.getInstance().getEmail());
            } else {
                setAccountManageButtonMode(AccountManageButtonMode.NO_LOGIN);
                emailTextView.setText(Utils.getApplication().getResources().getString(R.string.not_login));
            }
        } else {
            usernameTextView.setText(Utils.getApplication().getResources().getString(R.string.no_network));
            emailTextView.setText(Utils.getApplication().getResources().getString(R.string.please_cleck_network));
            setAccountManageButtonMode(AccountManageButtonMode.NO_NETWORK);
        }
    }

    public void setAccountManageButtonMode(int mode) {
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

    class Listener implements CurrentUserChangeCaller.CurrentUserChangeListener {

        @Override
        public void onCurrentUserChange() {
            reload();
        }
    }

}
