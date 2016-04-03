package com.bitjini.my_twitter_app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bitjini on 1/4/16.
 */
public class AndroidNetworkUtility {

    public boolean isConnected(Context ctx){
        boolean flag = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            flag = true;
        }
        return flag;
    }
}
