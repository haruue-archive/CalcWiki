package org.calcwiki.data.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.calcwiki.R;
import org.calcwiki.data.model.LoginModel;
import org.calcwiki.data.model.QueryModel;
import org.calcwiki.data.network.cookie.PersistentCookieJar;
import org.calcwiki.data.storage.changecaller.CurrentUserChangeCaller;
import org.calcwiki.ui.drawer.MainDrawer;
import org.calcwiki.ui.util.CurrentStateStorager;
import org.calcwiki.util.Utils;

import java.io.Serializable;

import rx.functions.Action1;

/**
 * 登陆后的用户信息存储
 * 请放入 {@link org.calcwiki.ui.util.CurrentStateStorager}存储
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentUser implements Serializable {

    private static CurrentUser currentUser;
    public String name;
    public String email;
    public int userId;
    public String lgtoken;
    public boolean isLogin;
    public boolean hasNetWork;

    public static CurrentUser getInstance() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
            currentUser.init();
        }
        return currentUser;
    }

    private void init() {
        SharedPreferences sharedPreferences = Utils.getApplication().getSharedPreferences("CurrentUser", Activity.MODE_PRIVATE);
        // Try to get name, if not exist, use IP instead, if not available, use "not login" instead
        name = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
        userId = sharedPreferences.getInt("userId", -1);
        lgtoken = sharedPreferences.getString("lgtoken", "");
        if (userId == -1 || name.equals("")) {
            isLogin = false;
            Utils.getIP(new Action1<String>() {
                @Override
                public void call(String s) {
                    if (s.equals("")) {
                        name = Utils.getApplication().getString(R.string.no_network);
                        email = Utils.getApplication().getString(R.string.please_cleck_network);
                        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
                        hasNetWork = false;
                    } else {
                        name = s;
                        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
                        hasNetWork = true;
                    }
                }
            });
        } else {
            isLogin = true;
            hasNetWork = true;
            CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
        }
    }

    public void saveBaseInfoToSharedPreferences() {
        SharedPreferences.Editor editor = Utils.getApplication().getSharedPreferences("CurrentUser", Activity.MODE_PRIVATE).edit();
        editor.putString("username", name);
        editor.putInt("userId", userId);
        editor.putString("email", email);
        editor.putString("lgtoken", lgtoken);
        editor.apply();
    }

    public void onLoginSuccess(LoginModel.Success userInfo) {
        name = userInfo.login.lgusername;
        userId = userInfo.login.lguserid;
        lgtoken = userInfo.login.lgtoken;
        isLogin = true;
        hasNetWork = true;
        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
    }

    public void onLogout() {
        userId = -1;
        name = "";
        email = "";
        lgtoken = "";
        isLogin = false;
        saveBaseInfoToSharedPreferences();
        init();
        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
    }

    public void setBaseUserInfo(QueryModel.UserInfo.QueryEntity.UserinfoEntity userInfo) {
        name = userInfo.name;
        userId = userInfo.id;
        email = userInfo.email;
        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
        saveBaseInfoToSharedPreferences();
    }

    public void refreshCurrentUser() {
        init();
    }

    public static void restoreInstance(Serializable instance) {
        currentUser = (CurrentUser) instance;
        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
    }
}
