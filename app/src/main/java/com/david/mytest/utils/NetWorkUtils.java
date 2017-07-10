package com.david.mytest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.david.mytest.application.BaseApplication;

/**
 * Created by weixi on 2017/7/7.
 */

public class NetWorkUtils {
    /**
     * 判断网络连接是否可用
     *
     * @return true=网络连接可用|false=不可用
     */
    public static boolean isNetworkAvailable() {

        if (null != BaseApplication.mApplication) {
            ConnectivityManager connMgr = (ConnectivityManager) BaseApplication.mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connMgr) {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (null != networkInfo && networkInfo.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }
}
