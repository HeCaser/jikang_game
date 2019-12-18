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
import com.example.game.R
import com.example.game.constant.SEARCH_SPEED_NUMBER_ACTIVITY
import com.example.game.utils.StatusBarUtils
import com.example.game.widget.ShowMultiNumberView
import kotlinx.android.synthetic.main.activity_remember_number.*
import kotlin.random.Random


/**
 *记忆数
 *
 */
class RememberNumberActivity : BaseActivity() {

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
            Intent(ctx, RememberNumberActivity::class.java).apply {
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
        setContentView(R.layout.activity_remember_number)
        initViewAndData()
        initListener()

    }

    private fun initViewAndData() {
        stRememberNumber.setStep(TOTAL_RIGHT_NUM + 1, 0)
        hideInput()

        mSpeed = intent.getIntExtra("speed", 0)
        mShowTime = (1800 - (100 * mSpeed)).toLong()
        mHandler.sendEmptyMessageDelayed(MSG_START, 1000)

    }

    private fun initListener() {
        showMultiNumberView.setCallback(object : ShowMultiNumberView.CallBack{
            override fun onSetCallBack(pos: Int, number: String) {
                handleItemClick(pos,number)
            }
        })
    }

    private fun handleItemClick(pos: Int, number: String) {
        if (mShowNumber == number){

        }else{
            showMultiNumberView.setErrorStyle(pos)
        }
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
        tvRememberNumber.visibility = View.VISIBLE
        mShowNumber = generateNum()
        tvRememberNumber.text = mShowNumber
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
        tvRememberNumber.visibility = View.INVISIBLE
        showInput()
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
        return sb.toString()
    }


    private fun showInput() {
        //等待时长开始计算
        mWaitTime = 0
        mHandler.sendEmptyMessageDelayed(MSG_ADD_WAIT_TIME, WAIT_TIEM_ADD_JIANGE)

        showSoftInput()
    }

    private fun hideInput() {
        mHandler.removeMessages(MSG_ADD_WAIT_TIME)
        rightOrError.text = ""
        hideSoftInput()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
