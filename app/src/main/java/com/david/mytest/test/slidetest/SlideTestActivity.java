package com.david.mytest.test.slidetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.david.mytest.R;
import com.david.mytest.test.drawtest.DrawerTestActivity;
import com.david.mytest.test.drawtest.RxBusTest;
import com.david.mytest.test.drawtest.RxBusTest1;
import com.david.mytest.utils.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by weixi on 2017/6/15.
 */

public class SlideTestActivity extends Activity {
    private TextView mText, mText1;
    private Disposable disposable;
    private Disposable disposable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_test);
        mText= (TextView) findViewById(R.id.tv_rxbus_test);
        mText1= (TextView) findViewById(R.id.tv_rxbus_test1);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SlideTestActivity.this, DrawerTestActivity.class));
            }
        });
        disposable = RxBus.getDefault().toObservable(RxBusTest.class)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RxBusTest>() {
            @Override
            public void accept(RxBusTest rxBusTest) throws Exception {
                //接收到消息之后未销毁，直到内存不够用了才一次性销毁了
                mText.setText(mText.getText().toString()+rxBusTest.message);
                disposable.dispose();
            }
        });


        disposable1 = RxBus.getDefault().toObservable(RxBusTest1.class).subscribe(new Consumer<RxBusTest1>() {
            @Override
            public void accept(RxBusTest1 rxBusTest1) throws Exception {
                mText1.setText("接收到了消息");
            }
        });
        mText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new RxBusTest1());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不影响其他消息的传递（不同ClassType的消息）,在那个activity中注册的就在那个activity销毁
        disposable.dispose();
        disposable1.dispose();
    }
}

