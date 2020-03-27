package com.example.game.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.game.R
import com.example.game.constant.BEAT_PRACTIVE_SPEED
import com.example.game.service.MusicService
import com.example.game.utils.SaveSpData
import kotlinx.android.synthetic.main.activity_beat_practice.*
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.sin


/**
 * 节拍器
 */
class BeatPracticeActivity : BaseActivity() {


    private lateinit var mBinder: MusicService.MyBinder

    private var mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as MusicService.MyBinder
        }
    }
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_CHANGE_S -> {
                    play(speed = beatSpeedView.getPercent().toInt())
                }
            }
        }
    }

    companion object {
        var isPlay = false
        const val MSG_CHANGE_S = 1
        fun start(ctx: Context) {
            Intent(ctx, BeatPracticeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beat_practice)
        initViewAndData()
        startService(Intent(applicationContext, MusicService::class.java))
        bindService(
            Intent(applicationContext, MusicService::class.java),
            mConnection,
            Context.BIND_AUTO_CREATE
        )

    }

    private fun initViewAndData() {
        setCenterTitle("济康-节拍器")

        var speed = SaveSpData.newInstance(this).getCommomIntData(BEAT_PRACTIVE_SPEED)
        if (speed == 0) {
            speed = 55
        }
        beatSpeedView.setPercent(speed.toFloat())
        showSpeed(speed)
        //还原未关闭的播放
        if (isPlay) {
            changeSpeed(speed)
        }

        tvPlay.setOnClickListener {
            isPlay = true
            changeSpeed(beatSpeedView.getPercent().toInt())
        }
        tvStop.setOnClickListener {
            isPlay = false
            cancelTimer()
//            stopService(Intent(this, MusicService::class.java))
//            unbindService(mConnection)
        }
        beatSpeedView.setCallBack {
            changeSpeed(it.toInt())
        }
        ivAddSpeed.setOnClickListener {
            beatSpeedView.addPercent(1)
        }
        ivSubpeed.setOnClickListener {
            beatSpeedView.subPercent(1)
        }

    }


    /**
     * @param speed 播放速度,越大间隔应该越小
     * 1-270
     */
    private fun play(speed: Int) {
        var radius = (speed) / 3.0
        if (radius > 90){
            radius=90.0
        }
//        var radius2 = sin(radius / 180.0 * Math.PI)*90

        //转换为 0.0-1.0 (0度-90度的sin值) 来计算节拍间隔, 斜率的变化 由最初数据向0.0-1.0映射时完成
        var range = sin(radius / 180.0 * Math.PI)

        var speedChange = -1800 * range + 2000
        println("hepans  $speedChange")
        if (MusicService.mTimer == null) {
            isPlay = true
            MusicService.mTimer = Timer().apply {
                var i = 0
                schedule(timerTask {

                    runOnUiThread {

                        when (i) {
                            0 -> {
                                mBinder.playMusic(1)
                                ivBeat1.isSelected = true
                                ivBeat2.isSelected = false
                                ivBeat3.isSelected = false
                                ivBeat4.isSelected = false
                            }
                            1 -> {
                                mBinder.playMusic(2)
                                ivBeat1.isSelected = false
                                ivBeat2.isSelected = true
                                ivBeat3.isSelected = false
                                ivBeat4.isSelected = false
                            }
                            2 -> {
                                mBinder.playMusic(2)
                                ivBeat1.isSelected = false
                                ivBeat2.isSelected = false
                                ivBeat3.isSelected = true
                                ivBeat4.isSelected = false
                            }
                            3 -> {
                                mBinder.playMusic(2)
                                ivBeat1.isSelected = false
                                ivBeat2.isSelected = false
                                ivBeat3.isSelected = false
                                ivBeat4.isSelected = true
                            }
                        }

                        i++
                        if (i >= 4) {
                            i = 0
                        }
                    }
                }, 1L, speedChange.toLong())
            }
        }
    }

    /**
     * 修改播放速度
     *
     */
    private fun changeSpeed(speed: Int) {
        cancelTimer()
        mHandler.removeMessages(MSG_CHANGE_S)
        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_S, 500)
        showSpeed(speed)
    }

    private fun showSpeed(speed: Int) {
        tvBeatSpeed.text = "${speed + 29}"
    }

    /**
     * 取消服务中 Timer
     */
    private fun cancelTimer() {
        if (MusicService.mTimer != null) {
            MusicService.mTimer!!.cancel()
            MusicService.mTimer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SaveSpData.newInstance(this)
            .saveCommonIntData(BEAT_PRACTIVE_SPEED, beatSpeedView.getPercent().toInt())

        if (!isPlay) {
            cancelTimer()
            stopService(Intent(this, MusicService::class.java))
            unbindService(mConnection)
        }
    }


}
