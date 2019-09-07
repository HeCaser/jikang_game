package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.example.game.R;
import com.example.game.utils.ScreenUtils;


/**
 * Author pan.he
 * 扩散的圆圈,用在 <速读-循环> 里
 */
public class CircleDiffusionView extends View {
    private Paint mCirclePaint;
    private Paint mLinePaint;

    private float mCircleRadius = 50;

    private int mScreenWidth = 0;
    private int mWidth;
    private int mHeight;

    public CircleDiffusionView(Context context) {
        super(context);
        init();
    }

    public CircleDiffusionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleDiffusionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        mScreenWidth = ScreenUtils.getScreenSize(getContext()).x;
        mCircleRadius = ScreenUtils.dip2px(getContext(), 20);
        mWidth = (int) (mScreenWidth * 0.12);
        if (mWidth < mCircleRadius * 4) {
            mWidth = (int) (mCircleRadius * 4);
        }
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(ScreenUtils.dip2px(getContext(), 2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //划圆点和横线
        drawCircleAndLine(canvas);

    }

    private void drawCircleAndLine(Canvas canvas) {
        int cx = mWidth / 2;
        int cy = mHeight / 2;
        int outRadius = (mWidth - 16) / 2;
        canvas.drawCircle(cx, cy, mCircleRadius, mCirclePaint);
        canvas.drawCircle(cx, cy, outRadius, mLinePaint);
    }


    public void updateWidth(int width) {
        mWidth = width;
        requestLayout();
    }


    public int getmWidth() {
        return mWidth;
    }
}