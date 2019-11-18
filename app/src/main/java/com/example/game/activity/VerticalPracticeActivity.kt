package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.game.R
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_vertical_practice.*

/**
 * 垂直练习
 */
class VerticalPracticeActivity : BaseActivity() {

    companion object {
        const val MSG_INIT_VIEW = 1
        fun start(ctx: Context) {
            Intent(ctx, VerticalPracticeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_INIT_VIEW->{

                }
            }
        }
    }

    var mIconColor = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_practice)
        initViewAndData()
        initListener()
    }

    private fun initListener() {
        mIconColor = resources.getColor(R.color.color_31c2ff)
        tvVerticalAdd.background.setColorFilter(mIconColor, PorterDuff.Mode.SRC_ATOP)
        tvVerticalSub.background.setColorFilter(mIconColor, PorterDuff.Mode.SRC_ATOP)
    }

    private fun initViewAndData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        //还原对drawable资源的处理
        tvVerticalAdd.background.setColorFilter(mIconColor, PorterDuff.Mode.DST)
        tvVerticalSub.background.setColorFilter(mIconColor, PorterDuff.Mode.DST)
    }

}
