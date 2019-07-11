package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import com.example.game.R
import com.example.game.constant.SEARCH_ODD_EVEN_ACTIVITY
import com.example.game.util.screenWidth
import com.example.game.utils.StatusBarUtils
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_search_odd_even.*
import kotlin.random.Random


/**
 *搜索奇偶数
 */
class SearchOddEvenActivity : BaseActivity() {

    companion object {
        const val TOTAL_WORD_NUMBER = 80
        const val SEARCH_WORD_NUMBER = 8
        const val MSG_MOVE_HINT_ITEM = 1
        const val MSG_START_MOVE = 2
        const val MSG_TIME_COUT_DOWN = 3

        fun start(ctx: Context, speed: Int) {
            Intent(ctx, SearchOddEvenActivity::class.java).apply {
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
                MSG_MOVE_HINT_ITEM -> {

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
        mHandler.sendEmptyMessage(MSG_MOVE_HINT_ITEM)
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUT_DOWN, mCountDownTimeDelay)
    }

    private var mRemoveCount = 0
    private var mSpeed = 0
    //横线初始位置
    private var mStart = 0F
    private var mTvHeight = 0F
    private var mLineMoveDelayTime = 1000L
    private var mCountDownTimeDelay = 100L
    private var mSocre = 0
    private var mTotalTime = 90
    //是否搜索奇数
    private var isSearchOdd = true
    val padding = 4
    private val margin = screenWidth / 140
    val textSize = screenWidth / 58.0F
    //等待选择的数据
    private var mSearPositions = arrayListOf<Int>()
    private var mStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_odd_even)
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

        setCenterTitle("济康-奇偶数")
        circleStepView.setStep(SEARCH_WORD_NUMBER * 3)

        initGameView()

    }

    /**
     * 游戏开始前的初始化,可能需要多次重置
     */
    private fun initGameView() {
        isSearchOdd = !isSearchOdd
        if (isSearchOdd) {
            tvSearchWhat.text = "找到奇数"
        } else {
            tvSearchWhat.text = "找到偶数"
        }
        //添加view给flexbox
        flexBox.removeAllViews()
        for (num in 0..TOTAL_WORD_NUMBER) {
            val tv = TextView(this)
            tv.textSize = textSize
            tv.text = generateNumber(!isSearchOdd).toString()
            tv.setPadding(padding + 10, padding, padding + 10, padding)

            tv.setTextColor(resources.getColor(R.color.color_333333))
            flexBox.addView(tv)
            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.topMargin = margin
            }
        }

        //随机放入待选择的数字
        mSearPositions.clear()
        for (num in 0..SEARCH_WORD_NUMBER) {
            val random = generateNumber(isSearchOdd)
            var position = Random.nextInt(0, TOTAL_WORD_NUMBER)
            while (mSearPositions.contains(position)) {
                position = Random.nextInt(0, SEARCH_WORD_NUMBER)
            }
            mSearPositions.add(position)

            val tv = flexBox[position] as TextView
            tv.text = random.toString()
            tv.setOnClickListener {
                handleWordClick(position)
            }
        }
    }

    /**
     * 待选择的数字被正确点击
     * @param pos 被点击数字是第几个
     */
    private fun handleWordClick(pos: Int) {
        if (!mSearPositions.contains(pos)) return
        mSocre += 10
        tvScore.text = "$mSocre"
        val tv = flexBox[pos] as TextView
        tv.setBackgroundColor(Color.RED)
        tv.setTextColor(Color.WHITE)
        circleStepView.setStep(++mStep)
        mSearPositions.remove(pos)
        if (mSearPositions.isEmpty()) {
            //全部找完
            mRemoveCount = 0
            initGameView()
            mHandler.removeMessages(MSG_MOVE_HINT_ITEM)
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT_ITEM, 50L)
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
        val spKey = SEARCH_ODD_EVEN_ACTIVITY + mSpeed
        SearchRecordActivity.start(this, spKey, getScore(), mSpeed)
        finish()
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
     * 后去待选择的数字
     * @param generateOdd 是否需要奇数
     */
    private fun generateNumber(generateOdd: Boolean): Int {
        var res = Random.nextInt(1000000, 9999999)
        if (generateOdd && res % 2 == 0) {
            res -= 1
        }
        if (!generateOdd && res % 2 != 0) {
            res -= 1
        }

        return res
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}
