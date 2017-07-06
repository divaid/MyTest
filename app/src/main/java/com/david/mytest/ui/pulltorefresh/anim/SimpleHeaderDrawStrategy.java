package com.david.mytest.ui.pulltorefresh.anim;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.david.mytest.R;

/**
 * Created by David on 2016/5/22.
 */
public class SimpleHeaderDrawStrategy extends AbstractDrawStrategy {
    private final PaintProperties PAINT_PROPERTIES = new PaintProperties();

    //for frame animation
    private static final int[] MOTOR_BIKE_RES_IDS = {
        R.drawable.motorbike_frame_0, R.drawable.motorbike_frame_1,
        R.drawable.motorbike_frame_2, R.drawable.motorbike_frame_3,
        R.drawable.motorbike_frame_4, R.drawable.motorbike_frame_5
    };

    private static final int[] BACKGROUND_RES_IDS = {
        R.drawable.background_frame_0, R.drawable.background_frame_1,
        R.drawable.background_frame_2, R.drawable.background_frame_3,
        R.drawable.background_frame_4, R.drawable.background_frame_5
    };

    private final Drawable[] mMotorBike = new Drawable[MOTOR_BIKE_RES_IDS.length];
    private final Drawable[] mBackground = new Drawable[BACKGROUND_RES_IDS.length];

    private float mProgress;

    private float mWidth, mHeight;

    private int mMotorBikeFrameCnt, mBackgroundFrameCnt;

    public enum PathType { FIRST, SECOND }

    private PathType mPathType = PathType.FIRST;

    private static final int DELAY_CONST = 5;
    private int delay;

    public void setPathPype(PathType pathType) {
        mPathType = pathType;
    }

    private final TypeEvaluator<Float> MOTOR_BIKE_EVALUATOR = new TypeEvaluator<Float>() {
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return (1.f - fraction) * startValue + fraction * endValue;
        }
    };

    private boolean mAutoFlag;

    private ValueAnimator mAutoAnimator;

    private SimpleHeaderDrawStrategy(Context context, IHostView hostView) {
        super(context, hostView);
        for(int index = 0, size = MOTOR_BIKE_RES_IDS.length; index < size; ++index) {
            mMotorBike[index] = getDrawable(((View) hostView).getContext(), MOTOR_BIKE_RES_IDS[index]);
        }
        for(int index = 0, size = BACKGROUND_RES_IDS.length; index < size; ++index) {
            mBackground[index] = getDrawable(((View) hostView).getContext(), BACKGROUND_RES_IDS[index]);
        }
    }

    private Drawable getDrawable(Context context, int resID) {
        return ContextCompat.getDrawable(context, resID);
    }

    @Override
    public void setParams(Object... params) {
        if(null != params && params.length > 0 && params[0] instanceof PathType) {
            mPathType = (PathType) params[0];
        }
    }

    @Override
    public void update(float progress, Object... params) {
        mProgress = Math.max(0.f, Math.min(progress, 1.f));
        if(null != params && params.length > 0) {
            mPathType = (PathType) params[0];
        }
        mHostView.invalidate();
    }

    @Override
    public void auto(float progress, Object... params) {
        mProgress = progress;
        if(null == mAutoAnimator) {
            mAutoAnimator = ValueAnimator.ofInt(0, 0);
            mAutoAnimator.setInterpolator(new LinearInterpolator());
            mAutoAnimator.setEvaluator(new IntEvaluator());
            mAutoAnimator.setDuration(1000L);
            mAutoAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAutoAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAutoAnimator.addUpdateListener(new ValueAnimator.
                AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if(0 == delay % DELAY_CONST) {
                        mHostView.invalidate();
                    }
                    delay = (delay + 1) % DELAY_CONST;
                }
            });
            mAutoAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mAutoFlag = true;
                    mPathType = PathType.FIRST;
                    delay = 0;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onAnimationCancel(animation);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mAutoFlag = false;
                    mAutoAnimator = null;
                    mPathType = PathType.SECOND;
                    mHostView.invalidate();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            mAutoAnimator.start();
        }
    }

    @Override
    public void abort(Object... params) {
        if(null != mAutoAnimator && mAutoAnimator.isRunning()) {
            mAutoAnimator.cancel();
            mAutoFlag = false;
            mAutoAnimator = null;
            mHostView.invalidate();
        }
        if(null != params && params.length > 0 && params[0] instanceof PathType) {
            mPathType = (PathType) params[0];
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(0 == mWidth) {
            mWidth = canvas.getWidth();
        }
        if(0 == mHeight) {
            mHeight = canvas.getHeight();
        }
        if(0 != mWidth && 0 != mHeight) {
            // Draw background
            Drawable background = mBackground[mBackgroundFrameCnt];
            float maxHeight = Math.max(PAINT_PROPERTIES.backgroundHeight, PAINT_PROPERTIES.motorBikeHeight);
            float baseLine = .5f * (mHeight + maxHeight), offsetY = -dp2px(10.f);
            background.setBounds(
                (int) (.5f * (mWidth - PAINT_PROPERTIES.backgroundWidth) + .5f),      // left
                (int) (baseLine + offsetY - PAINT_PROPERTIES.backgroundHeight + .5f), // top
                (int) (.5f * (mWidth + PAINT_PROPERTIES.backgroundWidth) + .5f),      // right
                (int) (baseLine + offsetY + .5f)  // bottom
            );
            background.draw(canvas);
            // Draw motorbike
            Drawable motorBike = mMotorBike[mMotorBikeFrameCnt];
            float centerX = 0.f;
            if(PathType.FIRST == mPathType) {
                centerX = MOTOR_BIKE_EVALUATOR.evaluate(mProgress, -.5f * PAINT_PROPERTIES
                    .motorBikeWidth, .5f * mWidth);
            } else if(PathType.SECOND == mPathType) {
                centerX = MOTOR_BIKE_EVALUATOR.evaluate(mProgress, .5f * PAINT_PROPERTIES
                    .motorBikeWidth + mWidth, .5f * mWidth);
            }
            motorBike.setBounds(
                (int) (centerX - .5f * PAINT_PROPERTIES.motorBikeWidth + .5f),    // left
                (int) (baseLine - PAINT_PROPERTIES.motorBikeHeight + .5f),        // top
                (int) (centerX + .5f * PAINT_PROPERTIES.motorBikeWidth + .5f),    // right
                (int) (baseLine + .5f));  // bottom
            motorBike.draw(canvas);
            mMotorBikeFrameCnt = (mMotorBikeFrameCnt + 1) % MOTOR_BIKE_RES_IDS.length;
            mBackgroundFrameCnt = (mBackgroundFrameCnt + 1) % BACKGROUND_RES_IDS.length;
        }
    }

    public static final class Builder {
        private SimpleHeaderDrawStrategy strategy;

        public Builder(Context context, IHostView hostView) {
            strategy = new SimpleHeaderDrawStrategy(context, hostView);
        }

        public Builder setMotorBikeWidth(float motorBikeWidth) {
            strategy.PAINT_PROPERTIES.motorBikeWidth = motorBikeWidth;
            return this;
        }

        public Builder setMotorBikeHeight(float motorBikeHeight) {
            strategy.PAINT_PROPERTIES.motorBikeHeight = motorBikeHeight;
            return this;
        }

        public Builder setBackgroundWidth(float backgroundWidth) {
            strategy.PAINT_PROPERTIES.backgroundWidth = backgroundWidth;
            return this;
        }

        public Builder setBackgroundHeight(float backgroundHeight) {
            strategy.PAINT_PROPERTIES.backgroundHeight = backgroundHeight;
            return this;
        }

        public SimpleHeaderDrawStrategy build() {
            return strategy;
        }
    }



    private static final class PaintProperties {
        // MotorBike
        public float motorBikeWidth, motorBikeHeight;
        // Background
        public float backgroundWidth, backgroundHeight;
    }
}