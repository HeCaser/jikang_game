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
import com.example.game.database.AppDatabase
import com.example.game.database.ArticleLine
import com.example.game.utils.*
import com.example.game.viewmodel.ArticleLineViewModel
import com.example.game.viewmodel.ArticleLineViewModelFactory
import com.example.game.widget.EbookSettingView
import kotlinx.android.synthetic.main.activity_ebook_loop.*
import java.util.*
import androidx.core.text.isDigitsOnly as isDigitsOnly1


/**
 *EBook 循环
 */
class EBookLoopActivity : BaseActivity() {

    companion object {
        const val MSG_START_GAME = 1
        const val MSG_MOVE_CIRCLE = 2
        fun start(ctx: Context, book: String) {
            Intent(ctx, EBookLoopActivity::class.java).apply {
                putExtra("book", book)
                ctx.startActivity(this)
            }
        }
    }

    private var isPaused = false //游戏是否暂停
    //半径是否反向变化(又大变小)
    private var isReverse = false

    private var mCircleWidth = 0
    private var mMindWidth = 0
    private var mMaxWidth = 0
    private var mStartLine = 0
    private var mStep = 80
    private var mMoveCircleDelay = 1000L
    val TAG = EBookLoopActivity::class.java.simpleName
    //文章的行集合
    private var mContents = listOf<ArticleLine>()//文本
    private var mTempContents = listOf<ArticleLine>()
    private var mShowIndex = 0
    private var book = ""
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    startGame()
                }
                MSG_MOVE_CIRCLE -> {
                    if (isPaused) {
                        //游戏暂停(设置速度,进度)时逻辑
                        sendEmptyMessageDelayed(MSG_MOVE_CIRCLE, mMoveCircleDelay)
                        return
                    }
                    changeRadius()
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
        mMaxWidth = (ScreenUtils.getScreenSize(this).x * 0.5).toInt()
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 500)
        book = intent.getStringExtra("book")?:""
        ebookSet.setMaxSpeed(1000)
        ebookSet.setMinSpeed(200)
        ebookSet.setSpeed(700)
        setCenterTitle("济康-EBook循环")
    }

    private fun initListener() {
        clContent.setOnLongClickListener {
            changeSetting()
            return@setOnLongClickListener true
        }

        ebookSet.setCallback(object : EbookSettingView.CallBack {
            override fun onSetCallBack(type: Int, speed: Int) {
                if (EbookSettingView.CALLBACK_FINISH == type) {
                    continueGame(speed)
                }
            }
        })

        articleLineViewModel.lines.observe(this, Observer {
            handleData(it)
        })
    }


    /**
     * 开始游戏就是去数据库里取数据
     */
    private fun startGame() {
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, book)
    }

    /**
     * 处理从数据库返回的数据
     */
    private fun handleData(lines: List<ArticleLine>) {
        if (lines.isEmpty() && mStartLine == 0) {
            //数据还没准备好,延时再次获取
            mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 200)
            return
        }

        if (lines.isEmpty()) {
            //没有数据了,现有数据显示完毕就结束游戏
            mTempContents = lines
            return
        } else {
            mTempContents = lines
            println("获取数据=" + lines[0].id)
        }
        if (mStartLine == 0) {
            //首次获取数据,开始动画,显示文字
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_CIRCLE, mMoveCircleDelay)
            mContents = mTempContents
            //缓存下一页数据
            getData()
        }
    }

    /**
     * 改变半径,文字,判断游戏是否结束,是否需要加载下一页内容
     */
    private fun changeRadius() {
        if (mShowIndex >= mContents.size) {
            if (mTempContents.isEmpty()) {
                //没有数据了,游戏结束
                finisGame()
                return
            } else {
                mContents = mTempContents
                getData()
                mShowIndex = 0
            }
        }

        if (isReverse) {
            mCircleWidth -= 4
        } else {
            mCircleWidth += 4
        }
        if (mCircleWidth >= mMaxWidth) {
            isReverse = true
            mCircleWidth -= 4
        }
        if (mCircleWidth <= mMindWidth) {
            isReverse = false
            mCircleWidth += 4
        }

        var end = mShowIndex + 2
        if (end > mContents.size) {
            tvLeft.text = mContents[mContents.size - 1].content
            tvRight.text = ""
        } else {
            tvLeft.text = mContents[mShowIndex].content
            tvRight.text = mContents[mShowIndex + 1].content
        }
        mShowIndex += 2
        circleView.updateWidth(mCircleWidth)

        mMoveCircleDelay = (100 + (ebookSet.mMax - ebookSet.mSpeed)).toLong()
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_CIRCLE, mMoveCircleDelay)
    }


    /**
     * 结束游戏
     */
    private fun finisGame() {
        mHandler.removeCallbacksAndMessages(null)
        EbookRecordActivity.start(this, "", 10)
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    fun getData() {
        mStartLine += mStep
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, book)
    }

    /**
     * 继续游戏
     */
    private fun continueGame(speed: Int) {
        ebookSet.gone()
        println("speed=$speed")
        isPaused = false
    }

    /**
     * 修改游戏参数
     */
    private fun changeSetting() {
        isPaused = true
        ebookSet.isVisible = true
    }
}
