package com.david.mytest.test.pulltorefreshtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.david.mytest.R;

/**
 * Created by David on 2017/6/29.
 */

public class PullRefreshTestActivity extends Activity implements View.OnClickListener{
//    private View mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pull_refresh);
        findViewById(R.id.btn_pull_refresh_grid).setOnClickListener(this);
        findViewById(R.id.btn_pull_refresh_list).setOnClickListener(this);
        findViewById(R.id.btn_pull_refresh_scroll).setOnClickListener(this);
        findViewById(R.id.btn_pull_refresh_web).setOnClickListener(this);
        findViewById(R.id.btn_pull_refresh_recycler).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pull_refresh_grid:
                nav(PullGridActivity.class);
                break;
            case R.id.btn_pull_refresh_list:
                nav(PullListActivity.class);
                break;
            case R.id.btn_pull_refresh_scroll:
                nav(PullScrollActivity.class);
                break;
            case R.id.btn_pull_refresh_web:
                nav(PullWebActivity.class);
                break;
            case R.id.btn_pull_refresh_recycler:
                nav(PullRecyclerActivity.class);

                break;
        }
    }

    private void nav(Class className){
        startActivity(new Intent(this, className));
    }
}
