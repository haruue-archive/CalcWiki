package org.calcwiki.ui.util;

import android.os.Bundle;

import org.calcwiki.data.storage.CurrentUser;

import java.io.Serializable;

/**
 * 存储和恢复 data.storage 数据
 * 在每个 Activity 中加入 <code>
 @Override
 protected void onSaveInstanceState(Bundle outState) {
 super.onSaveInstanceState(outState);
 CurrentStateStorager.save(outState);
 }

 @Override
 protected void onRestoreInstanceState(Bundle savedInstanceState) {
 super.onRestoreInstanceState(savedInstanceState);
 CurrentStateStorager.restore(savedInstanceState);
 }
 </code>
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentStateStorager {

    public static void save(Bundle outState) {
        outState.putSerializable(CurrentUser.class.getName(), CurrentUser.getInstance());
    }

    public static void restore(Bundle savedInstanceState) {
        CurrentUser.restoreInstance(savedInstanceState.getSerializable(CurrentUser.class.getName()));
    }

}
