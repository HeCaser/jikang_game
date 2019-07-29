package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import kotlinx.android.synthetic.main.ebook_setting.view.*

/**
 * EBook设置类, 速度和进度
 */
class EbookSettingView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var mSpeed = 500
    var mMax = 1600
    var mMin = 50

    companion object {
        const val CALLBACK_FINISH = 1
        const val CALLBACK_CHANGE_SPEED = 2
    }

    private var mCallback: CallBack? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.ebook_setting, this, true)
        initView()
    }

    private fun initView() {
        tvSpeed.text = mSpeed.toString()
        tvAddSpeed.setOnClickListener {
            if (mSpeed < mMax) {
//                mSpeed += 25
                mSpeed=1600
                tvSpeed.text = mSpeed.toString()
            }
        }
        tvSubSpeed.setOnClickListener {
            if (mSpeed > mMin) {
//                mSpeed -= 25
                mSpeed=50
                tvSpeed.text = mSpeed.toString()

            }
        }
        tvComplete.setOnClickListener {
            mCallback?.apply {
                onSetCallBack(CALLBACK_FINISH, mSpeed)
            }
        }

    }


    interface CallBack {
        fun onSetCallBack(type: Int, speed: Int)
    }

    fun setCallback(callBack: CallBack) {
        mCallback = callBack
    }

    fun getMaxSpeed(): Int {
        return mMax
    }

    fun getMinSpeed(): Int {
        return mMin
    }

}
