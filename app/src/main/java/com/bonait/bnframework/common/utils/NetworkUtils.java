package com.bonait.bnframework.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by LY on 2019/1/4.
 */
public class NetworkUtils {

    /**
     * 检测当的网络（WLAN、4G/3G/2G）状态，兼容Android 6.0以下
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkConnected(Context context) {
        boolean result = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (cm != null) {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            result = true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            result = true;
                        }
                    }
                }
            } else {
                if (cm != null) {
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork != null) {
                        // connected to the internet
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            result = true;
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            result = true;
                        }
                    }
                }
            }

        } catch (Exception e) {
            return false;
        }

        return result;
    }

    /**
     * 判断是否是移动网络连接
     * */
    public static boolean isActiveNetworkMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    /**
     * 判断是否是wifi
     * */
    public static boolean isActiveNetworkWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * @deprecated 请先使用 {@link NetworkUtils#isNetworkConnected(Context)} 方法
     *
     * 检测当的网络（WLAN、4G/3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    @Deprecated
    public static boolean checkNet(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    /*if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }*/
                    NetworkCapabilities networkCapabilities = connectivity.getNetworkCapabilities(connectivity.getActiveNetwork());
                    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
