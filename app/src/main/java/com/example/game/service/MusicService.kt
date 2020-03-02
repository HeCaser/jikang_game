package com.example.game.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.example.game.R
import java.util.*
import kotlin.collections.HashMap


class MusicService : Service() {

    companion object {
        var mSoundPoool: SoundPool? = null
        //记录音乐id, key 为 1 2
        var mMusicList = HashMap<Int, Int>()
        var mCtx: Context? = null
        var mTimer: Timer? = null
    }


    override fun onCreate() {
        super.onCreate()
        mCtx = this
        println("hepan创建")
    }

    override fun onBind(intent: Intent): IBinder {
        return MyBinder()
    }


    class MyBinder : Binder() {
        fun playMusic(pos: Int) {
            if (mSoundPoool == null) {
                mSoundPoool = if (Build.VERSION.SDK_INT >= 21) {
                    SoundPool.Builder().setMaxStreams(3).build()
                } else {
                    SoundPool(1, AudioManager.STREAM_MUSIC, 1)
                }
                mMusicList[1] = mSoundPoool!!.load(mCtx!!.applicationContext, R.raw.tone1_1, 1)
                mMusicList[2] = mSoundPoool!!.load(mCtx!!.applicationContext, R.raw.tone1, 1)
            } else {
                mSoundPoool!!.play(mMusicList[pos]!!, 1f, 1f, 0, 0, 1f)
            }
        }

        fun stopPlay() {
            mSoundPoool?.apply {
                stop(mMusicList[1]!!)
                stop(mMusicList[2]!!)
            }
        }

        fun loadMusic() {
            mMusicList[1] = mSoundPoool!!.load(mCtx!!.applicationContext, R.raw.tone1, 1)
            mMusicList[2] = mSoundPoool!!.load(mCtx!!.applicationContext, R.raw.tone1_1, 1)
        }
    }
}