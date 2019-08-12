package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import kotlinx.android.synthetic.main.sub_field_view.view.*

/**
 * Author pan.he
 * EBook 分类的view 每行显示三个词
 */
class EBookSubFieldView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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
        if (pos >= mViewList.size) return
        mViewList[pos].text = text
    }


}
