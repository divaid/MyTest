package com.david.mytest.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.david.mytest.R;

/**
 * 也可设置上面与下面图片
 * Created by wb-cuiweixing on 2016/7/9.
 */

public class IconLabelTetView extends TextView {
    private Drawable mDrawableLeft;
    private Drawable mDrawableRight;

    public IconLabelTetView(Context context) {
        super(context);
    }

    public IconLabelTetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconLabelTetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.iconText);

        mDrawableLeft = typedArray.getDrawable(R.styleable.iconText_left);
        mDrawableRight = typedArray.getDrawable(R.styleable.iconText_right);

        if (mDrawableLeft != null) {
            mDrawableLeft.setBounds(0, 0, typedArray.getDimensionPixelOffset(R.styleable
                    .iconText_leftWidth, dip2px(context, 30)), typedArray.getDimensionPixelOffset(R.styleable
                    .iconText_leftHeight, dip2px(context, 30)));
        }
        if (mDrawableRight != null) {
            mDrawableRight.setBounds(0, 0, typedArray.getDimensionPixelOffset(R.styleable
                    .iconText_rightWidth, dip2px(context, 30)), typedArray.getDimensionPixelOffset(R.styleable
                    .iconText_rightHeight, dip2px(context, 30)));
        }
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.setCompoundDrawables(mDrawableLeft,null, mDrawableRight, null);
        super.onLayout(changed, left, top, right, bottom);
    }

    private static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
