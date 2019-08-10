package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.game.utils.ScreenUtils;


/**
 * Created by hepan on 20190705
 * 在view中间画一个半透明圆圈,模拟放大镜效果
 */

public class MagnifyTextView extends TextView {


    private Paint mPaint;
    private float mCircleCenterX;
    private float mCircleCenterY;
    private float mRadius;

    private boolean isDrawCircle = false;

    //    private
    public MagnifyTextView(Context context) {
        super(context);
        init();
    }

    public MagnifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagnifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(0xddff5555);
        mPaint.setColor(0x33ff0000);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCircleCenterX = getMeasuredWidth() / 2;
        mCircleCenterY = getMeasuredHeight() / 2;
//        mRadius = ScreenUtils.dip2px(getContext(), getMeasuredHeight()/2.1f);
        mRadius = ScreenUtils.dip2px(getContext(), 18);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        SpannableString spannableString = new SpannableString("这是一个字符串");
//        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 2 ,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        setText(spannableString);
        if (isDrawCircle) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            canvas.drawCircle(mCircleCenterX, mCircleCenterY, mRadius, mPaint);
        }
    }


    public void setCircleSWitch(boolean isOpen) {
        isDrawCircle = isOpen;
        postInvalidate();

    }

    public void setCircleRadius(float value) {
        mRadius = value;
    }

}
