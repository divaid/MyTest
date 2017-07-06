package com.david.mytest.ui.pulltorefresh.anim;

import android.graphics.Canvas;

/**
 * Created by David on 2016/5/22.
 */
public interface IDrawStrategy {

    public void update(float progress, Object... params);

    public void auto(float progress, Object... params);

    public void hold(AbstractDrawStrategy.IHostView hostView);

    public void abort(Object... params);

    public boolean isNeedUpdate();

    public void onDraw(Canvas canvas);

    public void setParams(Object... params);
}