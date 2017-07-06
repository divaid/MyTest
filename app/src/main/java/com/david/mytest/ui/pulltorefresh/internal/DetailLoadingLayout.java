package com.david.mytest.ui.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;

/**
 * Created by David on 15/11/2.
 */
public class DetailLoadingLayout extends LoadingLayout {

    private int refreshTopResId = R.drawable.refresh_top_normal;

    private TextView mTipView;

    public DetailLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
//        setBackgroundColor(context.getResources().getColor(R.color.gray_bg_dark));
        mTipView = new TextView(context);
        mTipView.setText("测试文本1");
        mTipView.setGravity(Gravity.CENTER);
        int px = dip2px(context, 25);
        mTipView.setPadding(px, px, px, px);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        addView(mTipView, params);
        ((View) mHeaderImage.getParent()).setVisibility(View.GONE);
//        mHeaderText.setVisibility(View.GONE);
//        mSubHeaderText.setVisibility(View.GONE);

    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.refresh_top_default;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
//        LinearLayout.LayoutParams flLp = (LinearLayout.LayoutParams) mHeaderImage.getLayoutParams();
//        flLp.width = dip2px(getContext(), 24);
//        if (scaleOfLayout < 0.5357f) {
//            flLp.height = dip2px(getContext(), 30);
//            mHeaderImage.setBackgroundResource(R.drawable.refresh_top_default);
//        } else {
//            mHeaderImage.setBackgroundResource(refreshTopResId);
//            if (scaleOfLayout < 1) {
//                flLp.height = dip2px(getContext(), scaleOfLayout * 56);
//            } else {
//                flLp.height = dip2px(getContext(), 56);
//            }
//        }
//        mHeaderImage.requestLayout();
    }

    @Override
    protected void pullToRefreshImpl() {
        refreshTopResId = R.drawable.refresh_top_refresh;
    }

    @Override
    protected void refreshingImpl() {
//        FrameLayout.LayoutParams flLp = (FrameLayout.LayoutParams) mTipView.getLayoutParams();
//        flLp.topMargin = dip2px(getContext(), 0);
//        flLp.height = dip2px(getContext(), 30);
//        mTipView.requestLayout();
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {
//        refreshTopResId = R.drawable.refresh_top_normal;
//        mHeaderImage.setBackgroundResource(R.drawable.refresh_top_default);
//        mHeaderProgress.setVisibility(View.GONE);
//        LinearLayout.LayoutParams flLp= (LinearLayout.LayoutParams) mHeaderImage.getLayoutParams();
//        flLp.topMargin = dip2px(getContext(), 25);
//        mHeaderImage.requestLayout();

        if (mTipView != null) {
            mTipView.requestLayout();
        }

    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void setDrawable(Drawable drawable) {

    }
}
