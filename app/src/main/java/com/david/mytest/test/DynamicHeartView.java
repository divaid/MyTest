package com.david.mytest.test;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by weixi on 2017/6/10.
 */

public class DynamicHeartView extends View {

    private static final String TAG = "DynamicHeartView";
    private static final int PATH_WIDTH = 2;
    // 起始点
    private static final int[] START_POINT = new int[] {
            300, 270
    };
    // 爱心下端点
    private static final int[] BOTTOM_POINT = new int[] {
            450, 400
    };
    // 左侧控制点
    private static final int[] LEFT_CONTROL_POINT = new int[] {
            375, 250
    };
//    // 右侧控制点
//    private static final int[] RIGHT_CONTROL_POINT = new int[] {
//            150, 200
//    };

    private PathMeasure mPathMeasure;
    private Paint mPaint;
    private Path mPath;
    private float[] mCurrentPosition = new float[2];
    private ValueAnimator valueAnimator;

    public DynamicHeartView(Context context) {
        super(context);
        init();
    }
    public DynamicHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicHeartView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }//只重写一个构造方法时初始化属性可能会报错（因为调用未重写的构造器时属性未初始化）

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setStyle(Paint.Style.STROKE);//画圆圈
//        mPaint.setStrokeWidth(PATH_WIDTH);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mPath.moveTo(START_POINT[0], START_POINT[1]);
//        mPath.quadTo(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], BOTTOM_POINT[0],
//                BOTTOM_POINT[1]);//添加两个控制点会形成一个圆心效果
        mPath.quadTo(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], BOTTOM_POINT[0], BOTTOM_POINT[1]);

        mPathMeasure = new PathMeasure(mPath, false);
        mCurrentPosition = new float[2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.WHITE);
//        canvas.drawPath(mPath, mPaint);//绘制路径，放开即可

//        canvas.drawCircle(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], 5, mPaint);
        canvas.drawCircle(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], 5, mPaint);

        // 绘制对应目标
        canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 10, mPaint);
    }

    // 开启路径动画
    public void startPathAnim(long duration) {
        // 0 － getLength()
//        ValueAnimator valueAnimator =ValueAnimator.ofFloat(0f, mPathMeasure.getLength());
        valueAnimator = ValueAnimator.ofObject(new BizierEvaluator2(
                new Point(LEFT_CONTROL_POINT[0],LEFT_CONTROL_POINT[1])),
                new Point(START_POINT[0],START_POINT[1]),
                new Point(BOTTOM_POINT[0],BOTTOM_POINT[1]));
        Log.i(TAG, "measure length = " + mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addListener(mAnimationListener);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (Float) animation.getAnimatedValue();
//                // 获取当前点坐标封装到mCurrentPosition
//                float [] positon = new float[2];
//                mPathMeasure.getPosTan(value, mCurrentPosition, positon);
//                postInvalidate();

                Point point = (Point) animation.getAnimatedValue();
                mCurrentPosition[0] = point.x;
                mCurrentPosition[1] =point.y;
                invalidate();
                Log.e("animate","x:   "+point.x+"     y: "+point.y);
            }
        });
        valueAnimator.start();

    }

    public void cancelAnimate(){//动画取消，一般在连续点击添加购物车时调用
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    class BizierEvaluator2 implements TypeEvaluator<Point> {

        private Point controllPoint;

        public BizierEvaluator2(Point controllPoint) {
            this.controllPoint = controllPoint;
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {
            int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
            int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
            return new Point(x, y);
        }
    }
    class Test implements Interpolator {
        public Test() {
        }

        @Override
        public float getInterpolation(float input) {
            return input*input*input-1.5f*input*input+1.5f*input;
        }
    }

    private Animator.AnimatorListener mAnimationListener;

    public void setmAnimationListener(Animator.AnimatorListener mAnimationListener) {
        this.mAnimationListener = mAnimationListener;
    }
}


