package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.game.R
import com.example.game.constant.BOOK_ZHONGQIUJIE
import com.example.game.database.AppDatabase
import com.example.game.database.ArticleLine
import com.example.game.util.px2dp
import com.example.game.utils.ScreenUtils
import com.example.game.utils.StatusBarUtils
import com.example.game.viewmodel.ArticleLineViewModel
import com.example.game.viewmodel.ArticleLineViewModelFactory
import com.example.game.widget.EBookSubFieldView
import com.example.game.widget.EbookSettingView
import com.example.game.widget.MagnifyTextView
import kotlinx.android.synthetic.main.activity_ebook_subfield.*


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

    private var mViewItemList = arrayListOf<EBookSubFieldView>()//每一行的tv
    private var mStartLine = 0
    private var mStep = 80
    private var mMoveCircleDelay = 100L
    val TAG = EBookLoopActivity::class.java.simpleName
    //文章的行集合
    private var mContents = listOf<ArticleLine>()//文本
    private var mTempContents = listOf<ArticleLine>()
    private var mShowIndex = 0

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    initShowView()
                }
                MSG_MOVE_CIRCLE -> {
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
        setContentView(R.layout.activity_ebook_subfield)
        initViewAndData()
        initListener()
    }

    private fun initViewAndData() {
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 500)
        setCenterTitle("济康-EBook分栏")
    }

    private fun initListener() {

        articleLineViewModel.lines.observe(this, Observer {
            handleData(it)
        })
    }


    private fun initShowView() {
        if (llParent.childCount != 0) return
        val height = llParent.measuredHeight
        val mContentDp = px2dp(height.toFloat())
        val itemHeightDp = 90
        val width = ScreenUtils.getScreenSize(this).x
        if (mContentDp == 0) {
            mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 50)
        } else {
            llParent.removeAllViews()
            mViewItemList.clear()
            val mTotalItemCount = mContentDp / itemHeightDp
            for (num in 0 until mTotalItemCount) {
                val tv = EBookSubFieldView(this)
                llParent.addView(tv, width, itemHeightDp)
                mViewItemList.add(tv)
            }
//            mHandler.sendEmptyMessageDelayed(MSG_MOVE_LINE, 100)
        }

        if (mViewItemList.isNotEmpty()) {
            mViewItemList[0].setContent("2000", 0)
        }
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

        var end = mShowIndex + 2
        if (end > mContents.size) {
//            tvLeft.text = mContents[mContents.size - 1].content
//            tvRight.text = ""
        } else {
//            tvLeft.text = mContents[mShowIndex].content
//            tvRight.text = mContents[mShowIndex + 1].content
        }
        mShowIndex += 2
        mHandler.sendEmptyMessageDelayed(MSG_MOVE_CIRCLE, mMoveCircleDelay)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    fun getData() {
        mStartLine += mStep
        articleLineViewModel.getLineFromIndex(mStartLine, mStep, BOOK_ZHONGQIUJIE)
    }
}
