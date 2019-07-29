package com.example.game.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.game.R
import com.example.game.constant.EBOOK_TREE
import com.example.game.constant.SEARCH_TYPE_JIOU
import com.example.game.constant.SEARCH_TYPE_NUMBER
import kotlinx.android.synthetic.main.activity_before_ebook.*

class BeforeEBookActivity : AppCompatActivity() {

    private var mGameType = -1

    companion object {
        fun start(ctx: Context, type: Int) {
            Intent(ctx, BeforeEBookActivity::class.java).apply {
                putExtra("type", type)
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_ebook)
        mGameType = intent.getIntExtra("type", 1)
        initViewAndData()
        initListener()
    }


    private fun initViewAndData() {
        starLevel.setLevel(10)
        when (mGameType) {
            EBOOK_TREE -> {
                tvSearchName.text = "书籍/树形"
                tvHowUse.text = "目视行线阅读文本"
                tvFunction2.visibility = View.INVISIBLE
                tvFunction3.visibility = View.INVISIBLE
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
            EBOOK_TREE -> {
                EBookTreeActivity.start(this)
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
