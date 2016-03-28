package org.calcwiki.data.storage;

import java.io.Serializable;

/**
 * 注册时的临时信息存储
 * 临时保存，请勿放入 CurrentStateStorager 进行存储
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentRegister {

    private static CurrentRegister currentRegister;

    public String name;
    public String password;
    public String token;
    public String email;
    public String realname;
    public String language;

    private CurrentRegister() {

    }

    public static CurrentRegister getInstance() {
        if (currentRegister == null) {
            currentRegister = new CurrentRegister();
        }
        return currentRegister;
    }

    public static void clear() {
        currentRegister = null;
    }

    public static void restoreInstance(Serializable instance) {
        currentRegister = (CurrentRegister) instance;
    }

}
