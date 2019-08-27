package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.game.R
import com.example.game.constant.*
import com.example.game.database.AppDatabase
import com.example.game.utils.SaveSpData
import com.example.game.utils.ToastUtils
import com.example.game.utils.gone
import com.example.game.viewmodel.ArticleLineViewModel
import com.example.game.viewmodel.ArticleLineViewModelFactory
import kotlinx.android.synthetic.main.activity_before_ebook.*

class BeforeEBookActivity : AppCompatActivity() {

    private var mGameType = -1

    companion object {
        const val SAVE_BOOK = 1

        fun start(ctx: Context, type: Int) {
            Intent(ctx, BeforeEBookActivity::class.java).apply {
                putExtra("type", type)
                ctx.startActivity(this)
            }
        }

    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                SAVE_BOOK -> {
                    saveBook()
                }
            }
        }
    }

    private val articleLineViewModel: ArticleLineViewModel by lazy {
        ViewModelProviders.of(
                this,
                ArticleLineViewModelFactory(AppDatabase.getInstance(this).articleDao())
        ).get(ArticleLineViewModel::class.java)
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
                tvSearchName.text = "EBook/树形"
                tvHowUse.text = "目视行线阅读文本"
                tvFunction2.visibility = View.INVISIBLE
                tvFunction3.visibility = View.INVISIBLE
            }
            EBOOK_LOOP -> {
                tvSearchName.text = "EBook/循环"
                tvHowUse.text = "目视红点阅读文本"
                tvFunction1.text = "减少注视间隔"
                tvFunction2.text = "增加视角跨度"
                tvFunction3.visibility = View.INVISIBLE
                //保存书籍
//                if (TextUtils.isEmpty(SaveSpData.newInstance(this).getCommomStringData(BOOK_QINGTANG_HEYUN))) {
//                    articleLineViewModel.saveBook(this, BOOK_QINGTANG_HEYUN)
//                }
            }
            EBOOK_SUBFIELD -> {
                tvSearchName.text = "EBook/分栏"
                tvHowUse.text = "目视行线阅读文本"
                tvFunction2.visibility = View.INVISIBLE
                tvFunction3.visibility = View.INVISIBLE
                //保存书籍
                if (TextUtils.isEmpty(SaveSpData.newInstance(this).getCommomStringData(BOOK_DAOCAOREN))) {
                    articleLineViewModel.saveBook(this, BOOK_DAOCAOREN)
                }
            }
        }
    }

    private fun initListener() {
        btnStart.setOnClickListener {
            goSearchGame()
        }

        articleLineViewModel.saveResult.observe(this, Observer {
            if (it == 1) {
                tvCover.gone()
                ToastUtils.show(this, "初始化书籍成功")

            } else {
                ToastUtils.show(this, "初始化书籍错误")
            }
        })
    }

    /**
     * 跳转到具体的搜索游戏
     */
    private fun goSearchGame() {
        when (mGameType) {
            EBOOK_TREE -> {
                EBookTreeActivity.start(this)
                finish()

            }
            EBOOK_LOOP -> {
                if (TextUtils.isEmpty(SaveSpData.newInstance(this).getCommomStringData(BOOK_QINGTANG_HEYUN))) {
                    ToastUtils.show(this, "初始化书籍,请稍等...")
                } else {
                    EBookLoopActivity.start(this, BOOK_QINGTANG_HEYUN)
                    finish()
                }
            }
            EBOOK_SUBFIELD -> {
                //保存书籍
                if (TextUtils.isEmpty(SaveSpData.newInstance(this).getCommomStringData(BOOK_DAOCAOREN))) {
                    ToastUtils.show(this, "初始化书籍,请稍等...")
                } else {
                    EBookSubFieldActivity.start(this)
                    finish()
                }
            }

        }
    }

    private fun saveBook() {

        val bookName = when (mGameType) {
            EBOOK_TREE -> {
                ""
            }
            EBOOK_LOOP -> {
                BOOK_QINGTANG_HEYUN
            }
            EBOOK_SUBFIELD -> {
                BOOK_DAOCAOREN
            }
            else -> {
                ""
            }
        }
        if (TextUtils.isEmpty(bookName)) {
            tvCover.gone()
            return
        }
        if (TextUtils.isEmpty(SaveSpData.newInstance(this).getCommomStringData(bookName))) {
            articleLineViewModel.saveBook(this, bookName)
        } else {
            tvCover.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(SAVE_BOOK, 300)
    }
}
