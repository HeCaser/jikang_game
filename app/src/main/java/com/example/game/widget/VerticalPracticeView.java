package com.example.game.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VerticalPracticeView extends TextView {

    public VerticalPracticeView(Context context) {
        super(context);
    }

    public VerticalPracticeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalPracticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setNormalColor(){
        clearAnimation();
        int normalColor = Color.WHITE;
        int hintColor = Color.RED;
        ValueAnimator animator = ObjectAnimator.ofInt(this, "backgroundColor", hintColor, normalColor);//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
        animator.start();
    }
    public void setHintColor(){
        int normalColor = Color.WHITE;
        int hintColor = Color.RED;
//        ValueAnimator animator = ObjectAnimator.ofInt(this, "backgroundColor", normalColor, hintColor);//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
//        animator.setDuration(200);
//        animator.setEvaluator(new ArgbEvaluator());//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
//        animator.start();
//
        setBackgroundColor(hintColor);
    }
}
