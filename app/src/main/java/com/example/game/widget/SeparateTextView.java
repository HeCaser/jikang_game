package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 文案内容均匀分布
 */
public class SeparateTextView extends TextView {
    private int mWidth, mHeight;
    private int mColor;
    public SeparateTextView(Context context) {
        super(context);
    }

    public SeparateTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SeparateTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SeparateTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        String originalText = getText().toString();
        if(TextUtils.isEmpty(originalText)){
            super.onDraw(canvas);
        }else{
            Paint p = getPaint();
            p.setColor(mColor);
            int length = originalText.length();
            int itemWitdh = mWidth/length;
            int y  = getBaseline();
            for (int i = 0; i < length; i++) {

                canvas.drawText(String.valueOf(originalText.charAt(i)),itemWitdh*i+itemWitdh/4,y,  p);
            }
        }

    }
}