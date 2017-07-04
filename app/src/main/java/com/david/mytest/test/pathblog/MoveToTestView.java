
package com.david.mytest.test.pathblog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class MoveToTestView extends View {

    private static final int PATH_WIDTH = 2;
    private Paint mPaint;

    private Path mPath;
    private RectF mRectF;

    public MoveToTestView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(PATH_WIDTH);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mPath.moveTo(50, 50);
        mPath.lineTo(150, 150);
        // 相对前面的点 x 往后移动 100 个像素，y 往下移动 100 个像素
        mPath.rMoveTo(100, 100);
        mPath.lineTo(400, 400);
        mRectF = new RectF(0, 400, 800, 800);
        mPath.arcTo(mRectF, 0, 90, true);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);
        canvas.drawRect(mRectF, mPaint);

    }
}
