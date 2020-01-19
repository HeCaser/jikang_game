package com.example.game.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.example.game.R
import kotlinx.android.synthetic.main.view_show_multi_number.view.*
import kotlin.random.Random

/**
 * 展示多个数字
 * 用在记忆数选择正确数字的时候
 */
class ShowMultiNumberView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private var mCallback: CallBack? = null
    private val mViewList = arrayListOf<TextView>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_show_multi_number, this, true)
        initView()
    }

    private fun initView() {
        llMultiOne.forEach {
            mViewList.add(it as TextView)
        }
        llMultiTwo.forEach {
            mViewList.add(it as TextView)
        }

        mViewList.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                mCallback?.onSetCallBack(index, textView.text.toString())
            }
        }
    }

    fun setRightStyle(pos: Int) {
        mViewList[pos].isSelected = true
    }


    fun initStyle() {
        mViewList.forEach {
            it.isSelected = false
        }
    }

    fun setShowNumber(number: ArrayList<String>) {
        for (count in 0 until 4) {
            mViewList[count].isSelected = false
            mViewList[count].text = number[count]
        }
    }

    interface CallBack {
        fun onSetCallBack(pos: Int, number: String)
    }

    fun setCallback(callBack: CallBack) {
        mCallback = callBack
    }

}
