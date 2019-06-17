package com.example.game.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.game.R
import com.example.game.util.sp
import com.example.game.utils.DateUtil
import com.example.game.utils.SaveSpData
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_score_record.*
import kotlinx.android.synthetic.main.toolbar_view.*

/**
 * 成绩展示act
 * key 用于获取历史记录的key
 * record 记录本次耗时(毫秒)
 * 最好成绩会自动保存覆盖
 */
class ScoreRecordActivity : BaseActivity() {

    companion object {
        fun start(ctx: Context, key: String, record: Long) {
            Intent(ctx, ScoreRecordActivity::class.java).apply {
                putExtra("key", key)
                putExtra("record", record)
                ctx.startActivity(this)
            }
        }
    }

    private var mRecord = 0L
    private var mKey = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_record)
        initViewAndData()
        initListener()
    }

    private fun initListener() {

    }

    private fun initViewAndData() {
        mKey = intent.getStringExtra("key")
        mRecord = intent.getLongExtra("record", 0)
    }

    override fun onResume() {
        super.onResume()
        showRecord()
    }

    private fun showRecord() {
        var history = SaveSpData.newInstance(this).getCommomLongData(mKey)
        var text = ""
        if (history == 0L || mRecord <= history) {
            SaveSpData.newInstance(this).saveCommonLongData(mKey, mRecord)
            text = "真厉害,您创造了新的记录!\n用时${DateUtil.getMinuteAndSecondByMillisecond(mRecord)}"
        } else {
            text = "最好成绩:${DateUtil.getMinuteAndSecondByMillisecond(history)}\n" +
                    "您的成绩${DateUtil.getMinuteAndSecondByMillisecond(mRecord)}"
        }
        tvRecord.text = text
    }
}
