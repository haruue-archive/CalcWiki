package org.calcwiki.data.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.calcwiki.R;
import org.calcwiki.util.Utils;

import java.io.Serializable;

import rx.functions.Action1;

public class CurrentUser {

    private static CurrentUser currentUser;
    private SharedPreferences sharedPreferences;
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
        sharedPreferences = Utils.getApplication().getSharedPreferences("CurrentUser", Activity.MODE_PRIVATE);
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
}
