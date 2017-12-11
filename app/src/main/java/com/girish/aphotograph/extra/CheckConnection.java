package com.girish.aphotograph.extra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Girish on 11-Dec-17.
 */

public class CheckConnection {

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = null;
        if (context.getSystemService(Context.CONNECTIVITY_SERVICE) != null) {
            info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        }

        return info != null && info.isConnected();
    }

}
