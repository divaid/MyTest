package com.david.mytest.test.pulltorefreshtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.david.mytest.R;
import com.david.mytest.test.pulltorefreshtest.refresh.PullToRefreshBase;
import com.david.mytest.test.pulltorefreshtest.refresh.PullToRefreshWebView;

/**
 * Created by weixi on 2017/6/29.
 */

public class PullRefreshTestActivity extends Activity {
    private View mTest;
    private PullToRefreshWebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pull_refresh);
        mWebView = (PullToRefreshWebView) findViewById(R.id.pull);
        mWebView.setOnPullRefreshListener(new PullToRefreshBase.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.onRefreshComplete();
                    }
                }, 1000);
            }
        });
        mTest = findViewById(R.id.test);// TODO: 2017/7/2 如果网页是自动加载下一页 ？ 下拉之后往回拉会拉出refreshfooter
        mWebView.getRefreshableView().loadUrl("http://www.imooc.com/search/course?words=hybrid");

//        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(PullRefreshTestActivity.this, "click", Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
