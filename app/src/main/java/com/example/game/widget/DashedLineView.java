package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;



public class DashedLineView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);                

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.DKGRAY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub      
        super.onDraw(canvas);              

        Path path = new Path();
        path.moveTo(mWidth/2, 0);
        path.lineTo(mWidth/2,mHeight);
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
        mPaint.setPathEffect(effects);
        canvas.drawPath(path, mPaint);
    }   
}  