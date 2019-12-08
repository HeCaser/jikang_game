package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.game.utils.ScreenUtils;

public class WaterMarkView extends ConstraintLayout {

    private int mWidth, mHeight;
    private Paint mPaint;
    private String mName="济康智学@版权所有";

    public WaterMarkView(@NonNull Context context) {
        this(context, null);
    }

    public WaterMarkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterMarkView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(ScreenUtils.dip2px(getContext(), 10f));
        mPaint.setColor(Color.parseColor("#e4e4e4"));
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int rowCount = mHeight/ScreenUtils.dip2px(getContext(),60f);
        int columnCount = 3;

        float startx, starty;
        float endX, endY;
        Path path = new Path();
        for (int i = 0; i < rowCount; i++) {
            starty = (mHeight / rowCount) * i;

            starty+=100;
            for (int j = 0; j < columnCount; j++) {
                startx = (mWidth / columnCount) * (j+0.3f);
                path.reset();
                path.moveTo(startx, starty);
                endX = startx+1000;
                endY = starty-600;
                path.lineTo(endX, endY);
                canvas.drawTextOnPath(mName, path, 0, 0, mPaint);
            }
        }
    }


    public void showWaterMark(String name){
        mName  = name;
        setVisibility(VISIBLE);
        postInvalidate();
    }
}

