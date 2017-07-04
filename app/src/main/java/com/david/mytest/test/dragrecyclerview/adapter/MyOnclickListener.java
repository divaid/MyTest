package com.david.mytest.test.dragrecyclerview.adapter;

import android.os.Handler;
import android.view.View;

/**
 * Created by wb-cuiweixing on 2017/6/23.
 */

public class MyOnclickListener implements View.OnClickListener {
    private Handler mHandler;
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            onSingleClick();
        }
    };

    private long mLatestClickTime;
    @Override
    public void onClick(View v) {
        if(System.currentTimeMillis() - mLatestClickTime >300) {
            mLatestClickTime = System.currentTimeMillis();
            mHandler.postDelayed(mTask, 300);
        } else {
            mHandler.removeCallbacks(mTask);
            onDoubleClick(v);
        }
    }

    public void onSingleClick(){

    }

    public void onDoubleClick(View v){

    }
}
