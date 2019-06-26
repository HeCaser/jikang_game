package com.example.game.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.game.R
import com.example.game.utils.SaveSpData
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_search_record.*

/**
 * 成绩展示act
 * key 用于获取历史记录的key
 * record 本次成绩
 * 最好成绩会自动保存覆盖
 */
class SearchRecordActivity : BaseActivity() {

    companion object {
        fun start(ctx: Context, key: String, thisScore: Int, speed: Int) {
            Intent(ctx, SearchRecordActivity::class.java).apply {
                putExtra("key", key)
                putExtra("thisScore", thisScore)
                putExtra("speed", speed)
                ctx.startActivity(this)
            }
        }
    }

    private var mThisScore = 0
    private var mSpeed = 0
    private var mKey = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_record)
        initViewAndData()
        initListener()
    }

    private fun initListener() {

    }

    private fun initViewAndData() {
        mKey = intent.getStringExtra("key")
        mThisScore = intent.getIntExtra("thisScore", 0)
        mSpeed = intent.getIntExtra("speed", 0)

        starView.setLevel(mSpeed)
    }

    override fun onResume() {
        super.onResume()
        showRecord()
    }

    private fun showRecord() {
        var history = SaveSpData.newInstance(this).getCommomIntData(mKey)
        var text = ""
        if (history == 0 || mThisScore >= history) {
            SaveSpData.newInstance(this).saveCommonIntData(mKey, mThisScore)
            text = "真厉害,您创造了新的记录!\n分数为${mThisScore}"
        } else {
            text = "最好成绩:$history\n" +
                    "您的成绩$mThisScore"
        }
        tvRecord.text = text
    }
}
