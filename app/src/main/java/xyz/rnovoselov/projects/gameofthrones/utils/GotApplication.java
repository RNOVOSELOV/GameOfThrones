package xyz.rnovoselov.projects.gameofthrones.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;

import xyz.rnovoselov.projects.gameofthrones.data.storage.models.DaoMaster;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.DaoSession;

/**
 * Created by roman on 13.10.16.
 */

public class GotApplication extends Application {
    private static Context sContext;
    private static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        GotApplication.sContext = getApplicationContext();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "got-db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return GotApplication.sContext;
    }

    public static DaoSession getDaoSession () {
        return sDaoSession;
    }
}
