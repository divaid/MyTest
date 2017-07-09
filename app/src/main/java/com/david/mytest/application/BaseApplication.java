package com.david.mytest.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by david on 2016/7/1.
 */
public class BaseApplication extends Application {
    public static BaseApplication mApplication;

    public static RefWatcher mWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        mWatcher = LeakCanary.install(this);
    }
}
