package com.david.mytest.ui.pulltorefresh.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by David on 2016/5/22.
 */
public abstract class AbstractDrawStrategy implements IDrawStrategy {
    private boolean mHasSetAntiAlias;
    protected IHostView mHostView;
    protected final Paint PAINT = new Paint(Paint.ANTI_ALIAS_FLAG
        | Paint.FILTER_BITMAP_FLAG);
    private DisplayMetrics DISPLAY_METRICES;

    protected AbstractDrawStrategy(Context context, IHostView hostView) {
        mHostView = hostView;
        if(null != mHostView) {
            mHostView.hold(this);
            DISPLAY_METRICES = context.getResources().getDisplayMetrics();
            mHostView.invalidate();
        }
    }

    @Override
    public boolean isNeedUpdate() {
        return null != mHostView && mHostView.isNeedUpdate();
    }

    @Override
    public void hold(IHostView hostView) {
        mHostView = hostView;
        if(null != hostView) {
            hostView.hold(this);
            hostView.invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(!mHasSetAntiAlias) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint
                .ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
            mHasSetAntiAlias = true;
        }
    }

    @Override
    public void setParams(Object... params) {}

    public interface IHostView {
        public void hold(IDrawStrategy strategy);
        public boolean isNeedUpdate();
        public void invalidate();
    }

    protected float dp2px(float dp) {
        if(null != DISPLAY_METRICES) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, DISPLAY_METRICES);
        }
        return .0f;
    }

    public enum State {
        STATE_0, STATE_1, STATE_2, STATE_3, STATE_4;
    }
}