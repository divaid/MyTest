package com.david.mytest.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.david.mytest.R;


/**
 * Created by weixi on 2017/6/30.
 */

public class ActivityTestMyWidget extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywidget);

        findViewById(R.id.dragrecycler).setOnClickListener(this);
        findViewById(R.id.my_banner).setOnClickListener(this);
        findViewById(R.id.pulltorefresh).setOnClickListener(this);
        findViewById(R.id.autoline).setOnClickListener(this);
        findViewById(R.id.wheelview).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_banner:
                startActivity(new Intent(this, com.david.mytest.test.banner.BannerTestActivity.class));
                break;
            case R.id.dragrecycler:
                startActivity(new Intent(this, com.david.mytest.test.dragrecyclerview.activity.DragRecyclerTestActivity.class));
                break;
            case R.id.pulltorefresh:
                startActivity(new Intent(this, com.david.mytest.test.pulltorefreshtest.PullRefreshTestActivity.class));
                break;
            case R.id.autoline:
                startActivity(new Intent(this, com.david.mytest.test.day_6_27_autolinetest.AutoLineTestActivity.class));
                break;
            case R.id.wheelview:
                startActivity(new Intent(this, com.david.mytest.test.wheeltest.WheelTestActivity.class));

                break;
        }
    }
}
