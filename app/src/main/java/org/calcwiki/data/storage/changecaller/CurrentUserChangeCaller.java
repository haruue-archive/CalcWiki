package org.calcwiki.data.storage.changecaller;


import java.util.ArrayList;
import java.util.List;

/**
 * 监听 CurrentUser 变化的监听器
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentUserChangeCaller {

    private CurrentUserChangeCaller() {

    }

    private static CurrentUserChangeCaller caller;

    public static CurrentUserChangeCaller getInstance() {
        if (caller == null) {
            caller = new CurrentUserChangeCaller();
        }
        return caller;
    }

    public interface CurrentUserChangeListener {
        void onCurrentUserChange();
    }

    List<CurrentUserChangeListener> listeners = new ArrayList<CurrentUserChangeListener>(0);

    public void addCurrentUserListener(CurrentUserChangeListener listener) {
        listeners.add(listener);
    }

    public void removeCurrentUserListener(CurrentUserChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyCurrentUserChange() {
        for (CurrentUserChangeListener i: listeners) {
            try {
                i.onCurrentUserChange();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
