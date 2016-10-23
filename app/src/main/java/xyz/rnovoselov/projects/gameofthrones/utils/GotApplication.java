package xyz.rnovoselov.projects.gameofthrones.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    private static SharedPreferences mPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        GotApplication.sContext = getApplicationContext();
        GotApplication.mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "got-db");
        Database db = helper.getEncryptedWritableDb(ConstantManager.DB_PASSPHRASE);
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return GotApplication.sContext;
    }

    public static SharedPreferences getSharedPreferences () {
        return mPreference;
    }

    public static DaoSession getDaoSession () {
        return sDaoSession;
    }
}
