package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.TypedValue
import android.view.Gravity
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
class SearchOddEvenActivity : BaseActivity(), ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        const val SEARCH_WORD_NUMBER = 6

        const val MSG_MOVE_HINT = 1//移动指示背景(样式改变)
        const val MSG_START_GAME = 2//开始游戏
        const val MSG_TIME_COUNT_DOWN = 3//倒计时
        const val MSG_GLINT = 4 //提示闪烁

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
                MSG_MOVE_HINT -> {
                    moveHint()
                }
                MSG_START_GAME -> {
                    startGame()
                }
                MSG_TIME_COUNT_DOWN -> {
                    handTimeCount()
                }
                MSG_GLINT -> {
                    if (mSearPositions.size == SEARCH_WORD_NUMBER) {
                        var pos = mSearPositions[0]
                        var tv = flexBox[pos] as TextView
                        var isSelect = tv.isSelected
                        if (isSelect) {
                            tv.setBackgroundColor(mBgColor)
                        } else {
                            tv.setBackgroundColor(Color.WHITE)
                        }
                        tv.isSelected = !isSelect
                        this.sendEmptyMessageDelayed(MSG_GLINT, mGlintDelay)
                    }
                }
            }
        }
    }


    /**
     * 设置view 开始游戏
     */
    private fun startGame() {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUNT_DOWN, mCountDownTimeDelay)
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, 0)
    }

    private var mRemoveCount = 0
    private var mSpeed = 0
    private var mLineMoveDelayTime = 1000L
    private var mCountDownTimeDelay = 100L
    private var mGlintDelay = 500L
    private var mSocre = 0
    private var mTotalTime = 90
    private var mBgColor = 0
    //是否搜索奇数
    private var isSearchOdd = true
    val padding = 4
    val textSize = screenWidth / 28.0F
    //等待选择的数据
    private var mSearPositions = arrayListOf<Int>()
    private var mStep = 0
    private var mHintPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_odd_even)
        initViewAndData()
        initListener()
    }

    private fun initListener() {
        flexBox.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        flexBox.viewTreeObserver.removeOnGlobalLayoutListener(this)
        initGameView()
    }

    private fun initViewAndData() {
        mSpeed = intent.getIntExtra("speed", 1)
        //根据速度等级 1-10 设置移动线段的间隔 等级越高,间隔越短
        mLineMoveDelayTime = 600 - (mSpeed * 40).toLong()
        mBgColor = resources.getColor(R.color.color_ff8e00)
        //计算pb的最大值, 倒计时共90s
        mTotalTime = 900 * mCountDownTimeDelay.toInt()
        progressBar.max = mTotalTime
        progressBar.progress = mTotalTime

        setCenterTitle("济康-奇偶数")

        //设置步进总数,根据速度设置
        circleStepView.setStep(mSpeed * 3 + 10, 0)


    }

    /**
     * 游戏开始前的初始化,可能需要多次重置
     */
    private var totalNumber = 0

    private fun initGameView() {
        isSearchOdd = !isSearchOdd
        if (isSearchOdd) {
            tvSearchWhat.text = "找到奇数"
        } else {
            tvSearchWhat.text = "找到偶数"
        }
        mHintPosition = 0

        //添加view给flexbox
        flexBox.removeAllViews()
        //父控件尺寸决定子 View 数量
        val flexWidth = flexBox.measuredWidth
        val flexHeight = flexBox.measuredHeight
        val columnNumber = 5 //默认列数
        var rowNumber: Int // 行数根据行高计算

        val itemWidth = flexWidth / columnNumber //宽度/列 = 条目宽度
        val itemHeight = (itemWidth / 3).toInt() //行高为宽度一半
        rowNumber = flexHeight / itemHeight
        rowNumber -= 1 //减少一行,避免只显示一半的数据


        totalNumber = columnNumber * rowNumber

        for (num in 0 until totalNumber) {
            val tv = TextView(this)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (itemWidth / 5.0).toFloat())
            tv.text = generateNumber(!isSearchOdd).toString()
            tv.gravity=Gravity.CENTER
            tv.setTextColor(resources.getColor(R.color.color_333333))

            flexBox.addView(tv,(itemWidth*0.9).toInt(),1) ////宽度有效,高度无效?

            val para = tv.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.topMargin = 10
                para.leftMargin = itemHeight*0.1.toInt()  //上面宽度设置为0.9  margin为0.1
            }
        }

        //随机放入待选择的数字
        mSearPositions.clear()
        for (num in 0 until SEARCH_WORD_NUMBER) {
            val random = generateNumber(isSearchOdd)
            var position = Random.nextInt(0, totalNumber)
            while (mSearPositions.contains(position)) {
                position = Random.nextInt(0, totalNumber)
            }
            mSearPositions.add(position)

            val tv = flexBox[position] as TextView
            tv.text = random.toString()
            tv.setOnClickListener {
                handleWordClick(position)
            }
        }

        //开启闪烁提示
        mHandler.sendEmptyMessageDelayed(MSG_GLINT, mGlintDelay)
    }

    /**
     * 待选择的数字被正确点击
     * @param pos 被点击数字是第几个
     */
    private fun handleWordClick(pos: Int) {
        if (!mSearPositions.contains(pos)) return
        mSocre += 10
        tvScore.text = "$mSocre"
        if (mSearPositions.size == SEARCH_WORD_NUMBER) {
            //第一次点击正确,开始游戏
            var pos = mSearPositions[0]
            flexBox[pos].setBackgroundColor(Color.WHITE)
            mHandler.sendEmptyMessage(MSG_START_GAME)
        }
        val tv = flexBox[pos] as TextView
        tv.setBackgroundColor(mBgColor)
        tv.setTextColor(Color.WHITE)

        circleStepView.setStep(++mStep)
        mSearPositions.remove(pos)


        if (mSearPositions.isEmpty()) {
            //全部找完
            mRemoveCount = 0
            initGameView()
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
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUNT_DOWN, mCountDownTimeDelay)
    }

    /**
     * 提示状态的移动,根据传入的速度来决定移动快慢
     */
    private fun moveHint() {
        if (mHintPosition >= totalNumber) return
        val tv = flexBox[mHintPosition] as TextView
        tv.setShadowLayer(2.0f, 1.0f, 1.0f, resources.getColor(R.color.colorPrimary))
        if (mHintPosition != 0) {
            val tvBefore = flexBox[mHintPosition - 1] as TextView
            tvBefore.setShadowLayer(.0f, .0f, .0f, Color.WHITE)
        }
        mHintPosition++
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, mLineMoveDelayTime)
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
