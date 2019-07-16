package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.ViewTreeObserver
import com.example.game.R
import com.example.game.util.px2dp
import com.example.game.util.screenWidth
import com.example.game.utils.StatusBarUtils
import com.example.game.widget.MagnifyTextView
import kotlinx.android.synthetic.main.activity_ebook_tree.*
import androidx.core.text.isDigitsOnly as isDigitsOnly1


/**
 *EBook
 */
class EBookTreeActivity : BaseActivity() {

    companion object {
        const val MSG_MOVE_LINE = 1
        const val MSG_START_MOVE = 2
        const val MSG_START_GAME = 3

        fun start(ctx: Context, speed: Int) {
            Intent(ctx, EBookTreeActivity::class.java).apply {
                putExtra("speed", speed)
                ctx.startActivity(this)
            }
        }
    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    initShowView()
                }
                MSG_START_MOVE -> {
                    startGame()
                }
            }
        }
    }

    private var mContentDp = 1
    private var mItemHeight = 60
    private var mContentWidth = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebook_tree)
        initViewAndData()
        initListener()

    }

    private fun initListener() {


        flexBox.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {

        })
    }

    private fun initViewAndData() {
        mContentWidth = screenWidth

    }


    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 100)
    }

    private fun initShowView() {
        if (flexBox.childCount != 0) return
        val height = flexBox.measuredHeight
        mContentDp = px2dp(height.toFloat())
        if (mContentDp == 0) {
            mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 50)
        } else {
            flexBox.removeAllViews()
            val count = mContentDp / mItemHeight
            for (num in 0 until 1) {
                val tv = MagnifyTextView(this)
                tv.textSize = 30f

                tv.text = "$num"
                tv.gravity = Gravity.CENTER
                tv.setTextColor(resources.getColor(R.color.color_333333))
                flexBox.addView(tv, mContentWidth, mItemHeight)
                val para = tv.layoutParams
//                if (para is FlexboxLayout.LayoutParams) {
//                    para.height=dp2px(mItemHeight.toFloat())
//                    para.width = screenWidth
//                }
                tv.setOnClickListener {
                }
            }

        }
    }

    /**
     * 设置view 开始游戏
     */
    private fun startGame() {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.sendEmptyMessage(MSG_MOVE_LINE)
    }


    /**
     * 结束游戏
     */
    private fun finisGame() {


    }


//    private fun getScore(): Int {
//        val text = tvScore.text.toString()
//        return if (text.isDigitsOnly1()) text.toInt() else 0
//
//    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
