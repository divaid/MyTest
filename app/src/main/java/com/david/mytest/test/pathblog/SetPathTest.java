
package com.david.mytest.test.pathblog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.view.View;

public class SetPathTest extends View {

    private Path mEndPath;
    private Path mSrcPath;

    private Paint mPaint;

    public SetPathTest(Context context) {
        super(context);
        init();
    }

    private void init() {
        mEndPath = new Path();
        mEndPath.addCircle(300, 300, 100, Direction.CW);
        //mEndPath.addRect(new RectF(50, 50, 250, 200), Direction.CW);

        mSrcPath = new Path();
        mSrcPath.addRect(new RectF(50, 50, 250, 200), Direction.CW);
        mEndPath.set(mSrcPath);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mEndPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    };
}
