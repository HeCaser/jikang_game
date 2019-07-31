package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.game.R
import com.example.game.constant.BOOK_ZHONGQIUJIE
import com.example.game.database.AppDatabase
import com.example.game.utils.StatusBarUtils
import com.example.game.viewmodel.ArticleLineViewModel
import com.example.game.viewmodel.ArticleLineViewModelFactory
import kotlinx.android.synthetic.main.activity_ebook_loop.*
import androidx.core.text.isDigitsOnly as isDigitsOnly1


/**
 *EBook 循环
 */
class EBookLoopActivity : BaseActivity() {

    companion object {
        const val MSG_START_GAME = 1
        const val MSG_MOVE_LINE = 2
        fun start(ctx: Context) {
            Intent(ctx, EBookLoopActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mCircleWidth = 0
    private var mStartLine =0
    val TAG = EBookLoopActivity::class.java.simpleName
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    initShowView()
                }
                MSG_MOVE_LINE -> {
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
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebook_loop)
        initViewAndData()
        initListener()
    }

    private fun initViewAndData() {
        mCircleWidth = circleView.getmWidth()
    }

    private fun initListener() {
        circleView.setOnClickListener {
            mCircleWidth += 20
            circleView.updateWidth(mCircleWidth)
            getData()
        }
        tvLeft.setOnClickListener {
            articleLineViewModel.saveBook(this, BOOK_ZHONGQIUJIE)
        }

        articleLineViewModel.lines.observe(this, Observer {
            Log.e(TAG,"${it.size}")
        })
    }



    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 100)
    }

    private fun initShowView() {
    }


    /**
     * 结束游戏
     */
    private fun finisGame() {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.sendEmptyMessage(MSG_MOVE_LINE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    fun getData() {
        articleLineViewModel.getLineFromIndex(mStartLine,10, BOOK_ZHONGQIUJIE)
        mStartLine+=10
    }
}
