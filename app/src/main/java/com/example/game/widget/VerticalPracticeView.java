package com.example.game.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.game.R;

public class VerticalPracticeView extends AppCompatTextView {

    private int mNormalBgColor;
    private int mHintBgColor;
    public VerticalPracticeView(Context context) {
        this(context, null);
    }

    public VerticalPracticeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalPracticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNormalBgColor = getResources().getColor(R.color.white);
        mHintBgColor = getResources().getColor(R.color.color_31c2ff);
        setBackgroundColor(mNormalBgColor);
    }

    public int row, colum; //行号,列号.

    public void setNormalColor() {
        clearAnimation();
        ValueAnimator animator = ObjectAnimator.ofInt(this, "backgroundColor", mHintBgColor, mNormalBgColor);//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
        animator.setDuration(500);
        animator.setEvaluator(new ArgbEvaluator());//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
        animator.start();
    }

    public void setHintColor(int speed) {
        int normalColor = Color.WHITE;
//        ValueAnimator animator = ObjectAnimator.ofInt(this, "backgroundColor", normalColor, hintColor);//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
//        animator.setDuration(200);
//        animator.setEvaluator(new ArgbEvaluator());//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
//        animator.start();
//
        setBackgroundColor(mHintBgColor);

    }
}
