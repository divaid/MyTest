package com.david.mytest.application;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by david on 2016/7/1.
 */
public class BaseApplication extends Application {
    private static Context sContext;

    public static RefWatcher mWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        mWatcher = LeakCanary.install(this);

        
    }

    /**
     * 对外提供Application的Context
     * @return
     */
    public static Context getApplication(){
        return sContext;
    }
}
