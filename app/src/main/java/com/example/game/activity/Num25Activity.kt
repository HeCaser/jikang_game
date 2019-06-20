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
import com.example.game.adapter.Number25ItemBinder
import com.example.game.bean.NumberBean
import com.example.game.constant.NUMBER_25
import com.example.game.utils.DateUtil
import com.example.game.utils.ToastUtils
import com.example.game.widget.Number25Decoration
import com.example.game.widget.NumberItemDecoration
import kotlinx.android.synthetic.main.activity_num_25.*
import me.drakeet.multitype.MultiTypeAdapter
import java.util.*

/**
 * Num25
 */
class Num25Activity : BaseActivity() {
    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mItems: MutableList<NumberBean>
    private var mNumbers = ArrayList<NumberBean>()
    private var mSelectedNum = 1
    private val MSG_TIME_COUNT = 1
    private var mCountTime = 1000L

    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_TIME_COUNT -> {
                    tvTime.text = DateUtil.getMinuteAndSecondByMillisecond(mCountTime)
                    mCountTime += 1000
                    sendEmptyMessageDelayed(MSG_TIME_COUNT, 1000)
                }

            }
        }
    }

    companion object {
        const val SPAN_COUNT = 5
        const val TOTAL = 25
        fun start(ctx: Context) {
            Intent(ctx, Num25Activity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_num_25)

        initViewAndData()
        initListener()
    }

    private fun initListener() {
        button.setOnClickListener {
            resetData()
        }
    }

    private fun initViewAndData() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCenterTitle("济康-Num25")
        mAdapter = MultiTypeAdapter()
        mAdapter.register(Number25ItemBinder {
            handlerClick(it)
        })
        rvNumbers.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@Num25Activity, SPAN_COUNT)
            addItemDecoration(Number25Decoration())
        }

        resetData()
    }

    private fun resetData() {
        mSelectedNum = 1
        mNumbers.clear()
        for (number in 1..TOTAL) {
            val num = NumberBean(number, false)
            mNumbers.add(num)
        }
        mItems = ArrayList(mNumbers)
        mAdapter.items = mItems
        mItems.shuffle()
        mAdapter.notifyDataSetChanged()
        mCountTime = 1000
        mHandler.removeCallbacksAndMessages(null)
        tvTime.text = "倒计时"
    }

    private fun handlerClick(pos: Int) {
        val item = mItems[pos]
        if (item.isSelected) return
        if (item.number != mSelectedNum) {
            ToastUtils.show(this, "选择错误")

        } else {
            item.isSelected = true
            if (mSelectedNum == 1) {
                mHandler.sendEmptyMessageDelayed(MSG_TIME_COUNT, 0)
            }
            mSelectedNum++
            if (mSelectedNum == TOTAL+1) {
                saveResult()
            }
        }
        mAdapter.notifyItemChanged(pos)
    }

    private fun saveResult() {
        --mSelectedNum
        mHandler.removeCallbacksAndMessages(null)
        val key = NUMBER_25
        ScoreRecordActivity.start(this, key, mCountTime - 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}