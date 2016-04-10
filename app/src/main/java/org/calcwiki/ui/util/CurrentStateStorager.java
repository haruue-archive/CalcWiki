package org.calcwiki.ui.util;

import android.os.Bundle;

import org.calcwiki.data.storage.CurrentFragment;
import org.calcwiki.data.storage.CurrentPage;
import org.calcwiki.data.storage.CurrentUser;

import java.io.Serializable;

/**
 * 存储和恢复 data.storage 数据
 * 在每个 Activity 中加入 {@code

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

 }
 * 每个被存储的对象都采取单例模式，并且 implements {@link Serializable} ，并实现方法 {@code

    public static {@link Serializable} getInstance() {
        // 在这里返回单例
    }

    public static void restoreInstance({@link Serializable} instance) {
        // 在这里将 instance 恢复到当前单例
    }

}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CurrentStateStorager {

    public static void save(Bundle outState) {
        outState.putSerializable(CurrentUser.class.getName(), CurrentUser.getInstance());
        outState.putSerializable(CurrentFragment.class.getName(), CurrentFragment.getInstance());
        outState.putSerializable(CurrentPage.class.getName(), CurrentPage.getInstance());
    }

    public static void restore(Bundle savedInstanceState) {
        CurrentUser.restoreInstance(savedInstanceState.getSerializable(CurrentUser.class.getName()));
        CurrentFragment.restoreInstance(savedInstanceState.getSerializable(CurrentFragment.class.getName()));
        CurrentPage.restoreInstance(savedInstanceState.getSerializable(CurrentPage.class.getName()));
    }

}
