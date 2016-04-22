package org.calcwiki.data.storage;

import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.model.LoginModel;
import org.calcwiki.data.model.QueryModel;
import org.calcwiki.data.network.helper.QueryApiHelper;
import org.calcwiki.data.storage.changecaller.CurrentUserChangeCaller;
import org.calcwiki.ui.receiver.NetworkConnectivityReceiver;
import org.calcwiki.util.Utils;

import java.io.Serializable;

/**
 * 登陆后的用户信息存储
 * 请放入 {@link org.calcwiki.ui.util.CurrentStateStorager}存储
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentUser implements Serializable {

    private static CurrentUser currentUser;
    private QueryModel.UserInfo.QueryEntity.UserinfoEntity userInfo;
    private boolean hasNetWork = true;
    private int groups;
    private Listener listener;

    public static class UserGroup {
        public final static int USER = 1;
        public final static int AUTOCONFIRMED = 2;
        public final static int SYSOP = 4;
        public final static int CHECKUSER = 8;
        public final static int BUREAUCRAT = 16;
    }

    public static CurrentUser getInstance() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
            currentUser.init();
        }
        return currentUser;
    }

    private void init() {
        listener = new Listener();
        refreshCurrentUser();
    }

    public void onLoginSuccess(LoginModel.Success userInfo) {
        refreshCurrentUser();
    }

    public void onLogout() {
        refreshCurrentUser();
        groups = 0;
    }

    private void refreshGroupsState() {
        groups = 0;
        if (userInfo.groups.contains("user"))
            groups |= UserGroup.USER;
        if (userInfo.groups.contains("autoconfirmed") || userInfo.groups.contains("初级善意推定"))
            groups |= UserGroup.AUTOCONFIRMED;
        if (userInfo.groups.contains("sysop"))
            groups |= UserGroup.SYSOP;
        if (userInfo.groups.contains("checkuser"))
            groups |= UserGroup.CHECKUSER;
        if (userInfo.groups.contains("bureaucrat"))
            groups |= UserGroup.BUREAUCRAT;
    }

    public void refreshCurrentUser() {
        QueryApiHelper.getBaseUserInfo(listener);
    }

    public static void restoreInstance(Serializable instance) {
        currentUser = (CurrentUser) instance;
        CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
    }

    class Listener implements QueryApiHelper.OnGetBaseUserInfoListener, NetworkConnectivityReceiver.OnNetworkStateChangeListener, Serializable {

        @Override
        public void onGetBaseUserInfoSuccess(QueryModel.UserInfo.QueryEntity.UserinfoEntity userInfo) {
            CurrentUser.this.userInfo = userInfo;
            CurrentUser.this.hasNetWork = true;
            refreshGroupsState();
            CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
        }

        @Override
        public void onGetBaseUserInfoFailure(int reason) {
            switch (reason) {
                case QueryApiHelper.GetBaseUserInfoFailureReason.NETWORK_ERROR:
                    hasNetWork = false;
                    break;
                case QueryApiHelper.GetBaseUserInfoFailureReason.SERVER_ERROR:
                    hasNetWork = true;
                    JUtils.Toast(Utils.getApplication().getResources().getString(R.string.server_exception_and_try_again));
                    CurrentUser.this.userInfo = null;
                    break;
            }
            CurrentUserChangeCaller.getInstance().notifyCurrentUserChange();
        }

        @Override
        public void onNetworkStateChange(boolean hasNetwork) {
            if (!CurrentUser.this.hasNetWork && !hasLogin() && hasNetwork) {
                refreshCurrentUser();
            }
        }
    }

    public QueryModel.UserInfo.QueryEntity.UserinfoEntity getUserInfo() {
        return userInfo;
    }

    public String getName() {
        if (userInfo == null) {
            return null;
        }
        return userInfo.name;
    }

    public String getEmail() {
        if (userInfo == null) {
            return null;
        }
        return userInfo.email;
    }

    public boolean hasNetWork() {
        return hasNetWork;
    }

    public int getGroups() {
        return groups;
    }

    public boolean hasLogin() {
        return (groups & UserGroup.USER) != 0;
    }
}
