package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import com.example.game.constant.ARTICLE_JLFR
import com.example.game.constant.LINE_BREAK
import com.example.game.util.px2dp
import com.example.game.util.screenWidth
import com.example.game.utils.StatusBarUtils
import com.example.game.widget.MagnifyTextView
import kotlinx.android.synthetic.main.activity_ebook_tree.*
import androidx.core.text.isDigitsOnly as isDigitsOnly1
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.SpannableStringBuilder
import android.graphics.Color
import com.example.game.R
import com.example.game.utils.gone
import com.example.game.utils.isVisible
import com.example.game.widget.EbookSettingView


/**
 *EBook
 */
class EBookTreeActivity : BaseActivity() {

    companion object {
        const val MSG_START_GAME = 1
        const val MSG_MOVE_LINE = 2

        fun start(ctx: Context) {
            Intent(ctx, EBookTreeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mContentDp = 1
    //横条高度
    private var mItemHeight = 90
    private var mContentWidth = 0
    //条目总数
    private var mTotalItemCount = 0
    //显示的文章内容所在行数,不停自增,直到文章显示完毕
    private var mShowArticlePos = 0
    private var mNormalTextSize = 20f
    private var mForegroundTextSize = 22f
    private var mNoralTextColor = 0
    private var mForegroundTextColor = 0
    private var mMoveDelayTime = 1000L

    private var mViewItemList = arrayListOf<MagnifyTextView>()//每一行的tv
    private var mArticleLines = arrayListOf<String>()//阅读文章分行记录
    private var mArticleLineNumber = 0

    private var isPaused = false //游戏是否暂停


    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START_GAME -> {
                    initShowView()
                }
                MSG_MOVE_LINE -> {
                    if (isPaused) {
                        //游戏暂停(设置速度,进度)时逻辑
                        sendEmptyMessageDelayed(MSG_MOVE_LINE, mMoveDelayTime)
                        return
                    }
                    //文章结束
                    if (mShowArticlePos >= mArticleLineNumber) {
                        finisGame()
                        return
                    }
                    var foregroundPos = mShowArticlePos % mTotalItemCount
                    //改变当前显示行的文案状态,回复先前显示行的文案状态
                    if (foregroundPos == 0 && mShowArticlePos > 0) {
                        //当前屏文案显示结束,进入下一屏内容
                        setNormal(mTotalItemCount)
                        showNextScreenArticle()
                    }
                    setForeGround(foregroundPos)
                    if (foregroundPos > 0) {
                        setNormal(foregroundPos)
                    }


                    mMoveDelayTime = calculateDelayTime()
                    mShowArticlePos++
                    sendEmptyMessageDelayed(MSG_MOVE_LINE, mMoveDelayTime)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebook_tree)
        initViewAndData()
        initListener()

    }

    private fun initViewAndData() {
        mContentWidth = screenWidth
        mNoralTextColor = resources.getColor(R.color.color_999999)
        mForegroundTextColor = resources.getColor(R.color.color_333333)
        mArticleLines = ARTICLE_JLFR.split(LINE_BREAK) as ArrayList<String>
        mArticleLineNumber = mArticleLines.size

        setCenterTitle("济康-EBook树形")
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
    }


    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 100)
    }

    private fun initShowView() {
        if (flexBox.childCount != 0) return
        val height = flexBox.measuredHeight
        mContentDp = px2dp(height.toFloat())
        if (mContentDp == 0) {
            mHandler.sendEmptyMessageDelayed(MSG_START_GAME, 50)
        } else {
            flexBox.removeAllViews()
            mViewItemList.clear()
            mTotalItemCount = mContentDp / mItemHeight
            for (num in 0 until mTotalItemCount) {
                val tv = MagnifyTextView(this)
                tv.textSize = mNormalTextSize
                tv.text = "${mArticleLines[num]}"
                tv.gravity = Gravity.CENTER
                tv.setTextColor(mNoralTextColor)
                flexBox.addView(tv, mContentWidth, mItemHeight)
                mViewItemList.add(tv)
            }
            mHandler.sendEmptyMessageDelayed(MSG_MOVE_LINE, 100)
        }
    }

    /**
     * 显示下一屏内容
     */
    private fun showNextScreenArticle() {
        if (mShowArticlePos == mArticleLineNumber) {
            finisGame()
            return
        }
        val page = mShowArticlePos / mTotalItemCount
        val start = page * mTotalItemCount
        for (num in 0 until mTotalItemCount) {
            val relPos = start + num
            if (relPos < mArticleLineNumber) {
                mViewItemList[num].text = mArticleLines[relPos]
            } else {
                mViewItemList[num].text = ""
            }
        }
    }

    /**
     * 回复tv的默认状态
     */
    private fun setNormal(foregroundPos: Int) {
        with(mViewItemList[foregroundPos - 1]) {
            setTextColor(mNoralTextColor)
            textSize = mNormalTextSize
            setCircleSWitch(false)
            val content = text
            val spannable = SpannableStringBuilder(content)
            val good_gray = ForegroundColorSpan(mNoralTextColor)
            spannable.setSpan(good_gray, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            text = spannable
        }
    }

    /**
     * 计算阅读速度: handler 的延时
     * 计算因子有两个 1 当前行的字符数量  2 当前设置的阅读速度
     */
    private fun calculateDelayTime(): Long {
        if (mShowArticlePos >= mArticleLines.size) return mMoveDelayTime
        val lineLength = mArticleLines[mShowArticlePos].length
        val speed = ebookSet.mSpeed

        val tempSpeed = speed - lineLength * 20
        var resSpeed = ebookSet.getMaxSpeed() - tempSpeed

        if (resSpeed < 100) {
            resSpeed = 100
        }
        return resSpeed.toLong()
    }

    /**
     * 设置tv为前端显示状态
     */
    private fun setForeGround(foregroundPos: Int) {
        with(mViewItemList[foregroundPos]) {
            setTextColor(mForegroundTextColor)
            textSize = mForegroundTextSize
            setCircleSWitch(true)
            val content = text
            if (content.length % 2 != 0) {
                val spannable = SpannableStringBuilder(content)
                //设置字体颜色为红色
                val good_red = ForegroundColorSpan(Color.RED)
                //设置字体颜色为灰色
                val good_gray = ForegroundColorSpan(mNoralTextColor)
                //改变第0-3个字体颜色
                val center = content.length / 2
                spannable.setSpan(good_red, center, center + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                //改变第4-之后所有的字体颜色（这里一定要注意范围，否则会造成越界）
                spannable.setSpan(good_gray, 0, center, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(good_gray, center + 1, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                text = spannable
            }
        }
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
}
