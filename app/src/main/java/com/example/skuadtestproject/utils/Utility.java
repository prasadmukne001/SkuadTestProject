package com.example.skuadtestproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Prasad Mukne on 22-06-2021.
 */
public class Utility {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
