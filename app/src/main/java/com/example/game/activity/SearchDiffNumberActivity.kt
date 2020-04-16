package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import com.example.game.R
import com.example.game.constant.SEARCH_DIFF_NUMBER_ACTIVITY
import com.example.game.util.screenWidth
import com.example.game.utils.ScreenUtils
import com.example.game.utils.StatusBarUtils
import com.example.game.widget.DiffNumberView
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_search_odd_even.*
import kotlin.random.Random


/**
 *搜索 差异数字
 */
class SearchDiffNumberActivity : BaseActivity() {

    companion object {
        var TOTAL_WORD_NUMBER = 10
        var NUMBER_LENGTH = 3 //数字的长度

        var DIFF_NUMBER = Random.nextInt(3,5)
        const val MSG_MOVE_HINT = 1//移动指示背景(样式改变)
        const val MSG_START_GAME = 2//开始游戏
        const val MSG_TIME_COUNT_DOWN = 3//倒计时
        const val MSG_GLINT = 4 //提示闪烁
        const val MSG_INITVIEW = 5 //初始化view

        fun start(ctx: Context, speed: Int) {
            Intent(ctx, SearchDiffNumberActivity::class.java).apply {
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

                MSG_INITVIEW -> {
                    initGameView()
                }
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
                    if (mSearPositions.size == DIFF_NUMBER) {
                        var pos = mSearPositions[0]
                        var tv = flexBox[pos] as DiffNumberView
                        var isSelect = tv.isSelected
                        if (isSelect) {
                            tv.setBg(DiffNumberView.BG_SELECTED)
                        } else {
                            tv.setBg(DiffNumberView.BG_NORMAL)
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

    private var mSpeed = 0
    //横线初始位置
    private var mLineMoveDelayTime = 1000L
    private var mCountDownTimeDelay = 100L
    private var mGlintDelay = 500L
    private var mSocre = 0
    private var mTotalTime = 90
    private var mBgColor = 0
    val textSize = screenWidth / 28.0F
    //等待选择的数据
    private var mSearPositions = arrayListOf<Int>()
    private var mStep = 0
    private var mHintPosition = 0
    private var isGameStart  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_diff_number)
        initViewAndData()
        initListener()
    }

    private fun initListener() {

    }

    private fun initViewAndData() {
        mSpeed = intent.getIntExtra("speed", 1)
        //根据速度等级 1-10 设置移动线段的间隔 等级越高,间隔越短
        mLineMoveDelayTime = 600 - (mSpeed * 40).toLong()
        NUMBER_LENGTH = 3 + (mSpeed / 2)
        mBgColor = resources.getColor(R.color.color_ff8e00)
        //计算pb的最大值, 倒计时共90s
        mTotalTime = 900 * mCountDownTimeDelay.toInt()
        progressBar.max = mTotalTime
        progressBar.progress = mTotalTime

        setCenterTitle("济康-差异/数字")

        //设置步进总数,根据速度设置
        circleStepView.setStep(mSpeed * 3 + 10, 0)


    }

    /**
     * 游戏开始前的初始化
     */
    private fun initGameView() {
        if (flexBox.childCount != 0) return
        mHintPosition = 0

        val parentH = flexBox.measuredHeight
        if (parentH == 0) {
            //布局未加载好,延时再次调用
            mHandler.sendEmptyMessageDelayed(MSG_INITVIEW, 200)
            return
        }

        addViewToFlexBox()

        //开启闪烁提示
        mHandler.sendEmptyMessageDelayed(MSG_GLINT, mGlintDelay)
    }

    /**
     * 添加view
     */
    private fun addViewToFlexBox() {
        val parentH = flexBox.measuredHeight

        val itemHeight = ScreenUtils.dip2px(this, 90f)//条目的高度
        val margin = ScreenUtils.dip2px(this, 10f)
        val itemWidth = (flexBox.measuredWidth - 6 * margin) / 4//条目的宽度,固定一行四个
        val rowNumber = (parentH - 50) / (itemHeight + margin) //行数= 总高度/(条目高度+顶部间距)

        TOTAL_WORD_NUMBER = rowNumber * 4

        //添加view给flexbox
        flexBox.removeAllViews()
        for (num in 0 until TOTAL_WORD_NUMBER) {
            val diffView = DiffNumberView(this)
            diffView.setTextNumber(NUMBER_LENGTH, false)
            flexBox.addView(diffView, itemWidth, itemHeight)
            val para = diffView.layoutParams
            if (para is FlexboxLayout.LayoutParams) {
                para.bottomMargin = margin
                para.rightMargin = margin
                if (num % 4 == 0) {
                    para.leftMargin = margin
                }
                if (num <= 3) {
                    para.topMargin = margin
                }
            }
        }

        //随机放入待选择的数字
        mSearPositions.clear()
        for (num in 0 until DIFF_NUMBER) {
            var position = Random.nextInt(0, TOTAL_WORD_NUMBER)
            while (mSearPositions.contains(position)) {
                position = Random.nextInt(0, TOTAL_WORD_NUMBER)
            }
            println("hepan 位置=" + position)
            mSearPositions.add(position)
    //
            val diff = flexBox[position] as DiffNumberView
            diff.setTextNumber(NUMBER_LENGTH, true)
            diff.setOnClickListener {
                handleWordClick(position)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_INITVIEW, 200)

    }

    /**
     * 待选择的数字被正确点击
     * @param pos 被点击数字是第几个
     */
    private fun handleWordClick(pos: Int) {
        if (!mSearPositions.contains(pos)) return
        mSocre += 10
        tvScore.text = "$mSocre"
        if (!isGameStart) {
            //第一次点击正确,开始游戏
            isGameStart=true
            mHandler.sendEmptyMessage(MSG_START_GAME)
        }
        val diffView = flexBox[pos] as DiffNumberView
        diffView.setBg(DiffNumberView.BG_SELECTED)
        diffView.isChoosed=true

        circleStepView.setStep(++mStep)
        mSearPositions.remove(pos)


        if (mSearPositions.isEmpty()) {
            //全部找完,开始新的一页
            addViewToFlexBox()
            mHintPosition=0
            mHandler.removeMessages(MSG_MOVE_HINT)
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT,1000)
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
        if (mHintPosition >= TOTAL_WORD_NUMBER){
            return
        }
        val diffView = flexBox[mHintPosition] as DiffNumberView
        if (diffView.isChoosed){
            // 已选择的条目不提示
            if (mHintPosition != 0) {
                val tvBefore = flexBox[mHintPosition - 1] as DiffNumberView
                tvBefore.setBg(DiffNumberView.BG_NORMAL)
            }
            mHintPosition++
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, 0)
            return
        }
        diffView.setBg(DiffNumberView.BG_BLUE_HINT)
        if (mHintPosition != 0) {
            val tvBefore = flexBox[mHintPosition - 1] as DiffNumberView
            tvBefore.setBg(DiffNumberView.BG_NORMAL)
        }
        mHintPosition++
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_HINT, mLineMoveDelayTime)
    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        val spKey = SEARCH_DIFF_NUMBER_ACTIVITY + mSpeed
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
