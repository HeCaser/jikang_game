package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.example.game.R;


/**
 * Author pan.he
 * 奇偶数等界面最上面的view
 * 圆点和横线,步进增加
 * 暂时不做padding等细节处理
 */
public class CircleStepView extends View {

    private Paint mCirclePaint;
    private Paint mLinePaint;
    private int mBgColor;
    private int mFgColor;

    private int mWidth;
    private int mHeight;
    private float mCircleRadius;

    private int mTotalStep = 10; //最高步进
    private int mStep = 1; //当前步进

    public CircleStepView(Context context) {
        super(context);
        init();
    }

    public CircleStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleStepView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        mBgColor = getContext().getResources().getColor(R.color.colorPrimary);
        mFgColor = getContext().getResources().getColor(R.color.colorAccent);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mBgColor);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mBgColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //一些数值的计算
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCircleRadius = mHeight / 4.0f;
        mLinePaint.setStrokeWidth(mCircleRadius * 0.9f);

        //划圆点和横线
        drawCircleAndLine(canvas);

    }

    private void drawCircleAndLine(Canvas canvas) {


        float cY = mHeight / 2.0f;
        //小圆点之间的间隔
        float mInterval = (mWidth - 2 * mCircleRadius) / (mTotalStep - 1);

        //线,关键是计算顶层线的结束位置
        float topLineEnd;
        if (mStep == 1) {
            topLineEnd = mInterval/2+mCircleRadius;
        } else if (mStep == mTotalStep) {
            topLineEnd = mWidth - mCircleRadius;
        }else{
            topLineEnd= mInterval*(mStep-0.5f)+mCircleRadius;
        }

        mLinePaint.setColor(mBgColor);
        canvas.drawLine(mCircleRadius, cY, mWidth - mCircleRadius, cY, mLinePaint);
        mLinePaint.setColor(mFgColor);
        canvas.drawLine(mCircleRadius, cY, topLineEnd, cY, mLinePaint);



        //画圆点
        for (int num = 0; num < mTotalStep; num++) {
            if (num < mStep) {
                mCirclePaint.setColor(mFgColor);
            } else {
                mCirclePaint.setColor(mBgColor);
            }
            canvas.drawCircle((num * mInterval) + mCircleRadius, cY, mCircleRadius, mCirclePaint);
        }


    }


    public void setTotalStep(int num) {
        mTotalStep = num;
    }

    public void setStep(int step) {
        if (step <= 0 || step > mTotalStep) return;
        mStep = step;
        postInvalidate();
    }
}