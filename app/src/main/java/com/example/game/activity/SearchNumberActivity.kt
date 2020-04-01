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
import com.example.game.constant.SEARCH_NUMBER_ACTIVITY
import com.example.game.util.screenWidth
import com.example.game.utils.StatusBarUtils
import com.example.game.utils.setLetterSpacingText
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_search_word.*
import kotlin.random.Random


/**
 *搜索数字
 */
class SearchNumberActivity : BaseActivity() {

    companion object {
        const val TOTAL_WORD_NUMBER = 133
        const val MSG_MOVE_LINE = 1
        const val MSG_START_MOVE = 2
        const val MSG_TIME_COUT_DOWN = 3

        fun start(ctx: Context, speed: Int) {
            Intent(ctx, SearchNumberActivity::class.java).apply {
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
    private var mSpeed = 0
    //横线初始位置
    private var mStart = 0F
    private var mTvHeight = 0F
    private var mLineMoveDelayTime = 1000L
    private var mCountDownTimeDelay = 100L
    private var mShowView = arrayListOf<TextView>()
    private var mSocre = 0
    private var mTotalTime = 90
    private var randomSeed = 1
//    val padding = screenWidth / 260
    val padding = 2
    val margin = screenWidth / 140
    val textSize = screenWidth / 28.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_number)
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

        setCenterTitle("济康-搜索数")
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
        //设置需要找出数字
        randomSeed = Random.nextInt(1, 9)
        var rText = "$randomSeed$randomSeed$randomSeed$randomSeed"
        for (num in 0..3) {
            with(mShowView[num]) {
                text = rText
                isSelected = false
            }
        }

        //添加view给flexbox
        flexBox.removeAllViews()
        for (num in 0..TOTAL_WORD_NUMBER) {
            val tv = TextView(this)
            tv.textSize = textSize
            tv.setLetterSpacingText(getErrorWord())
            tv.setPadding(padding , padding, padding , padding)

            tv.setTextColor(getTextColor(num))
            flexBox.addView(tv)
            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.topMargin = margin
            }
        }

        //随机放入待选择的数字,四个
        val q = TOTAL_WORD_NUMBER / 4
        for (num in 0..3) {
            //每1/4区域,计算一个位置,放置待选择的数字
            var random = Random.nextInt(q * num, q * (num + 1))
            while (random >= TOTAL_WORD_NUMBER || random == 0) {
                random = Random.nextInt(q * (num + 1))
            }
            val tv = flexBox[random] as TextView
            tv.setOnClickListener {
                handleWordClick(num)
            }
            tv.setLetterSpacingText(rText)
        }
    }

    /**
     * 待选择的数字被正确点击
     * @param num 被点击数字是第几个 从0开始
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
        val spKey = SEARCH_NUMBER_ACTIVITY + mSpeed
        SearchRecordActivity.start(this, spKey, getScore(), mSpeed)
        finish()
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
    private fun getScore(): Int {
        val text = tvScore.text.toString()
        return if (text.isDigitsOnly()) text.toInt() else 0
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_MOVE, 500)
    }

    /**
     * 获取非正常数字
     */
    private fun getErrorWord(): String {
        var res = (Random.nextInt(1, 999) + randomSeed * 1000).toString()
        //如果出现相同的四位数就-1 防止和目标数相同
        if (res.matches(Regex("([0-9])\\1{3}"))) {
            res = (res.toInt() - 1).toString()
        }
        return res
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}
