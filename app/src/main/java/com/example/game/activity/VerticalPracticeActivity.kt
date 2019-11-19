package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import com.example.game.R
import com.example.game.utils.ScreenUtils
import com.example.game.utils.StatusBarUtils
import com.example.game.widget.VerticalPracticeView
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_vertical_practice.*

/**
 * 垂直练习
 */
class VerticalPracticeActivity : BaseActivity() {

    companion object {
        const val MSG_INIT_VIEW = 1
        const val MSG_MOVE_HINT = 2  //移动提示的位置
        fun start(ctx: Context) {
            Intent(ctx, VerticalPracticeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_INIT_VIEW -> {
                    initGameView()
                }
                MSG_MOVE_HINT -> {
                    moveHintView()
                }
            }
        }
    }

    var mIconColor = 1
    val mViewList = arrayListOf<VerticalPracticeView>()
    var mHintPos = 0
    var TOTAL_WORD_NUMBER = 0
    var mSpeed = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_practice)
        initViewAndData()
        initListener()
    }

    private fun initListener() {
        mIconColor = resources.getColor(R.color.color_31c2ff)
        tvVerticalAdd.background.setColorFilter(mIconColor, PorterDuff.Mode.SRC_ATOP)
        tvVerticalSub.background.setColorFilter(mIconColor, PorterDuff.Mode.SRC_ATOP)
        tvVerticalSpeed.text = "$mSpeed"

        tvVerticalSub.setOnClickListener {
            if (mSpeed > 80)
                mSpeed -= 20
            tvVerticalSpeed.text = "$mSpeed"
        }
        tvVerticalAdd.setOnClickListener {
            if (mSpeed < 800)
                mSpeed += 20
            tvVerticalSpeed.text = "$mSpeed"
        }
    }

    private fun initViewAndData() {

    }

    /**
     * 游戏开始前的初始化
     */
    private fun initGameView() {
        if (flexBox.childCount != 0) return
        val parentH = flexBox.measuredHeight
        if (parentH == 0) {
            //布局未加载好,延时再次调用
            mHandler.sendEmptyMessageDelayed(MSG_INIT_VIEW, 100)
            return
        }
        addViewToFlexBox()

    }

    /**
     * 添加view
     */
    private fun addViewToFlexBox() {
        val parentH = flexBox.measuredHeight
        val parentW = flexBox.measuredWidth

        val itemHeight = ScreenUtils.dip2px(this, 90f) //条目的高度
        val itemWidth = ScreenUtils.dip2px(this, 30f)  //条目的宽度

        val margin = ScreenUtils.dip2px(this, 10f)
        val rowNumber = (parentH) / (itemHeight + margin) //行数= 总高度/(条目高度+顶部间距)
        val columNumber = parentW / (itemWidth + margin) //列数=总宽度(条目宽度+横向间距)

        TOTAL_WORD_NUMBER = rowNumber * columNumber

        //添加view给flexbox
        flexBox.removeAllViews()
        for (num in 0 until TOTAL_WORD_NUMBER) {
            val tv = VerticalPracticeView(this)
//            tv.text = "$num"
            flexBox.addView(tv, itemWidth, itemHeight)
            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.bottomMargin = margin
                para.rightMargin = margin
//
//                if (num % columNumber == 0) {
//                    para.leftMargin = margin
//                }
//                if (num <= columNumber) {
//                    para.topMargin = margin
//                }
            }
            tv.row = num/columNumber
            tv.colum = num%columNumber
            mViewList.add(tv)
        }

        //以列的倒序,行的正序排列
        mViewList.sortBy { -it.colum }
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, 50)

    }

    private fun moveHintView() {
        if (mHintPos == TOTAL_WORD_NUMBER){
            //最后一个
            mViewList.get(mHintPos-1).setNormalColor()
            mHintPos=0
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, 900 - mSpeed.toLong())
            return
        }
        mViewList.get(mHintPos).setHintColor(1)
        if (mHintPos>0){
            mViewList.get(mHintPos-1).setNormalColor()
        }

        mHintPos++
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, 900 - mSpeed.toLong())

    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_INIT_VIEW, 50)

    }

    override fun onDestroy() {
        super.onDestroy()
        //还原对drawable资源的处理
        tvVerticalAdd.background.setColorFilter(mIconColor, PorterDuff.Mode.DST)
        tvVerticalSub.background.setColorFilter(mIconColor, PorterDuff.Mode.DST)
    }

}
