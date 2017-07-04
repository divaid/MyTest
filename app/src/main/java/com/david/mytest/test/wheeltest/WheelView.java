package com.david.mytest.test.wheeltest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixi on 2017/6/30.
 */

public class WheelView extends ScrollView {
    public static final String TAG = "WheelView";

    public static class OnWheelListener {
        public void onSelected(int selectedIndex, String item) {
        }
    }
    private LinearLayout mViewContainer;

    private OnWheelListener mWheelListener;

    private List<String> mData;

    private int mDisplayCount; // 每页显示的数量
    private int mSelectedIndex = 1;
    private int mOffset = 1; // 偏移量（需要在最前面和最后面补全）
    private int mLatestScrollY;
    private int mItemHeight = 0;
    private Runnable mScrollTask;
    private int mTaskDelay = 25;

    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private List<String> getData() {
        return mData;
    }

    public void setData(List<String> list) {
        if (null == mData) {
            mData = new ArrayList<String>();
        }
        mData.clear();
        mData.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < mOffset; i++) {
            mData.add(0, "");
            mData.add("");
        }

        initData();
    }

    public int getOffset() {
        return mOffset;
    }

    /**
     * 设置第一条向后偏移几行，用于设置显示的行数
     * @param offset 向后偏移的行数
     */
    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    private void init() {
//        this.setOrientation(VERTICAL);
        this.setVerticalScrollBarEnabled(false);

        mViewContainer = new LinearLayout(getContext());
        mViewContainer.setOrientation(LinearLayout.VERTICAL);
        this.addView(mViewContainer);

        mScrollTask = new Runnable() {

            public void run() {
                int newY = getScrollY();
                if (mLatestScrollY - newY == 0) { // stopped
                    final int remainder = mLatestScrollY % mItemHeight;
                    final int divided = mLatestScrollY / mItemHeight;
                    if (remainder == 0) {
                        mSelectedIndex = divided + mOffset;

                        onSelectedCallBack();
                    } else {
                        if (remainder > mItemHeight / 2) {
                            WheelView.this.smoothScrollTo(0, mLatestScrollY - remainder + mItemHeight);
                            mSelectedIndex = divided + mOffset + 1;
                            onSelectedCallBack();
                        } else {
                            WheelView.this.smoothScrollTo(0, mLatestScrollY - remainder);
                            mSelectedIndex = divided + mOffset;
                            onSelectedCallBack();
                        }
                    }
                } else {
                    //松开手指后持续更新最后滑动到的位置,当mLatestScrollY - newY == 0时说明不在滑动
                    mLatestScrollY = getScrollY();
                    WheelView.this.postDelayed(mScrollTask, mTaskDelay);
                }
            }
        };
    }

    public void startScrollerTask() {
        mLatestScrollY = getScrollY();
        this.postDelayed(mScrollTask, mTaskDelay);
    }

    private void initData() {
        mDisplayCount = mOffset * 2 + 1;

        for (String item : mData) {
            mViewContainer.addView(createView(item));
        }

        refreshItemView(0);
    }

    /**
     * create TextView
     */
    private TextView createView(String item) {
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(10);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == mItemHeight) {
            mItemHeight = getViewMeasuredHeight(tv);
            Log.d(TAG, "mItemHeight: " + mItemHeight);
            mViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight * mDisplayCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, mItemHeight * mDisplayCount));
        }
        return tv;
    }


    @Override//l表示滑动后的x值，oldl表示滑动前的x位置,t表示滑动后的y值，oldt表示滑动前的y位置。
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        refreshItemView(t);
    }

    //根据滑动距离计算中间textview，不断更新UI
    private int mCenterPosition;
    private void refreshItemView(int y) {
        int position;
        int remainder = y % mItemHeight;
        int divided = y / mItemHeight;

        if (remainder == 0) {
            position = divided + mOffset;
        } else {
            if (remainder > mItemHeight / 2) {
                position = divided + mOffset + 1;
            } else {
                position = divided + mOffset;
            }
        }

        if (mCenterPosition == position){
            return;
        } else {
            mCenterPosition = position;
        }

        int childSize = mData.size();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) mViewContainer.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(Color.parseColor("#0288ce"));
//                itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);//设置选中样式
            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));
//                itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
        }
    }

    /**
     * 获取选中区域的边界
     */
    private int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = mItemHeight * mOffset;
            selectedAreaBorder[1] = mItemHeight * (mOffset + 1);
        }
        return selectedAreaBorder;
    }

    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    private Paint paint;
    private int viewWidth;

    @Override
    public void setBackground(Drawable background) {
        if (viewWidth == 0) {
            viewWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            paint.setStrokeWidth(dip2px(1f));
        }

        background = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                canvas.drawLine(viewWidth * 1.0F / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth * 1.0F / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
        super.setBackground(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackground(null);
    }

    /**
     * 选中回调
     */
    private void onSelectedCallBack() {
        if (null != mWheelListener) {
            mWheelListener.onSelected(mSelectedIndex, mData.get(mSelectedIndex));
        }

    }

    public void setSelection(int position) {
        final int p = position;
        mSelectedIndex = p + mOffset;
        this.post(new Runnable() {//post任务是要布局渲染完成后滑动
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * mItemHeight);
            }
        });

    }

    public String getSeletedItem() {
        return mData.get(mSelectedIndex);
    }

    public int getSeletedIndex() {
        return mSelectedIndex - mOffset;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);//通过velocityY/n 设置不滑动太快
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    public void setOnWheelListener(OnWheelListener mWheelListener) {
        this.mWheelListener = mWheelListener;
    }

    private int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }
}
