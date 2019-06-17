package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEachIndexed
import com.example.game.R
import kotlinx.android.synthetic.main.single_selection_view.view.*


/**
 * Author pan.he
 * Date 2019/05/26
 *
 * 单选
 */
class SingleSelectionView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        LayoutInflater.from(context).inflate(R.layout.single_selection_view, this, true)
    }

    private var mViewList = ArrayList<RadioButton>()
    private var mSelectPos = 0
    private val MAX_NUM = 8
    private var mContentList: List<String>? = null

    private fun initView() {
        if (mViewList.isEmpty()) {
            mViewList.add(rb1)
            mViewList.add(rb2)
            mViewList.add(rb3)
            mViewList.add(rb4)
            mViewList.add(rb5)
            mViewList.add(rb6)
            mViewList.add(rb7)
            mViewList.add(rb8)
        }
    }

    fun setSelected(pos: Int) {
        if (pos > MAX_NUM - 1) return
        initView()
        mViewList[pos].isChecked = true
        mSelectPos = pos
    }

    fun getSelectContent(pos: Int): String {
        if (pos > MAX_NUM - 1) return ""
        initView()
        return mViewList[pos].text.toString()
    }

    fun getSelectPos(): Int {
        var pos = 0
        mViewList.forEachIndexed { index, radioButton ->
            if (radioButton.isChecked) pos = index
        }

        return pos
    }


    fun setContentList(contents: List<String>) {
        if (contents.size > MAX_NUM) return
        val count = contents.size
        mContentList = contents
        initView()
        for (i in 0 until count) {
            mViewList[i].text = mContentList!![i]
        }
        for (pos in count until MAX_NUM) {
            mViewList[pos].visibility = View.GONE
        }
    }

}
