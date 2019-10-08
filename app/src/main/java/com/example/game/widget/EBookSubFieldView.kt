package com.example.game.widget

import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.clearSpans
import com.example.game.R
import kotlinx.android.synthetic.main.sub_field_view.view.*

/**
 * Author pan.he
 * EBook 分类的view 每行显示三个词
 */
class EBookSubFieldView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var mLevel = 1
    var mViewList = arrayListOf<TextView>()

    init {
        LayoutInflater.from(context).inflate(R.layout.sub_field_view, this, true)
        initView()
    }

    private fun initView() {
        if (mViewList.isEmpty()) {
            mViewList.add(textView1)
            mViewList.add(textView2)
            mViewList.add(textView3)
        }
    }

    fun setContent(text: String, pos: Int) {
        if (pos !in 0..2) return
        mViewList[pos].text = text
    }

    fun setContent(text1: String, text2: String, text3: String) {
        mViewList[0].text = text1
        mViewList[1].text = text2
        mViewList[2].text = text3
    }


    fun clearStyle(pos: Int) {
        if (pos !in 0..2) return
        val text = mViewList[pos].text
        val span = SpannableStringBuilder(text)
        span.clearSpans()
        mViewList[pos].text = span
    }

    fun setStyle(pos: Int) {
        if (pos !in 0..2) return
        val text = mViewList[pos].text
        val span = SpannableStringBuilder(text)
        val length = text.length
        var start = 0
        if (length >= 3) {
            if (length % 2 == 0) {
                start = length / 2 - 1
            } else {
                start = length / 2
            }
        }
        span.setSpan(
            ForegroundColorSpan(Color.RED),
            start,
            start + 1,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        mViewList[pos].text = span
    }

    fun getChild(pos: Int): TextView {
        return mViewList[pos]
    }

}
