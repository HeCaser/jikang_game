package com.example.game.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.game.R
import com.example.game.service.MusicService
import kotlinx.android.synthetic.main.activity_beat_practice.*
import java.util.*
import kotlin.concurrent.timerTask


/**
 * 节拍器
 */
class BeatPracticeActivity : BaseActivity() {


    private var isCanClick = false
    private lateinit var mBinder: MusicService.MyBinder

    private var mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as MusicService.MyBinder
        }
    }

    val music = arrayListOf(
        R.raw.tone1,
        R.raw.tone1_1,
        R.raw.tone2,
        R.raw.tone2_1
    )

    val mLoadId = arrayListOf(0)

    companion object {
        const val MUSIC_MAX_SIZE = 4
        const val MSG_START = 1
        const val MSG_NEXT_TURN = 2
        const val MSG_CAN_CLICK = 3
        fun start(ctx: Context) {
            Intent(ctx, BeatPracticeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START -> {
                    play(1)
                    sendEmptyMessageDelayed(MSG_START, 5000)
                }
                MSG_NEXT_TURN -> {
                }
                MSG_CAN_CLICK -> {
                    isCanClick = true
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beat_practice)
        initViewAndData()
        startService(Intent(this, MusicService::class.java))
        bindService(Intent(this, MusicService::class.java), mConnection, Context.BIND_AUTO_CREATE)

    }

    private fun initViewAndData() {
        setCenterTitle("济康-节拍器")

        var speed = 1
        tvPlay.setOnClickListener {
            //                        mHandler.sendEmptyMessageDelayed(MSG_START,200)

            changeSpeed(speed++)
//            mBinder.playMusic(1)

//            testMusic()
        }
    }


    private fun play(speed: Int) {
        val i = (speed * 500.0f).toInt()
        var j = i
        if (i < 80) {
            j = 80
        }
        if (MusicService.mTimer == null) {
            MusicService.mTimer = Timer().apply {
                var i = 4
                schedule(timerTask {
                    println("hepan$i")
                    runOnUiThread {

                    }
                    if (i % 4 == 0) {
                        mBinder.playMusic(1)
                    } else {
                        mBinder.playMusic(2)
                    }
                    i++
                }, 1L, j * 1L)
            }
        }
    }

    /**
     * 修改了播放速度
     */
    private fun changeSpeed(speed:Int){
        cancelTimer()
        play(speed =speed )
    }
    /**
     * 取消服务中 Timer
     */
    private fun cancelTimer() {
        if (MusicService.mTimer != null) {
            MusicService.mTimer!!.cancel()
            MusicService.mTimer=null
        }
    }

    //生成所有数据
    private fun generateAdapterData() {

    }

    private fun testMusic(){
        var play = MediaPlayer.create(this,R.raw.tone1)
        play.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinder.stopPlay()
        cancelTimer()
    }


}
