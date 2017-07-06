package com.david.mytest.test.pulltorefreshtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshScrollView;

/**
 * Created by david on 2017/7/5.
 */

public class PullScrollActivity extends Activity{
    private PullToRefreshScrollView mRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_scroll);
        mRefreshView = (PullToRefreshScrollView) findViewById(R.id.pull_scrollview);
        mRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                setTask();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
setTask();
            }
        });
    }

    private void setTask(){
        mRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshView.onRefreshComplete();
            }
        }, 1000);
    }
}
