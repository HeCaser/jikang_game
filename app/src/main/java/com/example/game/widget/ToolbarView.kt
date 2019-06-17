package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import kotlinx.android.synthetic.main.toolbar_view.view.*

/**
 * Author pan.he
 * Date 2018/12/06
 */
class ToolbarView:ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init{
        LayoutInflater.from(context).inflate(R.layout.toolbar_view,this,true)
    }


    fun setCenterTitle(@StringRes resId: Int) {
        tvTitle.setText(resId)
    }

    fun setCenterTitle(title: String) {
        tvTitle.text = title
    }

}