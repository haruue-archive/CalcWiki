package org.calcwiki;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import org.calcwiki.util.Utils;

public class App extends Application {

    public enum ObjectStorageDirs {
        Cookie,
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize ActiveAndroid
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("xxx.db").create();
        ActiveAndroid.initialize(dbConfiguration);
        // Initialize Utils
        Utils.init(this);
        JUtils.initialize(this);
        JFileManager.getInstance().init(this, ObjectStorageDirs.values());
    }
}
