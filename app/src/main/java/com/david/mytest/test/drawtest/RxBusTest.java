package com.david.mytest.test.drawtest;

import android.util.Log;

/**
 * Created by weixi on 2017/6/17.
 */

public class RxBusTest {
    public RxBusTest(String message) {
        this.message = message;
    }

    public String message;

    @Override
    protected void finalize() throws Throwable {
        Log.e("David", "对象销毁了");
        super.finalize();
    }
}
