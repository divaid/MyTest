package com.david.mytest.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.david.mytest.R;


/**
 * Created by wb-cuiweixing on 2016/12/26.
 */
public class LabelEditText extends EditText {
    public static final int TEXT_ALIGN_CENTER = 0x00000000;
    public static final int TEXT_ALIGN_LEFT = 0x00000001;
    public static final int TEXT_ALIGN_RIGHT = 0x00000010;
    public static final int TEXT_ALIGN_CENTER_VERTICAL = 0x00000100;
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL = 0x00001000;
    public static final int TEXT_ALIGN_TOP = 0x00010000;
    public static final int TEXT_ALIGN_BOTTOM = 0x00100000;

    /**
     * 文字的颜色
     */
    private int mLabelColor;
    /**
     * 文字的大小
     */
    private int mLabelSize;
    /**
     * 文字的方位
     */
    private int mLabelAlign;

    /**
     * 标签与边框间隔
     */
    private int mLabelSpace = 10;

    /**
     * 控件的宽度
     */
    private int mViewWidth;
    /**
     * 控件的高度
     */
    private int mViewHeight;

    /**
     * 控件画笔
     */
    private Paint mPaint;

    private Paint.FontMetrics fm;

    /**
     * 文本中轴线X坐标
     */
    private float mLabelCenterX;
    /**
     * 文本baseline线Y坐标
     */
    private float mLabelBaselineY;


    private String mLabel = "";
    private float mLabelWidth;

    public LabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.labelAttrs);

        mLabel = ta.getString(R.styleable.labelAttrs_labelText);
        mLabelColor= ta.getColor(R.styleable.labelAttrs_labelColor, Color.GRAY);
        mLabelSize= (int) ta.getDimension(R.styleable.labelAttrs_labelSize, 16);
        mLabelAlign= ta.getInteger(R.styleable.labelAttrs_labelAlign, TEXT_ALIGN_LEFT|TEXT_ALIGN_CENTER_VERTICAL);
        mLabelSpace = (int) ta.getDimension(R.styleable.labelAttrs_labelSpace, 0);
        ta.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(mLabelSize);
        mPaint.setColor(mLabelColor);
        mPaint.setAntiAlias(true);
        fm = mPaint.getFontMetrics();
        setSingleLine(false);
        mLabelBaselineY = mViewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mLabel)) {
            mLabelBaselineY = mViewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
            canvas.drawText(mLabel, mLabelCenterX, mLabelBaselineY + 1.5f, mPaint);
            setSelection(getText().length());
        }
    }

    /**
     * 动态设置右边label
     *
     * @param label 输入标签
     */
    public void setLabelText(String label) {
        mLabel = label;
        mLabelWidth = mPaint.measureText(mLabel);
        setTextLocation();
//        setPadding(getPaddingLeft(),getPaddingTop(), (int) (mLabelWidth+mLabelSpace),getPaddingBottom());
        invalidate();
    }

    public String getLabelText() {
        return mLabel;
    }

    /**
     * 定位文本绘制的位置，TODO 添加左右space
     */
    private void setTextLocation() {
        fm = mPaint.getFontMetrics();
        float textCenterVerticalBaselineY = mViewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        switch (mLabelAlign) {
            case TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_CENTER_VERTICAL:
                mLabelCenterX = (float) mViewWidth +mLabelSpace;
                mLabelBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_LEFT | TEXT_ALIGN_CENTER_VERTICAL:
                mLabelCenterX = mLabelSpace/2;
                mLabelBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_RIGHT | TEXT_ALIGN_CENTER_VERTICAL:
                mLabelCenterX = mViewWidth - mLabelWidth -mLabelSpace;
                mLabelBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_CENTER_HORIZONTAL:
                mLabelCenterX = mViewWidth +mLabelSpace;
                mLabelBaselineY = mViewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_CENTER_HORIZONTAL:
                mLabelCenterX = mViewWidth +mLabelSpace;
                mLabelBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_LEFT:
                mLabelCenterX = mLabelSpace;
                mLabelBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_LEFT:
                mLabelCenterX = mLabelSpace;
                mLabelBaselineY = mViewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_RIGHT:
                mLabelCenterX = mViewWidth - mLabelWidth -mLabelSpace;
                mLabelBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_RIGHT:
                mLabelCenterX = mViewWidth - mLabelWidth -mLabelSpace;
                mLabelBaselineY = mViewHeight - fm.bottom;
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mViewWidth = getWidth();
        mViewHeight = getHeight();

        //xml布局中设置了label
        if(!TextUtils.isEmpty(mLabel)){
            mLabelWidth = mPaint.measureText(mLabel);
            setTextLocation();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setLabelColor(int mTextColor) {
        this.mLabelColor = mTextColor;
    }

    public void setLabelSize(int mTextSize) {
        this.mLabelSize = mTextSize;
    }

    public void setLabelAlign(int mTextAlign) {
        this.mLabelAlign = mTextAlign;
    }

    public void setLabelSpace(int mLabelSpace) {
        this.mLabelSpace = mLabelSpace;
    }

}
