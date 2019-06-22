package com.example.game.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.core.animation.addListener
import com.example.game.R
import com.example.game.util.screenHeight
import com.example.game.util.screenWidth
import com.example.game.utils.StatusBarUtils
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_search_word.*


/**
 *搜索词
 */
class SearchWordActivity : BaseActivity() {

    companion object {
        const val MSG_MOVE_LINE = 1
        const val MSG_START_MOVE = 2
        fun start(ctx: Context, key: String, record: Long) {
            Intent(ctx, SearchWordActivity::class.java).apply {
                putExtra("key", key)
                putExtra("record", record)
                ctx.startActivity(this)
            }
        }
    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_MOVE_LINE -> {
                    val anim = ObjectAnimator.ofFloat(viewLine, "y", mStart + (mTvHeight * mRemoveCount))
                    anim.start()
                    mRemoveCount++
                    this.sendEmptyMessageDelayed(MSG_MOVE_LINE, mDelayTime)
                }
                MSG_START_MOVE -> {
                    this.removeCallbacksAndMessages(null)
                    this.sendEmptyMessage(MSG_MOVE_LINE)
                }
            }
        }
    }
    private var mRemoveCount = 0
    private var mRecord = 0L
    private var mKey = ""
    private var mWidth = 0
    private var mHeight = 0
    private var mStart = 0F
    private var mTvHeight = 0F
    private var mDelayTime = 1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_word)
        initViewAndData()
        initListener()
    }

    private fun initListener() {

    }

    private fun initViewAndData() {
        mKey = intent.getStringExtra("key")
        mRecord = intent.getLongExtra("record", 0)
        mWidth = screenWidth
        mHeight = screenHeight

        val padding = screenWidth / 100
        val margin = screenWidth / 100
        val textSize = screenWidth / 50.0F
        for (num in 0..500) {
            val tv = TextView(this)
            tv.textSize = textSize
            tv.text = "撒$num"
            tv.setPadding(padding + 10, padding, padding + 10, padding)
            flexBox.addView(tv)
            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.topMargin = margin
            }
            if (num % 4 == 0) {
                tv.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
        }
        flexBox.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val tv = flexBox.getChildAt(0)
//            println("bottom=${tv.bottom}")
            var totalSpace = (tv.bottom - tv.top) + margin
            mStart = flexBox.top+tv.bottom.toFloat()
            mTvHeight = totalSpace.toFloat()

//            moveLine(tv.y, totalSpace.toFloat())
        })
    }

    /**
     *移动线段
     */
    private fun moveLine(bottom: Float, totalSpace: Float) {
        val coutDown = ValueAnimator.ofInt(1, 10)
        coutDown.duration = 10000
        coutDown.interpolator = LinearInterpolator()
        var currentValue: Int
        coutDown.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int
            println("值=$currentValue")

        }

        coutDown.start()
//        val start = viewLine.bottom

//
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_MOVE,500)
    }
}
