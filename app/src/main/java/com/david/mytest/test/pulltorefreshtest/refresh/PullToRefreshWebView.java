package com.david.mytest.test.pulltorefreshtest.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by weixi on 2017/6/29.
 */

public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    public PullToRefreshWebView(Context context) {
        super(context);
    }

    public PullToRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    public boolean isReadyForPullEnd() {
        float exactContentHeight= (float) Math.floor(mRefreshableView.getContentHeight()*mRefreshableView.getScaleY());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }

    @Override
    public WebView createRefreshableView(Context context, AttributeSet attrs) {
        WebView view = new WebView(context, attrs);
        view.setWebChromeClient(new WebChromeClient());
        view.setWebViewClient(new WebViewClient());
//        view.setId(R.id.webview);//设置id，须在value中定义
        return view;
    }
}