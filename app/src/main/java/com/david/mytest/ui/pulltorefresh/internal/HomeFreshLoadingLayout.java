package com.david.mytest.ui.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;
import com.david.mytest.ui.pulltorefresh.anim.AbstractDrawStrategy;
import com.david.mytest.ui.pulltorefresh.anim.SimpleHeaderDrawStrategy;
import com.david.mytest.ui.pulltorefresh.progress.Utils;

@SuppressLint("ViewConstructor")
public class HomeFreshLoadingLayout extends LoadingLayout {

    private AbstractDrawStrategy strategy;

    public HomeFreshLoadingLayout(Context context, final PullToRefreshBase.Mode mode, final PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mInnerLayout.setBackgroundResource(R.color.white);
        if (null == strategy) {
            strategy = new SimpleHeaderDrawStrategy.Builder(context, mAnimatedView)
                    .setBackgroundWidth(Utils.dp2px(context.getResources(), 120.f))
                    .setBackgroundHeight(Utils.dp2px(context.getResources(), 24.f))
                    .setMotorBikeWidth(Utils.dp2px(context.getResources(), 50.f))
                    .setMotorBikeHeight(Utils.dp2px(context.getResources(), 48.f))
                    .build();
        }

        switch (mMode) {
            case PULL_FROM_END:
                mIndicatorPanel.setVisibility(GONE);
                mTipProgress.setVisibility(GONE);
                break;
            case PULL_FROM_START:
                //todo 添加摩托动画
                mTitle.setVisibility(View.GONE);
                mIndicatorPanel.setVisibility(View.VISIBLE);
                mIndicatorImage.setVisibility(View.GONE);
                mIconImage.setVisibility(View.GONE);
                mAnimatedView.setVisibility(VISIBLE);
            default:
                break;
        }
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {
            final int dHeight = imageDrawable.getIntrinsicHeight();
            final int dWidth = imageDrawable.getIntrinsicWidth();

            /**
             * We need to set the width/height of the ImageView so that it is
             * square with each side the size of the largest drawable dimension.
             * This is so that it doesn't clip when rotated.
             */
            ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
            lp.width = lp.height = Math.max(dHeight, dWidth);
            mHeaderImage.requestLayout();

            /**
             * We now rotate the Drawable so that is at the correct rotation,
             * and is centered.
             */
            mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height - dHeight) / 2f);
            matrix.postRotate(getDrawableRotationAngle(), lp.width / 2f, lp.height / 2f);
            mHeaderImage.setImageMatrix(matrix);
        }
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        strategy.update(Math.min(scaleOfLayout, 1.f), SimpleHeaderDrawStrategy.PathType.FIRST);
    }

    @Override
    protected void pullToRefreshImpl() {
//		HPLog.e("pullToRefreshImpl");
//		if (mRotateAnimation == mIndicatorImage.getAnimation()) {
//			mIndicatorImage.startAnimation(mResetRotateAnimation);
//		}

        if (mMode == PullToRefreshBase.Mode.PULL_FROM_END) {
            mTipProgress.setVisibility(GONE);
        } else {
            mTitle.setVisibility(GONE);
        }
        mAnimatedView.setVisibility(VISIBLE);
    }

    @Override
    protected void refreshingImpl() {
        if (mMode == PullToRefreshBase.Mode.PULL_FROM_END) {
            mTipProgress.setVisibility(VISIBLE);
        } else {
            mTitle.setVisibility(GONE);
        }
        strategy.auto(1.f, SimpleHeaderDrawStrategy.PathType.FIRST);
    }

    @Override
    protected void releaseToRefreshImpl() {
        strategy.abort(SimpleHeaderDrawStrategy.PathType.SECOND);
    }

    @Override
    protected void resetImpl() {
        mAnimatedView.removeAllViews();
        mAnimatedView.setVisibility(VISIBLE);
        mIndicatorImage.setVisibility(GONE);
        mIconImage.setVisibility(View.GONE);
        mTipProgress.setVisibility(GONE);
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
//        switch (mMode) {
//            case PULL_FROM_END:
//                if (mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) {
//                    angle = 90f;
//                } else {
//                    angle = 180f;
//                }
//                break;
//
//            case PULL_FROM_START:
//                if (mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) {
//                    angle = 270f;
//                }
//                break;
//
//            default:
//                break;
//        }
//
        return angle;
    }
}
