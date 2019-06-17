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
import com.example.game.adapter.NumberViewBinder
import com.example.game.bean.NumberBean
import com.example.game.util.sp
import com.example.game.utils.DateUtil
import com.example.game.widget.NumberItemDecoration
import kotlinx.android.synthetic.main.activity_one_to_onehundard.*
import me.drakeet.multitype.MultiTypeAdapter
import java.util.*


class OneToOneHundredActivity : BaseActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mItems: MutableList<NumberBean>
    private var mNumbers = ArrayList<NumberBean>()
    private var mSelectedNum = 1
    private var mIsStop = false
    private val MSG_TIME_COUNT = 1
    private var mCountTime = 1000L

    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (mIsStop) return
            when (msg.what) {
                MSG_TIME_COUNT -> {
                    tvTime.text = DateUtil.getMinuteAndSecondByMillisecond(mCountTime)
                    mCountTime += 1000
                    sendEmptyMessageDelayed(MSG_TIME_COUNT, 1000)
                    if (mCountTime > 5 * 60 * 1000) {
                        saveResult()
                    }
                }

            }
        }
    }

    companion object {
        const val SPAN_COUNT = 10
        fun start(ctx: Context) {
            Intent(ctx, OneToOneHundredActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_to_onehundard)

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
        setCenterTitle("济康数到100")
        mAdapter = MultiTypeAdapter()
        mAdapter.register(NumberViewBinder {
            handlerClick(it)
        })
        rvNumbers.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@OneToOneHundredActivity, SPAN_COUNT)
            addItemDecoration(NumberItemDecoration())
        }

        resetData()
    }

    private fun resetData() {
        mSelectedNum = 1
        mNumbers.clear()
        for (number in 1..100) {
            val num = NumberBean(number, false)
            mNumbers.add(num)
        }
        mItems = ArrayList(mNumbers)
        mAdapter.items = mItems
        mItems.shuffle()
        mAdapter.notifyDataSetChanged()
        mIsStop = false
        mCountTime = 1000
        mHandler.removeCallbacksAndMessages(null)
        tvTime.text = "倒计时"
    }

    private fun handlerClick(pos: Int) {
        val item = mItems[pos]
        if (mIsStop || item.isSelected) return
        if (item.number != mSelectedNum) {
            item.isError = true
            mIsStop = true
            if (mSelectedNum != 1) {
                saveResult()
            }

        } else {
            item.isSelected = true
            if (mSelectedNum == 1) {
                mHandler.sendEmptyMessageDelayed(MSG_TIME_COUNT, 0)
            }
            mSelectedNum++
            if(mSelectedNum==101){
                saveResult()
            }
        }
        mAdapter.notifyItemChanged(pos)
    }

    private fun saveResult() {
        val oldScore = sp.oneToHundredRecord.toInt()
        val oldTime = sp.oneToHundredTime
        --mSelectedNum
        if (oldScore < (mSelectedNum) || (oldScore == mSelectedNum && oldTime > mCountTime - 1000)) {
            sp.apply {
                oneToHundredRecord = (mSelectedNum).toString()
                oneToHundredTime = (mCountTime - 1000)
            }
        }
        mHandler.removeCallbacksAndMessages(null)
        OneToHundredResultActivity.start(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
