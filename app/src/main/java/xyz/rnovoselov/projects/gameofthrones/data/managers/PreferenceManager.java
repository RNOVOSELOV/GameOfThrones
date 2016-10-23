package xyz.rnovoselov.projects.gameofthrones.data.managers;

import android.content.SharedPreferences;

import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;
import xyz.rnovoselov.projects.gameofthrones.utils.GotApplication;

/**
 * Created by roman on 24.10.16.
 */

public class PreferenceManager {

    private SharedPreferences mPreferences;

    public PreferenceManager() {
        mPreferences = GotApplication.getSharedPreferences();
    }

    public void writeLastDbUpdateTime(long time) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(ConstantManager.DB_LAST_UPDATED_TIME, time);
        editor.apply();
    }

    public long getLastDbUpdateTime () {
        return mPreferences.getLong(ConstantManager.DB_LAST_UPDATED_TIME, 0);
    }

}
