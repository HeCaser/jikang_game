package com.example.game.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.game.R
import com.example.game.constant.SEARCH_TYPE_JIOU
import com.example.game.constant.SEARCH_TYPE_NUMBER
import com.example.game.constant.SEARCH_TYPE_WORD
import kotlinx.android.synthetic.main.activity_before_search.*

class BeforeSearchActivity : AppCompatActivity() {

    private var mGameType = -1

    companion object {
        fun start(ctx: Context, type: Int) {
            Intent(ctx, BeforeSearchActivity::class.java).apply {
                putExtra("type", type)
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_search)
        mGameType = intent.getIntExtra("type", 1)
        initViewAndData()
        initListener()
    }


    private fun initViewAndData() {
        when (mGameType) {
            SEARCH_TYPE_WORD -> {
                tvSearchName.text = "搜索/词"
            }
            SEARCH_TYPE_NUMBER -> {
                tvSearchName.text = "搜索/数"
            }
            SEARCH_TYPE_JIOU -> {
                tvSearchName.text = "搜索/奇偶数"
            }
        }
    }

    private fun initListener() {
        ivAdd.setOnClickListener {
            starLevel.increaseLevel()
        }

        ivSub.setOnClickListener {
            starLevel.subLevel()
        }

        btnStart.setOnClickListener {
            goSearchGame()
        }
    }

    /**
     * 跳转到具体的搜索游戏
     */
    private fun goSearchGame() {
        val speed = starLevel.getLevel()
        when (mGameType) {
            SEARCH_TYPE_WORD -> {
                SearchWordActivity.start(this, speed)
            }
            SEARCH_TYPE_NUMBER -> {
                SearchNumberActivity.start(this, speed)
            }
            SEARCH_TYPE_JIOU -> {
                SearchOddEvenActivity.start(this, speed)
            }
        }
        finish()
    }
}
