package com.david.mytest.test.pulltorefreshtest.refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 建议不要设置背景色，否则可能影响体验
 * Created by wb-cuiweixing on 2017/6/27.
 */

public class LoadingLayout extends FrameLayout implements ILoadingLayout {

    private final TextView title;

    public LoadingLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics())));
        setBackgroundColor(Color.BLUE);

        title = new TextView(getContext());
        title.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        title.setGravity(Gravity.CENTER);
        title.setTextSize(18);
        addView(title);
    }

    @Override
    public void onRefreshing() {
        title.setText("isrefreshing");
    }

    @Override
    public void onRefreshCompleted() {
        title.setText("refresh complete");
    }

    /**
     * @param progress 0-1 represent the progress that pulled
     */
    public void onPull(float progress) {
        title.setText("progress:" + progress);
        Log.e("david", "current progress is " + progress);
    }
}
