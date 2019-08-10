package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.game.R
import com.example.game.constant.BOOK_ZHONGQIUJIE
import com.example.game.database.AppDatabase
import com.example.game.database.ArticleLine
import com.example.game.utils.SaveSpData
import com.example.game.utils.ScreenUtils
import com.example.game.utils.StatusBarUtils
import com.example.game.viewmodel.ArticleLineViewModel
import com.example.game.viewmodel.ArticleLineViewModelFactory
import kotlinx.android.synthetic.main.activity_ebook_loop.*
import androidx.core.text.isDigitsOnly as isDigitsOnly1


/**
 *EBook 循环
 */
class EBookSubFieldActivity : BaseActivity() {

    companion object {
        const val MSG_START_GAME = 1
        const val MSG_MOVE_CIRCLE = 2
        fun start(ctx: Context) {
            Intent(ctx, EBookSubFieldActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mCircleWidth = 0
    private var mMindWidth=0
    private var mMaxWidth=0
    private var mStartLine = 0
    private var mStep = 10
    private var mMoveCircleDelay = 100L
    val TAG = EBookSubFieldActivity::class.java.simpleName
    //文章的行集合
    private var mLines = listOf<ArticleLine>()
    private var mContents = "" //文本

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    startGame()
                }
                MSG_MOVE_CIRCLE -> {
                    mCircleWidth+=4
                    if (mCircleWidth>=mMaxWidth){
                        mCircleWidth= mMindWidth
                    }
                    circleView.updateWidth(mCircleWidth)
                    this.sendEmptyMessageDelayed(MSG_MOVE_CIRCLE,mMoveCircleDelay)
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
        mMindWidth = circleView.getmWidth()
        mMaxWidth = (ScreenUtils.getScreenSize(this).x*0.8).toInt()

        if (TextUtils.isEmpty(SaveSpData.newInstance(this).getCommomStringData(BOOK_ZHONGQIUJIE))) {
            articleLineViewModel.saveBook(this, BOOK_ZHONGQIUJIE)
        }
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 500)
        setCenterTitle("济康-EBook分栏")
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
            handleData(it)
        })
    }


    /**
     * 开始游戏就是去数据库里取数据
     */
    private fun startGame() {
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, BOOK_ZHONGQIUJIE)
    }

    /**
     * 处理从数据库返回的数据
     */
    private fun handleData(lines: List<ArticleLine>) {
        if (lines.isEmpty() && mStartLine == 0) {
            //数据还没准备好,延时再次获取
            mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 500)
            return
        }

        if (lines.isEmpty()) {
            finisGame()
            return
        }
        mLines = lines
        if (mStartLine==mStep){
            //首次获取数据,开始动画,显示文字
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_CIRCLE,mMoveCircleDelay)
        }
        getData()
    }


    private fun changeLinesToString(){
        val builder = StringBuilder()
        mLines.forEach {
            builder.append(it.content)
        }
        mContents = builder.toString()
    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.sendEmptyMessage(MSG_MOVE_CIRCLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    fun getData() {
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, BOOK_ZHONGQIUJIE)
        mStartLine += mStep
    }
}
