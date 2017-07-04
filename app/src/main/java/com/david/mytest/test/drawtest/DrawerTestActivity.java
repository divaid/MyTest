package com.david.mytest.test.drawtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.david.mytest.R;
import com.david.mytest.utils.RxBus;

/**
 * Created by weixi on 2017/6/15.
 */

public class DrawerTestActivity extends Activity implements View.OnClickListener {
    private DrawerLayout mDrawer;
    private View mLeftDrawer,mRightDrawer;

    private RxBusTest mMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);
        initView();
        mMessage =new RxBusTest("测试消息");
        findViewById(R.id.tv_rxbus_test_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RxBus.getDefault().post(new RxBusTest());从主线程发消息

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RxBus.getDefault().post(mMessage);//从子线程发消息

                    }
                }).start();
            }
        });
    }

    private void initView() {
        findViewById(R.id.tv_left_drawer).setOnClickListener(this);
        findViewById(R.id.tv_right_drawer).setOnClickListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.test_drawer);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //slideoffset拖动进度0-1
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override//通过id可判断打开的是哪边的抽屉
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //拖动状态，静止，在拖动等
            }
        });
        //可自己获取左右抽屉的view对象设置相应的事件
        mLeftDrawer = findViewById(R.id.my_left_drawer);
        mRightDrawer = findViewById(R.id.my_right_drawer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_drawer://openDrawer(左边抽屉View对象亦可（看源代码可知）);
                mDrawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.tv_right_drawer:
                mDrawer.openDrawer(Gravity.RIGHT);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessage = null;
    }
}
