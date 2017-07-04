package com.david.mytest.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.david.mytest.R;

public class TestActivity extends Activity implements View.OnClickListener {

    private FllowerAnimation mFllowerView;
    private DynamicHeartView mHeartView;
    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_test).setOnClickListener(this);
        mFllowerView = (FllowerAnimation) findViewById(R.id.flower);
//        mHeartView = (DynamicHeartView) findViewById(R.id.heart);

        rootView = (ViewGroup) this.getWindow().getDecorView();

        mHeartView = new DynamicHeartView(this);
        mHeartView.setTag("animate");
        mHeartView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_test){
            animateView();//执行自定义View

//            animateImage();//imageView执行动画
        }
    }

    private View animateView;

    private void animateImage() {
        animateView = new View(this);//可以是ImageView，这里只实现一个小方块
        animateView.setLayoutParams(new ViewGroup.LayoutParams(30,30));
        animateView.setBackgroundColor(Color.RED);
        rootView.addView(animateView);

        int []startPosition={0,0};
        int[] des = {500,500};//开始位置与结束位置动态指定，控制点根据开始与结束位置指定

        //获取控制点位置,此时x在两点中间，y在其实点小100处（有上扔效果）
        int controlPointX = (startPosition[0] + des[1]) / 2 - 100;
        int controlPointY = startPosition[1] - 200;

        //设置二元的贝塞尔曲线路径
        Path path = new Path();
        path.moveTo(startPosition[0],startPosition[1]);
        path.quadTo(controlPointX,controlPointY,des[0],des[1]);

        //初始化路径测量对象，第二个参数是否闭合（闭合时执行一个环的动画,此时只执行一个曲线）
        final PathMeasure measure = new PathMeasure(path, false);


        //实现属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f,measure.getLength());//设置路径总长
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                float[] point = new float[2];
                measure.getPosTan(value,point,null);
                animateView.setX(point[0]);
                animateView.setY(point[1]);
            }
        });

        /**
         * 动画结束，移除掉小圆圈
         */
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup rootView = (ViewGroup) TestActivity.this.getWindow().getDecorView();
                rootView.removeView(animateView);
            }
        });
    }
    //rootview: ViewGroup rootView = (ViewGroup) TestActivity.this.getWindow().getDecorView();
    private void animateView() {
        //            mFllowerView.startAnimation();
        mHeartView.cancelAnimate();//只添加一个View在连续点击是比较快点
        rootView.findViewWithTag("animate");
        rootView.addView(mHeartView);//添加要执行的动画到

        Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.removeView(mHeartView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                rootView.removeView(mHeartView);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        mHeartView.setmAnimationListener(mAnimationListener);
        mHeartView.startPathAnim(100);

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
}
