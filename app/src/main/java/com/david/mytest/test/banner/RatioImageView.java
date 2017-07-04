package com.david.mytest.test.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by weixi on 2017/6/30.
 */

public class RatioImageView extends ImageView {
    //宽高比，由我们自己设定
    private float ratio;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        //获得属性名称和对应的值
//        for (int i = 0; i < attrs.getAttributeCount() ; i++) {
//            String name = attrs.getAttributeName(i);
//            String value = attrs.getAttributeValue(i);
//            System.out.println("====name: "+name+"value:"+value);
//        }
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView);
//        //根据属性名称获取对应的值，属性名称的格式为类名_属性名
//        ratio = typedArray.getFloat(R.styleable.MyImageView_ratio, 0.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0) {
            heightSize = (int) (widthSize * ratio + 0.5f);//根据宽度和比例计算高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY & ratio != 0) {
            widthSize = (int) (heightSize / ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
//        else {
//            throw new RuntimeException("无法设定宽高比");
//        }
        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置宽高比
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);//加速内存回收,会对轮播图产生影响，在列表项里边不要使用
    }
}
