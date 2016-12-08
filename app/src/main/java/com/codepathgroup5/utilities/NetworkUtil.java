package com.codepathgroup5.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;


public class NetworkUtil {
        private static final String TAG = "NetworkUtil";
        private static NetworkUtil networkUtil;

        public static synchronized NetworkUtil getInstance() {
            if ( networkUtil == null ) {
                networkUtil = new NetworkUtil();
            }
            return networkUtil;
        }


        public boolean connectionPermitted(Context context){
            return isNetworkAvailable(context) && isOnline();
        }

        private Boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }

        public boolean isOnline() {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return false;
        }

}