package xyz.rnovoselov.projects.gameofthrones.data.managers;

import android.content.Context;

import xyz.rnovoselov.projects.gameofthrones.utils.GotApplication;

/**
 * Created by roman on 13.10.16.
 */

public class DataManager {

    private Context mContext;

    private DataManager() {
        mContext = GotApplication.getAppContext();
    }

    private static class DataMangerHolder {
        private final static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return DataMangerHolder.INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

}
