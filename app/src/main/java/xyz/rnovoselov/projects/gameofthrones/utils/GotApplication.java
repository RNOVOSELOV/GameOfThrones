package xyz.rnovoselov.projects.gameofthrones.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by roman on 13.10.16.
 */

public class GotApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        GotApplication.sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return GotApplication.sContext;
    }
}
