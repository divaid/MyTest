
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
import android.widget.Toast;

public class IsRectTest extends View {

    private static final String TAG = "FillTypeTest";

    private Path mEndPath;
    private Path mSrcPath;

    private Paint mPaint;
    private Context mContext;
    private RectF mRectF = new RectF(200, 300, 500, 500);

    public IsRectTest(Context context) {
        super(context);
        init();
        mContext = context;
    }

    private void init() {
        mEndPath = new Path();
        //mEndPath.addCircle(300, 300, 150, Direction.CW);
        mEndPath.addCircle(380, 380, 150, Direction.CW);
        mEndPath.addRect(mRectF, Direction.CW);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mEndPath, mPaint);
        Toast.makeText(mContext,
                "is rect :" + mEndPath.isRect(mRectF),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    };
}
