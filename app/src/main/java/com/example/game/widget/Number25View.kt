package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import com.example.game.bean.NumberBean
import com.example.game.util.res2color
import kotlinx.android.synthetic.main.number_view.view.*

/**
 * Author pan.he
 * Date 2018/12/06
 */
class Number25View : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //选中 错误 默认
    private val bgList = arrayListOf(R.color.colorPrimary_60, R.color.color_e60012_60, R.color.white)
    private val colorList = arrayListOf(R.color.white, R.color.white,R.color.colorPrimary)

    init {
        LayoutInflater.from(context).inflate(R.layout.number_25_view, this, true)
    }

    private fun setNumber(number: String) {
        tvNumber.text = number
    }

//    private fun setState(state: STATE) {
//        when (state) {
//            STATE.RED -> {
//                tvNumber.setBackgroundColor(this.context.res2color(bgList[1]))
//                tvNumber.setTextColor(this.context.res2color(colorList[0]))
//            }
//        }
//    }
//
//    enum class STATE {
//        RED(), GREED(), NORMAL()
//    }

    fun setData(bean: NumberBean) {
        setNumber(bean.number.toString())
        if (bean.isError) {
            tvNumber.setBackgroundColor(this.context.res2color(bgList[1]))
            tvNumber.setTextColor(this.context.res2color(colorList[1]))
        } else if (bean.isSelected) {
            tvNumber.setBackgroundColor(this.context.res2color(bgList[0]))
            tvNumber.setTextColor(this.context.res2color(colorList[0]))
        } else {
            tvNumber.setBackgroundColor(this.context.res2color(bgList[2]))
            tvNumber.setTextColor(this.context.res2color(colorList[2]))
        }
    }
}
