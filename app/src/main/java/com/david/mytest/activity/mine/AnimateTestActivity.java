package com.david.mytest.activity.mine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.david.mytest.R;

/**
 * Created by weixi on 2017/8/1.
 */

public class AnimateTestActivity extends Activity implements View.OnClickListener {
    private View mFirstView, mSecondView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_test);
        mFirstView = findViewById(R.id.first_container);
        mSecondView = findViewById(R.id.second_container);

        findViewById(R.id.tv_first).setOnClickListener(this);
        findViewById(R.id.tv_first_retreat).setOnClickListener(this);
        findViewById(R.id.tv_second).setOnClickListener(this);
        findViewById(R.id.tv_second_retreat).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_first:
                ObjectAnimator animator = ObjectAnimator.ofFloat(mFirstView, "scaleX", 1f, 2f);
                animator.setDuration(1000);
                animator.start();
                break;
            case R.id.tv_first_retreat:
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(mFirstView, "scaleX", 2f, 1f);
                animator1.setDuration(1000);
                animator1.start();
                break;
            case R.id.tv_second:
                //沿x轴放大
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mSecondView, "scaleX", 1f, 2f);
                // 沿y轴放大
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mSecondView, "scaleY", 1f, 2f, 1f);
                //移动
//                ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(mSecondView, "translationX", 0f, 200f, 0f);
                ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(mSecondView, "translationX", 0f, -200f);
                //透明动画
                ObjectAnimator alpha = ObjectAnimator.ofFloat(mSecondView, "alpha", 1f, 0f, 1f);
                AnimatorSet set = new AnimatorSet();
                //同时沿X,Y轴放大，且改变透明度，然后移动
                set.play(scaleXAnimator)
//                        .with(scaleYAnimator)
                        .with(translationXAnimator);
//                .before(translationXAnimator);
                //都设置3s，也可以为每个单独设置
                set.setDuration(3000);
                set.start();
                break;
            case R.id.tv_second_retreat:
                //沿x轴放大
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(mSecondView, "scaleX", 2f, 1f);
                //移动
//                ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(mSecondView, "translationX", 0f, 200f, 0f);
                ObjectAnimator translationX = ObjectAnimator.ofFloat(mSecondView, "translationX", -200f, 0f);
                //透明动画
//                ObjectAnimator alpha = ObjectAnimator.ofFloat(mSecondView, "alpha", 1f, 0f, 1f);
                AnimatorSet set1 = new AnimatorSet();
                //同时沿X,Y轴放大，且改变透明度，然后移动
                set1.play(scaleX)
//                        .with(scaleYAnimator)
                        .with(translationX);
//                .before(translationXAnimator);
                //都设置3s，也可以为每个单独设置
                set1.setDuration(3000);
                set1.start();
                break;
        }
    }
}