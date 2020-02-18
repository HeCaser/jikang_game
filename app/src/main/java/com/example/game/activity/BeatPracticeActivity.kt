package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.game.R
import com.example.game.adapter.RememberEyeItemBinder
import com.example.game.bean.RememberEyeBean
import com.example.game.constant.SEARCH_REMEMBER_EYE_ACTIVITY
import kotlinx.android.synthetic.main.activity_beat_practice.*
import me.drakeet.multitype.MultiTypeAdapter
import kotlin.random.Random


/**
 * 节拍器
 */
class BeatPracticeActivity : BaseActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    lateinit var list: ArrayList<RememberEyeBean>
    //记录正确位置的集合
    private var mRightListPos = arrayListOf<Int>()

    private var SPAN_COUNT = 3
    private var TOTAL_COUNT = 0
    private var mScore = 0
    //此轮未找到的数量
    private var mUnFountCount = SPAN_COUNT
    //当前 SPAN_COUNT 对应多添加的正确数量, 0-1
    private var mAddCount = 0

    //是否可点击
    private var isCanClick = false

    companion object {
        const val MSG_START = 1
        const val MSG_NEXT_TURN = 2
        const val MSG_CAN_CLICK = 3
        const val MSG_FINISH_GAME = 4
        fun start(ctx: Context) {
            Intent(ctx, BeatPracticeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                MSG_START -> {
                    tempShowALlFace()
                }
                MSG_NEXT_TURN -> {
                    generateAdapterData()
                    generateRightList()
                    sendEmptyMessageDelayed(MSG_START, 800)
                }
                MSG_CAN_CLICK -> {
                    isCanClick = true
                }
                MSG_FINISH_GAME -> {
                    finisGame()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beat_practice)
        initViewAndData()

    }

    private fun initViewAndData() {
        TOTAL_COUNT = SPAN_COUNT * SPAN_COUNT
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCenterTitle("济康-节拍器")

        mAdapter = MultiTypeAdapter()
        mAdapter.register(RememberEyeItemBinder {
            handleItemClick(it.pos)
        })
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@BeatPracticeActivity, SPAN_COUNT)
//            addItemDecoration(NumberItemDecoration())
        }

        list = arrayListOf()

        generateAdapterData()
        generateRightList()

        mHandler.sendEmptyMessageDelayed(MSG_START, 800)

    }


    //生成正确位置集合
    private fun generateRightList() {
        isCanClick = false
        mRightListPos.clear()
        var count = 0
        val total = SPAN_COUNT + mAddCount
        while (count < total) {
            val index = Random.nextInt(0, TOTAL_COUNT)
            if (!mRightListPos.contains(index)) {
                mRightListPos.add(index)
                count++
            }
        }
    }

    //生成所有数据
    private fun generateAdapterData() {
        list.clear()

        mAdapter.items = list
        if (mAddCount == 2) {
            SPAN_COUNT++
            if (SPAN_COUNT >= 8) {
                SPAN_COUNT = 7
            }
            mAddCount = 0
            recyclerView.layoutManager = GridLayoutManager(this@BeatPracticeActivity, SPAN_COUNT)
        }
        TOTAL_COUNT = SPAN_COUNT * SPAN_COUNT
        for (count in 0 until TOTAL_COUNT) {
            val bean = RememberEyeBean(count)
            list.add(bean)
        }
        mAdapter.notifyDataSetChanged()
        mUnFountCount = SPAN_COUNT + mAddCount
        tvUnfoundCount.text = "$mUnFountCount"
    }


    private fun showALlFace() {
        mRightListPos.forEach {
            getHorder(it).showFace()
        }
    }

    private fun tempShowALlFace() {
        //临时展示时不可点击
        isCanClick = false
        mRightListPos.forEach {
            getHorder(it).showTempFace()
        }
        mHandler.sendEmptyMessageDelayed(MSG_CAN_CLICK, 1000)
    }

    //条目点击事件
    private fun handleItemClick(pos: Int) {
        if (!isCanClick) {
            return
        }
        if (mRightListPos.contains(pos)) {
            //点击了正确选项
            list[pos].isFaceShow = true
            getHorder(pos).showFace()
            mScore += 1
            tvREScore.text = "$mScore"
            mUnFountCount--
            tvUnfoundCount.text = "$mUnFountCount"
            if (mUnFountCount == 0) {
                //全部找完,进入下一轮
                mAddCount++
                mHandler.sendEmptyMessageDelayed(MSG_NEXT_TURN, 500)
            }

        } else {
            isCanClick = false
            getHorder(pos).showError()
            showALlFace()
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_GAME, 500)
        }
    }


    //获取对应位置的horder
    private fun getHorder(pos: Int): RememberEyeItemBinder.ViewHolder {
        val view = recyclerView.getChildAt(pos)
        val horder = recyclerView.getChildViewHolder(view)
        return horder as RememberEyeItemBinder.ViewHolder
    }

    /**
     * 结束游戏
     */
    private fun finisGame() {
        val spKey = SEARCH_REMEMBER_EYE_ACTIVITY
        SearchRecordActivity.start(this, spKey, mScore, 1)
        finish()
    }

}
