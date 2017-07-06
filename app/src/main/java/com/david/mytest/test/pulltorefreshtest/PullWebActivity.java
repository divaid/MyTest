package com.david.mytest.test.pulltorefreshtest;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshWebView;

/**
 * Created by David on 2016/7/5.
 */

public class PullWebActivity extends Activity {
    private PullToRefreshWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_web);

        mWebView = (PullToRefreshWebView) findViewById(R.id.pull);
        mWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WebView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
                setTask();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {
                setTask();
            }
        });
//        mTest = findViewById(R.id.test);// TODO: 2017/7/2 如果网页是自动加载下一页 ？ 下拉之后往回拉会拉出refreshfooter
        mWebView.getRefreshableView().loadUrl("file:///android_asset/test.html");
    }

    private void setTask() {
        mWebView.getRefreshableView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.onRefreshComplete();
            }
        }, 1000);
    }
}
