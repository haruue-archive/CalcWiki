package org.calcwiki.data.storage;

import java.io.Serializable;

/**
 * 登录时的临时登录信息存储
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentLogin implements Serializable {

    private static CurrentLogin currentLogin;

    public String username;
    public String password;
    public boolean isRememberMe;
    public String lgtoken;

    private CurrentLogin() {

    }

    public static CurrentLogin getInstance() {
        if (currentLogin == null) {
            currentLogin = new CurrentLogin();
        }
        return currentLogin;
    }

    public static void clear() {
        currentLogin = null;
    }

}
