package com.david.mytest.ui.pulltorefresh.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by David on 2016/5/15.
 */
public class AnimatedView extends FrameLayout implements AbstractDrawStrategy.IHostView {
    protected IDrawStrategy mDrawStrategy;

    public AnimatedView(Context context) {
        this(context, null);
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    public void hold(IDrawStrategy strategy) {
        mDrawStrategy = strategy;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public boolean isNeedUpdate() {
        return isShown();
    }

    @Override
    public void onDraw(Canvas c) {
        if(null != mDrawStrategy) {
            mDrawStrategy.onDraw(c);
        }
    }
}