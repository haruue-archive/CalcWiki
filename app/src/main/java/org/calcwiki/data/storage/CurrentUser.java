package org.calcwiki.data.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.calcwiki.R;
import org.calcwiki.data.model.LoginModel;
import org.calcwiki.util.Utils;

import java.io.Serializable;

import rx.functions.Action1;

/**
 * 登陆后的用户信息存储
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentUser implements Serializable {

    private static CurrentUser currentUser;
    public String name;
    public String email;
    public boolean isLogin;

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
        if (name.equals("")) {
            Utils.getIP(new Action1<String>() {
                @Override
                public void call(String s) {
                    if (s.equals("")) {
                        name = Utils.getApplication().getString(R.string.not_login);
                    } else {
                        name = s;
                    }
                }
            });
            isLogin = false;
        } else {
            isLogin = true;
        }
    }

    public void onLoginSuccess(LoginModel.Success userInfo) {
        // TODO: implements this method
    }
}
