package com.example.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 文案内容均匀分布
 */
public class SeparateTextView extends TextView {
    private CharSequence originalText = "";
    private int mWidth, mHeight;
    public SeparateTextView(Context context) {
        this(context, null);
    }

    public SeparateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeparateTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int length = originalText.length();
        int itemWitdh = mWidth/length;
        int y  = getBaseline();
        for (int i = 0; i < length; i++) {
            canvas.drawText(String.valueOf(originalText.charAt(i)),itemWitdh*i+itemWitdh/4,y,  getPaint());
        }
    }
}