package com.david.mytest.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * Created by cuiweixing on 2018/7/28.
 */

public class VerticalViewPagerAllowChildScroll extends ViewPager {
    private boolean isChildScrollAble;
    private List<View> views;

    public VerticalViewPagerAllowChildScroll(Context context) {
        super(context);
        init();
    }

    public VerticalViewPagerAllowChildScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setChildScrollAble(boolean childScrollAble) {
        isChildScrollAble = childScrollAble;
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    private float fingerDownPosition;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (views.get(getCurrentItem()) instanceof ScrollView) {//getChild(getCurrentItem)获取的item不正确，不要用
            ScrollView scrollView = (ScrollView) views.get(getCurrentItem());
            switch (ev.getAction()) {
                case ACTION_DOWN:
                    fingerDownPosition = ev.getY(0);
                    break;
                case ACTION_MOVE:
                    if ((ev.getY() - fingerDownPosition) > 0) {
                        if (scrollView.canScrollVertically(-1)) {
                            return false;
                        } else {
                            return true;
                        }
                    } else if (ev.getY() - fingerDownPosition < 0){
                        if (scrollView.canScrollVertically(1)){
                            return false;
                        } else
                            return true;
                    }
            }

        }
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }
}

