package com.example.game.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.ViewTreeObserver
import android.widget.TextView
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
        const val ERROR_WORD = "閬棩楹槬棰鑽夐鍟婁鎸璁棰勯槻鎶" +
                "鏂戞鍙戦樋鍛嗗戝姩鏈鐖鍘棩鍘噹" +
                "搸閬埢鐖辩湇鍟墦鍙戝彂澶樋鐗鍝鍝鍓嶆鍎" +
                "鏈熶杈悆鍛樼儹嬭勬棩鍒鍝闂殑鍗庡洖澶嶅鍘摝鍋劧傚" +
                "畨寰鍝獟鍘悆鍝啽鍘獟濡墠鏃鍩熼剛鐟鏃濮喎" +
                "劏濮懡澶愰崯閹鍨庣幎弬鍦煃閻劍澧崣鎴閸欐嬮崨鍡椼姵崣鎴濆" +
                "閺堝搫浼撻鍗炲瀻閸樼粯氬櫣娴滃搫鎼搁鍥煂鐟曞懐鍩畱鐞絾婀囬崯閸欐" +
                "閻楁閸濓掗崜锤閸庡閺堢喍曟閸"

        const val ERROR_WORD_SIZE = ERROR_WORD.length - 1
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
                    startGame()
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

    private var mRemoveCount = 0
    private var mRecord = 0L
    private var mKey = ""
    private var mWidth = 0
    private var mHeight = 0
    private var mStart = 0F
    private var mTvHeight = 0F
    private var mDelayTime = 1000L

    val padding = screenWidth / 100
    val margin = screenWidth / 100
    val textSize = screenWidth / 50.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(com.example.game.R.layout.activity_search_word)
        initViewAndData()
        initListener()
    }

    private fun initListener() {
        flexBox.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val tv = flexBox.getChildAt(0)
            var totalSpace = (tv.bottom - tv.top) + margin
            mStart = flexBox.top + tv.bottom.toFloat()
            mTvHeight = totalSpace.toFloat()
        })
    }

    private fun initViewAndData() {
        mKey = intent.getStringExtra("key")
        mRecord = intent.getLongExtra("record", 0)
        mWidth = screenWidth
        mHeight = screenHeight
        setCenterTitle("济康-搜索词")


        for (num in 0..500) {
            val tv = TextView(this)
            tv.textSize = textSize

            tv.text = "${getErrorWord(num)}"
            tv.setPadding(padding + 10, padding, padding + 10, padding)
            flexBox.addView(tv)
            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.topMargin = margin
            }
            if (num % 4 == 0) {
                tv.setBackgroundColor(resources.getColor(com.example.game.R.color.colorPrimary))
            }
        }
    }


    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_MOVE, 500)
    }

    private fun getErrorWord(index: Int): Char {
        val position = index % ERROR_WORD_SIZE
        return ERROR_WORD[position]
    }
}
