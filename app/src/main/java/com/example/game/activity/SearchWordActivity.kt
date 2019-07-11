package com.example.game.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import com.example.game.R
import com.example.game.constant.SEARCH_WORD_ACTIVITY
import com.example.game.util.screenHeight
import com.example.game.util.screenWidth
import com.example.game.utils.StatusBarUtils
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_search_word.*
import kotlin.random.Random


/**
 *搜索词
 */
class SearchWordActivity : BaseActivity() {

    companion object {
        const val TOTAL_WORD_NUMBER = 223
        const val MSG_MOVE_LINE = 1
        const val MSG_START_MOVE = 2
        const val MSG_TIME_COUT_DOWN = 3
        const val ERROR_WORD = "閬棩楹槬棰鑽夐鍟婁鎸璁棰勯槻鎶" +
                "鏂戞鍙戦樋鍛嗗戝姩鏈鐖鍘棩鍘噹" +
                "搸閬埢鐖辩湇鍟墦鍙戝彂澶樋鐗鍝鍝鍓嶆鍎" +
                "鏈熶杈悆鍛樼儹嬭勬棩鍒鍝闂殑鍗庡洖澶嶅鍘摝鍋劧傚" +
                "畨寰鍝獟鍘悆鍝啽鍘獟濡墠鏃鍩熼剛鐟鏃濮喎" +
                "劏濮懡澶愰崯閹鍨庣幎弬鍦煃閻劍澧崣鎴閸欐嬮崨鍡椼姵崣鎴濆" +
                "閺堝搫浼撻鍗炲瀻閸樼粯氬櫣娴滃搫鎼搁鍥煂鐟曞懐鍩畱鐞絾婀囬崯閸欐" +
                "閻楁閸濓掗崜锤閸庡閺堢喍曟閸"

        const val RIGHT_WORD = "人月肉多王内火于阿拉基家达都爱华发度说前明词动光东意思巨率头网起民低头就思怕的故不是"
        const val ERROR_WORD_SIZE = ERROR_WORD.length - 1
        fun start(ctx: Context, speed: Int) {
            Intent(ctx, SearchWordActivity::class.java).apply {
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
                MSG_MOVE_LINE -> {
                    val anim = ObjectAnimator.ofFloat(viewLine, "y", mStart + (mTvHeight * mRemoveCount))
                    anim.start()
                    mRemoveCount++
                    if (mRemoveCount < 25) {
                        this.sendEmptyMessageDelayed(MSG_MOVE_LINE, mLineMoveDelayTime)
                    }
                }
                MSG_START_MOVE -> {
                    startGame()
                }
                MSG_TIME_COUT_DOWN -> {
                    handTimeCount()
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
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUT_DOWN, mCountDownTimeDelay)
    }

    private var mRemoveCount = 0
    private var mRecord = 0L
    private var mSpeed = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mStart = 0F
    private var mTvHeight = 0F
    private var mLineMoveDelayTime = 1000L
    private var mCountDownTimeDelay = 100L
    private var mSelectWords = arrayListOf<String>()
    private var mShowView = arrayListOf<TextView>()
    private var mSocre = 0
    private var mTotalTime = 90
    val padding = screenWidth / 160
    val margin = screenWidth / 140
    val textSize = screenWidth / 28.0F

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
        mSpeed = intent.getIntExtra("speed", 1)
        //根据速度等级 1-10 设置移动线段的间隔 等级越高,间隔越短
        mLineMoveDelayTime = 1500 - (mSpeed * 100).toLong()

        //计算pb的最大值, 倒计时共90s
        mTotalTime = 900 * mCountDownTimeDelay.toInt()
        progressBar.max = mTotalTime
        progressBar.progress=mTotalTime

        mWidth = screenWidth
        mHeight = screenHeight
        setCenterTitle("济康-搜索词")
        mShowView.add(tvContent1)
        mShowView.add(tvContent2)
        mShowView.add(tvContent3)
        mShowView.add(tvContent4)
        initGameView()
    }

    /**
     * 游戏开始前的初始化,可能需要多次重置
     */
    private fun initGameView() {
        //设置需要找出的词 四个
        var start = Random.nextInt(RIGHT_WORD.length - 5)
        println("其实$start")
        for (num in 0..3) {
            mSelectWords.add(num, RIGHT_WORD[start + num].toString())
            with(mShowView[num]) {
                text = RIGHT_WORD[start + num].toString()
                isSelected = false
            }
        }

        //添加view给flexbox
        flexBox.removeAllViews()
        for (num in 0..TOTAL_WORD_NUMBER) {
            val tv = TextView(this)
            tv.textSize = textSize

            tv.text = "${getErrorWord(num)}"
            tv.setTextColor(getTextColor(num))
            tv.setPadding(padding + 6, padding, padding + 6, padding)
            flexBox.addView(tv)
            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.topMargin = margin
            }
//            if(num%9==0){
//                tv.setBackgroundColor(resources.getColor(R.color.colorPrimary))
//            }
        }

        //随机放入待选择的词
        val q = TOTAL_WORD_NUMBER / 4
        for (num in 0..3) {
            var random = Random.nextInt(q * num, q * (num + 1))
            while (random >= TOTAL_WORD_NUMBER || random == 0) {
                random = Random.nextInt(q * (num + 1))
            }
            val tv = flexBox[random] as TextView
            tv.setOnClickListener {
                handleWordClick(num)
            }
            tv.text = mSelectWords[num]
        }
    }

    /**
     * 待选择的词被正确点击
     * @param num 被点击词是第几个 从0开始
     */
    private fun handleWordClick(num: Int) {
        if (mShowView[num].isSelected) return
        mShowView[num].isSelected = true
        mSocre += 10
        tvScore.text = "$mSocre"
        if (mSocre % 40 == 0) {
            mRemoveCount = 0
            initGameView()
            mHandler.removeMessages(MSG_MOVE_LINE)
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_LINE, 50L)
        }
    }

    /**
     * 90s倒计时
     */
    private fun handTimeCount() {
        mTotalTime -= 100
        if (mTotalTime == 0) {
            finisGame()
        }
        progressBar.progress = mTotalTime
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUT_DOWN, mCountDownTimeDelay)
    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        val spKey = SEARCH_WORD_ACTIVITY + mSpeed
        SearchRecordActivity.start(this, spKey, getScore(), mSpeed)
        finish()
    }


    private fun getScore(): Int {
        val text = tvScore.text.toString()
        return if (text.isDigitsOnly()) text.toInt() else 0

    }

    /**
     * 根据起始颜色确定tv色值
     */
    var star = 0xff098dfa
    var end = Color.RED

    var a1 = star shr 24 and 0xff
    var r1 = star shr 16 and 0xff
    var g1 = star shr 8 and 0xff
    var b1 = star and 0xff

    var a2 = end shr 24 and 0xff
    var r2 = end shr 16 and 0xff
    var g2 = end shr 8 and 0xff
    var b2 = end and 0xff
    private fun getTextColor(pos: Int): Int {
        //关键是求得中间过度值 0-1
        var value = pos / 40.0

        val a3 = (a1 + (a2 - a1) * value).toInt()
        val r3 = (r1 + (r2 - r1) * value).toInt()
        val g3 = (g1 + (g2 - g1) * value).toInt()
        val b3 = (b1 + (b2 - b1) * value).toInt()

        val color = a3 and 0xff shl 24 or (r3 and 0xff shl 16) or (g3 and 0xff shl 8) or (b3 and 0xff)
        return color
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_MOVE, 500)
    }

    private fun getErrorWord(index: Int): Char {
        val position = index % ERROR_WORD_SIZE
        return ERROR_WORD[position]
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
