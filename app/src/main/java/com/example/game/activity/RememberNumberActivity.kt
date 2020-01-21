package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.core.text.isDigitsOnly
import com.example.game.R
import com.example.game.constant.SEARCH_REMEMBER_ACTIVITY
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
        const val MSG_SHOW_NUMBER = 3
        const val MSG_ADD_WAIT_TIME = 4
        const val MSG_FINISH_GAME = 5

        //一共五轮,正确,错误的选择都计算在内
        const val TOTAL_PLAY_TIME = 5
        //一轮中需要展示的数字数量
        const val ONE_TIME_COUNT = 8

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
    //当前显示内容的长度
    private var mShowNumberLength = 0
    //数字展示时长
    private var mShowTime = 0L
    //每轮展示的数据(每轮8个)
    private var mShowList = arrayListOf<String>()
    //当前展示的位置
    private var mShowPos = 0
    //完成的次数
    private var mPlayTime = 0
    private var mScore = 0
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START -> {
                    startGame()
                }
                MSG_SHOW_NUMBER -> {
                    if (mShowPos >= ONE_TIME_COUNT) {
                        showSelectView()
                        return
                    }
                    showNextNumber()
                }
                MSG_ADD_WAIT_TIME -> {
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
        stRememberNumber.setStep(TOTAL_PLAY_TIME, 0)
        setCenterTitle("济康-记忆数")

        mSpeed = intent.getIntExtra("speed", 0)
        mShowTime = (1800 - (100 * mSpeed)).toLong()

        //生成要展示的数据集合
        generateNumberList()

        mHandler.sendEmptyMessageDelayed(MSG_START, 1000)

    }


    private fun initListener() {
        showMultiNumberView.setCallback(object : ShowMultiNumberView.CallBack {
            override fun onSetCallBack(pos: Int, number: String) {
                handleItemClick(pos, number)
            }
        })
    }

    /**
     * 处理选择view的条目点击事件
     */
    private fun handleItemClick(pos: Int, number: String) {

        if (mShowList.contains(number)) {
//            showMultiNumberView.setRightStyle(pos)
            rightOrError.text = "正确"
            mScore += 50
            tvSNScore.text = "$mScore"
            stRememberNumber.addStep()
        } else {
            rightOrError.text = "错误"
        }
        generateNumberList()
        mPlayTime++
        showMultiNumberView.visibility = View.INVISIBLE
        if (mPlayTime == TOTAL_PLAY_TIME) {
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_GAME, 1000)
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_START, 1000)
        }
    }

    /**
     * 开始游戏
     */
    private fun startGame() {
        rightOrError.text = ""
        mShowPos = 0
        tvRememberNumber.text = mShowList[mShowPos++]
        //延时显示下一个
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_NUMBER, mShowTime)
    }

    /**
     * 展示下一个数字
     */
    private fun showNextNumber() {
        tvRememberNumber.text = mShowList[mShowPos++]
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_NUMBER, mShowTime)
    }

    /**
     * 展示选择view,重点是显示内容
     */
    private fun showSelectView() {
        mHandler.removeMessages(MSG_SHOW_NUMBER)
        tvRememberNumber.text = ""
        showMultiNumberView.visibility = View.VISIBLE
        val s = Random.nextInt(0, ONE_TIME_COUNT)
        val show = mShowList[s]

        val list = arrayListOf<String>()
        var count = 0
        while (count < 4) {
            val diff = getDiffNumber(show)
            if (!mShowList.contains(diff) && !list.contains(diff)) {
                list.add(diff)
                count++
            }
        }

//        list.shuffle() //不好用,大概率出现在固定位置

        //采用随机添加,然后移除的方法
        val index = Random.nextInt(0, 4)
        println("位置$index")
        list.add(index, show)
        list.removeAt(index + 1)
        showMultiNumberView.setShowNumber(list)
    }

    /**
     * 根据传入的字符串生成近似字符串
     */
    private fun getDiffNumber(src: String): String {
        var des = src
        val pos = Random.nextInt(0, src.length)
        des = src.replaceRange(pos, pos + 1, "${Random.nextInt(1, 9)}")
        return des
    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        val spKey = SEARCH_REMEMBER_ACTIVITY + mSpeed
        SearchRecordActivity.start(this, spKey, getScore(), mSpeed)
        finish()
    }

    private fun getScore(): Int {
        val text = tvSNScore.text.toString()
        return if (text.isDigitsOnly()) text.toInt() else 0
    }

    /**
     * 生成要展示的数字集合
     */
    private fun generateNumberList() {
        var count = 0
        mShowList.clear()
        while (count < ONE_TIME_COUNT) {
            val number = generateNum()
            if (!mShowList.contains(number)) {
                mShowList.add(number)
                count++
            }
        }
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
        return sb.toString()
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
