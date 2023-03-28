package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.game.R
import com.example.game.constant.BOOK_DAOCAOREN
import com.example.game.database.AppDatabase
import com.example.game.database.ArticleLine
import com.example.game.utils.ScreenUtils
import com.example.game.utils.StatusBarUtils
import com.example.game.utils.gone
import com.example.game.utils.isVisible
import com.example.game.viewmodel.ArticleLineViewModel
import com.example.game.viewmodel.ArticleLineViewModelFactory
import com.example.game.widget.EBookSubFieldView
import com.example.game.widget.EbookSettingView
import kotlinx.android.synthetic.main.activity_ebook_subfield.*


/**
 *EBook 分栏
 */
class EBookSubFieldActivity : BaseActivity() {

    companion object {
        const val MSG_START_GAME = 1
        const val MSG_MOVE_FOCUS = 2
        fun start(ctx: Context) {
            Intent(ctx, EBookSubFieldActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var isPaused = false //游戏是否暂停

    private var mViewItemList = arrayListOf<EBookSubFieldView>()//每一行的tv
    private var mStartLine = 0
    private var mStep = 80
    private var mMoveCircleDelay = 600L
    private var mLineCount = 1
    val TAG = EBookSubFieldActivity::class.java.simpleName
    //文章的行集合
    private var mContents = listOf<ArticleLine>()//文本
    private var mTempContents = listOf<ArticleLine>()
    private var mShowIndex = -1

    private var mBookName = ""
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    initShowView()
                }
                MSG_MOVE_FOCUS -> {
                    if (isPaused) {
                        //游戏暂停(设置速度,进度)时逻辑
                        sendEmptyMessageDelayed(MSG_MOVE_FOCUS, mMoveCircleDelay)
                        return
                    }
                    changeFocus()
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
        setContentView(R.layout.activity_ebook_subfield)
        initViewAndData()
        initListener()
    }

    private fun initViewAndData() {
        mBookName = BOOK_DAOCAOREN
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 500)
        ebookSet.setMaxSpeed(1000)
        ebookSet.setMinSpeed(200)
        ebookSet.setSpeed(600)
        setCenterTitle("济康-EBook分栏")
    }

    private fun initListener() {
        clContent.setOnLongClickListener {
            changeSetting()
            return@setOnLongClickListener true
        }

        articleLineViewModel.lines.observe(this, Observer {
            handleData(it)
        })


        ebookSet.setCallback(object : EbookSettingView.CallBack {
            override fun onSetCallBack(type: Int, speed: Int) {
                if (EbookSettingView.CALLBACK_FINISH == type) {
                    continueGame(speed)
                }
            }
        })
    }


    private fun initShowView() {
        if (llParent.childCount != 0) return
        val height = llParent.measuredHeight
        val itemHeight = 200
        val width = ScreenUtils.getScreenSize(this).x
        if (height == 0) {
            mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 50)
        } else {
            llParent.removeAllViews()
            mViewItemList.clear()
            mLineCount = height / itemHeight
            mStep = mLineCount * 3 // 每一页数量是行数*3, 步进用来控制下一页请求时的起始位置判断
            for (num in 0 until mLineCount) {
                val tv = EBookSubFieldView(this)
                llParent.addView(tv, width, itemHeight)
                mViewItemList.add(tv)

            }
        }

        startGame()
    }

    /**
     * 开始游戏就是去数据库里取数据
     */
    private fun startGame() {
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, mBookName)
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
            //没有数据了,缓存集合置空. 等待现有数据显示完毕就结束游戏
            mTempContents = lines
            return
        } else {
            mTempContents = lines
            println("获取数据=" + lines[0].id)
        }
        if (mStartLine == 0) {
            //首次获取数据,开始动画,显示文字
            mContents = mTempContents
            setPageData(mContents)
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_FOCUS, mMoveCircleDelay)
            //缓存下一页数据
            getData()
        }
    }

    /**
     * 更新页面数据
     * 每次取库都是取一整页数据
     */
    private fun setPageData(listData: List<ArticleLine>) {
        for (index in 0 until mStep) {
            val line = index / 3
            val pos = index % 3
            if (index >= listData.size) {
                mViewItemList[line].setContent("", pos)
            } else {
                mViewItemList[line].setContent(listData[index].content!!, pos)
            }
        }
    }

    /**
     * 改变当前选中文字,判断游戏是否结束,是否需要加载下一页内容
     */

    private fun changeFocus() {


        //第一次开始
        if (mShowIndex == -1) {
            mShowIndex = 0
            mViewItemList[0].setStyle(0)
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_FOCUS, mMoveCircleDelay)
            return
        }


        //显示到最后
        if (mShowIndex == mStep) {
            mShowIndex = 1
            mViewItemList[mLineCount - 1].clearStyle(2)
        }

        clearStyle()
        mShowIndex++

        if (mShowIndex >= mContents.size && mTempContents.isEmpty()) {
            //没有数据了,游戏结束
            finisGame()
            return
        }

        setStyle()
        mMoveCircleDelay = 1200 - ebookSet.mSpeed.toLong()
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_FOCUS, mMoveCircleDelay)
    }

    //清空样式
    private fun clearStyle() {
        val line = mShowIndex / 3
        val pos = mShowIndex % 3
        mViewItemList[line].clearStyle(pos)
    }

    //设置新的样式
    private fun setStyle() {
        if (mShowIndex == mStep) {
            //显示最后一个, 需展示新的一页,并且缓存下一页
            mContents = mTempContents
            getData()
            setPageData(mContents)
            mShowIndex = 0

        }
        val line = mShowIndex / 3
        val pos = mShowIndex % 3
        mViewItemList[line].setStyle(pos)
    }


    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 100)

    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        mHandler.removeCallbacksAndMessages(null)
        EbookRecordActivity.start(this, "", 10)
        finish()
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


    fun getData() {
        mStartLine += mStep
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, mBookName)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}
