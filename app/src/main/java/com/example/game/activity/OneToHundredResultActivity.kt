package com.example.game.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.game.R
import com.example.game.util.sp
import com.example.game.utils.DateUtil
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_one_to_hundred_result.*

class OneToHundredResultActivity : BaseActivity() {

    companion object {
        fun start(ctx: Context) {
            Intent(ctx, OneToHundredResultActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_to_hundred_result)
        initViewAndData()
        initListener()
    }

    private fun initListener() {

    }

    private fun initViewAndData() {
        tvRecord.text =
            "分数: ${sp.oneToHundredRecord}\n用时: ${DateUtil.getMinuteAndSecondByMillisecond(sp.oneToHundredTime)}"
    }
}
