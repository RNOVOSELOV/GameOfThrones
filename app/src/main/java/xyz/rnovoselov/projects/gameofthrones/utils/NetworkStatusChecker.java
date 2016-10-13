package xyz.rnovoselov.projects.gameofthrones.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by novoselov on 13.10.2016.
 */

public class NetworkStatusChecker {
    public static boolean isNetworkAvailable (Context context) {
        ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo network = cm.getActiveNetworkInfo();
        return (network != null) && network.isConnectedOrConnecting();
    }
}
