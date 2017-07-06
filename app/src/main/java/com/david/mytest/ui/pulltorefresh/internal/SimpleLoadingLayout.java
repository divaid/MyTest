package com.david.mytest.ui.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;

/**
 * Created by weixi on 2017/6/6.
 */

public class SimpleLoadingLayout extends LoadingLayout {
    static final int FLIP_ANIMATION_DURATION = 600;

    private final Animation mRotateAnimation, mResetRotateAnimation;

    public SimpleLoadingLayout(Context context, final PullToRefreshBase.Mode mode, final PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);

        final int rotateAngle = mode == PullToRefreshBase.Mode.PULL_FROM_START ? 360 : -360;

        mRotateAnimation = new RotateAnimation(0, rotateAngle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(rotateAngle, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
        mResetRotateAnimation.setRepeatCount(Animation.INFINITE);
        mResetRotateAnimation.setFillAfter(true);

        switch (mMode) {
            case PULL_FROM_END:
                mIndicatorPanel.setVisibility(GONE);
                mTipProgress.setVisibility(GONE);
                break;
            case PULL_FROM_START:
            default:
                mIndicatorPanel.setVisibility(VISIBLE);
                mIndicatorImage.setFinishedStrokeWidth(com.david.mytest.ui.pulltorefresh.progress.Utils.dp2px(getResources(), 1.5f));
                mIndicatorImage.setUnfinishedStrokeWidth(com.david.mytest.ui.pulltorefresh.progress.Utils.dp2px(getResources(), 1.5f));
                mIndicatorImage.setText("");
                mIndicatorImage.setStartingDegree(270);
                break;
        }
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {
            final int dHeight = imageDrawable.getIntrinsicHeight();
            final int dWidth = imageDrawable.getIntrinsicWidth();

            ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
            lp.width = lp.height = Math.max(dHeight, dWidth);
            mHeaderImage.requestLayout();

            mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height - dHeight) / 2f);
            matrix.postRotate(getDrawableRotationAngle(), lp.width / 2f, lp.height / 2f);
            mHeaderImage.setImageMatrix(matrix);
        }
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        if (scaleOfLayout >= 1) scaleOfLayout = 1.0f;
        mIndicatorImage.setProgress((int) (scaleOfLayout * 100));
    }

    @Override
    protected void pullToRefreshImpl() {
        if( mMode == PullToRefreshBase.Mode.PULL_FROM_END) {
            mTipProgress.setVisibility( GONE );
            mTitle.setVisibility(VISIBLE);
        } else {
            mTitle.setVisibility( GONE );
        }
        mIconImage.setVisibility(View.VISIBLE);
        mIndicatorImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void refreshingImpl() {
//		HPLog.e("refreshingImpl");
        if( mMode == PullToRefreshBase.Mode.PULL_FROM_END) {
            mTipProgress.setVisibility( VISIBLE );
            mTitle.setVisibility(VISIBLE);
        }
        else {
            mTitle.setVisibility( GONE );
        }
        mIndicatorImage.setProgress(85);
        mIndicatorImage.setVisibility(View.VISIBLE);
        mIndicatorImage.startAnimation(mRotateAnimation);
        mIconImage.setVisibility(VISIBLE);
//		mIconImage.startAnimation(mRotateAnimation);
    }

    @Override
    protected void releaseToRefreshImpl() {
//		HPLog.e("releaseToRefreshImpl");
//		mIndicatorImage.setProgress(120);
//		mIndicatorImage.startAnimation(mRotateAnimation);

//		mIconImage.startAnimation(mRotateAnimation);
    }

    @Override
    protected void resetImpl() {
//		HPLog.e("resetImpl");
//		mHeaderImage.clearAnimation();
//		mHeaderProgress.setVisibility(View.GONE);
//		mHeaderImage.setVisibility(View.VISIBLE);
        mIndicatorImage.clearAnimation();
        mIndicatorImage.setProgress(0);
        mIndicatorImage.setVisibility(View.VISIBLE);
        mIconImage.clearAnimation();
        mIconImage.setVisibility(View.GONE);
        mTipProgress.setVisibility( GONE );
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.shape_refresh_ring;
    }

    @Override
    public void setDrawable(Drawable drawable) {

    }

    private float getDrawableRotationAngle() {
        float angle = 0f;
        switch (mMode) {
            case PULL_FROM_END:
                if (mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) {
                    angle = 90f;
                } else {
                    angle = 180f;
                }
                break;

            case PULL_FROM_START:
                if (mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) {
                    angle = 270f;
                }
                break;

            default:
                break;
        }

        return angle;
    }
}
