package com.david.mytest.test.dragrecyclerview.helper;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by david on 2016/4/14.
 */
public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    private long mLatestClickTime;
    private float mInitionX;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//        if (e.getAction() == MotionEvent.ACTION_DOWN) {//滑动删除
//            mLatestClickTime = System.currentTimeMillis();
//            mInitionX = e.getX();
//        } else if (e.getAction() == MotionEvent.ACTION_UP) {
//            if (System.currentTimeMillis() - mLatestClickTime < 300 && e.getX() - mInitionX > 30) {
//                int position = recyclerView.getChildAdapterPosition(recyclerView.findChildViewUnder(e.getX(), e.getY()));
//                if (position >= 0) {
//                    onSwiped(position);
//                    return false;
//                }
//            }
//        }
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onLongClick(vh);
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void onLongClick(RecyclerView.ViewHolder vh) {
    }

    public void onItemClick(RecyclerView.ViewHolder vh) {
    }

    public void onSwiped(int position){

    }
}
