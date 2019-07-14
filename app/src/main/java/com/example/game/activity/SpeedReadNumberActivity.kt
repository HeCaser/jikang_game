package com.example.game.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.game.R
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_speed_read_number.*
import androidx.core.text.isDigitsOnly as isDigitsOnly1


/**
 *速读数
 */
class SpeedReadNumberActivity : BaseActivity() {

    companion object {
        const val MSG_MOVE_LINE = 1
        const val MSG_START_MOVE = 2

        fun start(ctx: Context, speed: Int) {
            Intent(ctx, SpeedReadNumberActivity::class.java).apply {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_read_number)
        initViewAndData()
        initListener()
    }

    private fun initListener() {
        tvSpeedNumber.setOnClickListener {
            hideCover()
        }
        etInputRes.setOnClickListener {
            showCover()
        }
    }

    private fun initViewAndData() {

        initGameView()
    }

    /**
     * 游戏开始前的初始化,可能需要多次重置
     */
    private fun initGameView() {
        //设置需要找出的词 四个

    }

    private fun hideCover() {
        val anim = ObjectAnimator.ofFloat(tvNumberCover, "alpha", 1.0f, .0f)
        anim.duration = 800
        tvNumberCover.alpha
        anim.start()
    }

    private fun showCover() {
        tvNumberCover.alpha = 1.0f
    }


    /**
     * 结束游戏
     */
    private fun finisGame() {


    }


//    private fun getScore(): Int {
//        val text = tvScore.text.toString()
//        return if (text.isDigitsOnly1()) text.toInt() else 0
//
//    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
