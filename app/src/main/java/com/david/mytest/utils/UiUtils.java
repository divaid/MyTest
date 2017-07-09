package com.david.mytest.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.david.mytest.application.BaseApplication;

/**
 * Created by hnthgys on 2016/7/1.
 */
public class UiUtils {

    /**
     * 对外提供上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.mApplication;
    }

    public static int dip2px(float dpValue){
        return dip2px(getContext(), dpValue);
    }

    /**
     * 根据手机分辨率,将dp转换为px(像素)
     * density值表示每英寸有多少个显示点
     */
    private static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue){
        return px2dip(getContext(), pxValue);
    }

    /**
     * 根据手机分辨率,将px转换为dp
     * density值表示每英寸有多少个显示点
     */
    private static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取版本名称
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
    }


}
