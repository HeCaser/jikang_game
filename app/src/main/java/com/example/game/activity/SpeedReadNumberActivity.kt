package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import com.example.game.R
import com.example.game.constant.SEARCH_SPEED_NUMBER_ACTIVITY
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_speed_read_number.*
import kotlin.random.Random


/**
 *速读数
 *
 */
class SpeedReadNumberActivity : BaseActivity() {

    companion object {
        const val MSG_START = 1
        const val MSG_DISSMISS_NUMNER = 2
        const val MSG_SHOW_NUMBER = 3
        const val MSG_ADD_WAIT_TIME = 4
        const val MSG_FINISH_GAME = 5

        //所需正确选择数量,到达则游戏结束
        const val TOTAL_RIGHT_NUM = 10
        //等待时间自增间隔
        const val WAIT_TIEM_ADD_JIANGE = 600L

        fun start(ctx: Context, speed: Int) {
            Intent(ctx, SpeedReadNumberActivity::class.java).apply {
                putExtra("speed", speed)
                ctx.startActivity(this)
            }
        }
    }

    //选择的星标值, 越大数字长度越大,展示时间越短 范围 1-10
    private var mSpeed = 0
    //现在显示的内容
    private var mShowNumber = ""
    //当前显示内容的长度
    private var mShowNumberLength = 0
    //当前已正确输入的个数
    private var mRightNum = 0
    //数字展示时长
    private var mShowTime = 0L
    //输入等待时长
    private var mWaitTime = 0

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_DISSMISS_NUMNER -> {
                    dismissNumber()
                }
                MSG_START -> {
                    startGame()
                }
                MSG_SHOW_NUMBER -> {
                    showNextNumber()
                }
                MSG_ADD_WAIT_TIME -> {
                    mWaitTime++
                    sendEmptyMessageDelayed(MSG_ADD_WAIT_TIME, WAIT_TIEM_ADD_JIANGE)
                }
                MSG_FINISH_GAME -> {
                    finisGame()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_read_number)
        initViewAndData()
        initListener()

    }

    private fun initListener() {

        etInputRes.doAfterTextChanged {
            handleUserInput()
        }
    }


    private fun initViewAndData() {
        stSpeedNumber.setStep(TOTAL_RIGHT_NUM + 1, 0)
        hideInput()

        mSpeed = intent.getIntExtra("speed", 0)
        mShowTime = (1800 - (100 * mSpeed)).toLong()
        mHandler.sendEmptyMessageDelayed(MSG_START, 1000)

    }


    /**
     * 开始游戏
     */
    private fun startGame() {
        showNumber(false)
        //延时隐藏显示
        mHandler.sendEmptyMessageDelayed(MSG_DISSMISS_NUMNER, mShowTime)
    }

    /**
     * 展示下一个数字
     */
    private fun showNextNumber() {
        hideInput()
        showNumber(Random.nextBoolean())
        //延时隐藏显示
        mHandler.sendEmptyMessageDelayed(MSG_DISSMISS_NUMNER, mShowTime)
    }

    /**
     * 显示数字
     */
    private fun showNumber(showCover: Boolean) {
        tvSpeedNumber.visibility = View.VISIBLE
        mShowNumber = generateNum()
        tvSpeedNumber.text = mShowNumber
        if (showCover) {
            tvNumberCover.visibility = View.VISIBLE
        } else {
            tvNumberCover.visibility = View.INVISIBLE
        }
    }

    /**
     * 隐藏数字
     */
    private fun dismissNumber() {
        tvNumberCover.visibility = View.INVISIBLE
        tvSpeedNumber.visibility = View.INVISIBLE
        showInput()
    }

    /**
     * 处理用户输入的内容
     */
    private fun handleUserInput() {
        val content = etInputRes.text.toString()
        if (content.length != mShowNumberLength) {
            return
        }
        if (content == mShowNumber) {
            //输入正确
            mHandler.removeMessages(MSG_ADD_WAIT_TIME)
            rightOrError.text = "正确"
            stSpeedNumber.setStep(++mRightNum)
            //计算分数
            countScore()

            if (mRightNum == TOTAL_RIGHT_NUM+1) {
                mHandler.sendEmptyMessageDelayed(MSG_FINISH_GAME,600)
                return
            }

        } else {
            //输入错误
            rightOrError.text = "错误"
        }
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_NUMBER, 1000)

    }

    /**
     *计算分数
     *
     */
    private fun countScore() {
        var nowScore = getScore()
        var targetScore = 0
        if (mWaitTime < 10) {
            //在规定时间内完成,有提分
            nowScore = nowScore + (12 - mWaitTime)
        }
        targetScore = nowScore + 7
        tvSNScore.text = "$targetScore"

    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        val spKey = SEARCH_SPEED_NUMBER_ACTIVITY + mSpeed
        SearchRecordActivity.start(this, spKey, getScore(), mSpeed)
        finish()
    }

    private fun getScore(): Int {
        val text = tvSNScore.text.toString()
        return if (text.isDigitsOnly()) text.toInt() else 0
    }

    /**
     * 生成要显示的数字
     */
    private fun generateNum(): String {
        //确定数字长度
        val time = mSpeed / 2
        mShowNumberLength = 2 + time

        if (mShowNumberLength < 3) {
            mShowNumberLength = 3
        }
        val sb = StringBuffer()
        var random: Int
        var index = 0
        while (index < mShowNumberLength) {
            random = Random.nextInt(0, 10)
            //首位数字不为0
            if (random == 0 && index == 0) {
                continue
            }
            sb.append(random)
            index++
        }
        val filters = arrayOf<InputFilter>(LengthFilter(mShowNumberLength))
        etInputRes.filters = filters
        return sb.toString()
    }


    private fun showInput() {
        //等待时长开始计算
        mWaitTime = 0
        mHandler.sendEmptyMessageDelayed(MSG_ADD_WAIT_TIME, WAIT_TIEM_ADD_JIANGE)

        etInputRes.setText("")
        etInputRes.visibility = View.VISIBLE
        viewEtLine.visibility = View.VISIBLE
        etInputRes.requestFocus()
        showSoftInput()
    }

    private fun hideInput() {
        mHandler.removeMessages(MSG_ADD_WAIT_TIME)
        rightOrError.text = ""
        etInputRes.visibility = View.INVISIBLE
        viewEtLine.visibility = View.INVISIBLE
        hideSoftInput()
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
