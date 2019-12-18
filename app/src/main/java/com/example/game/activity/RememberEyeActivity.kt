package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.SyncStateContract
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.game.R
import com.example.game.adapter.NumberViewBinder
import com.example.game.adapter.SchulteAttentionBinder
import com.example.game.adapter.SchulteGridBinder
import com.example.game.bean.NumberBean
import com.example.game.constant.SCHULTE_GRID_ACTIVITY
import com.example.game.constant.SCHULTE_GRID_ACTIVITY2
import com.example.game.util.sp
import com.example.game.utils.*
import com.example.game.widget.NumberItemDecoration
import kotlinx.android.synthetic.main.activity_schulte_grid.*
import kotlinx.android.synthetic.main.activity_schulte_grid.button
import kotlinx.android.synthetic.main.activity_schulte_grid.rvNumbers
import kotlinx.android.synthetic.main.schulte_attention_item.view.*
import me.drakeet.multitype.MultiTypeAdapter
import java.util.*

/**
 * 第四阶段 过目不忘
 */
class RememberEyeActivity : BaseActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mItems: MutableList<NumberBean>
    private var mNumbers = ArrayList<NumberBean>()
    val MSG_TIME_COUNT = 1
    val MSG_HIDE_NUMBE = 2
    val MSG_NOTOFY_DADA_CHANGED = 3

    private var mGridNumContents = arrayListOf("2*2", "3*3", "4*4", "5*5", "6*6", "7*7","8*8","9*9")
    private var mTypeontents = arrayListOf("普通", "困难")
    private var mCountTime = 1000L

    private var mNumSelectPos = 0
    private var mTypeSelectPos = 0

    private var mSelectedNum = 1
    private var mSpanNum = 2
    private var mSpKey = ""
    private var mCanTypeThreeClick = false
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
                MSG_HIDE_NUMBE -> {
                    mItems.forEachIndexed { index, numberBean ->
                        numberBean.isHidden = true
                    }
                    mAdapter.notifyDataSetChanged()
                    mCanTypeThreeClick = true
                }
                MSG_NOTOFY_DADA_CHANGED -> {
                    mAdapter.notifyDataSetChanged()
                    mCanTypeThreeClick = true
                }
            }
        }
    }

    companion object {
        fun start(ctx: Context) {
            Intent(ctx, RememberEyeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schulte_grid)
        initViewAndData()
        initListener()
    }

    private fun initViewAndData() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCenterTitle("济康-舒尔特方格")
        singleSelectNum.setContentList(mGridNumContents)
        singleSelectType.setContentList(mTypeontents)
    }

    private fun initListener() {
        button.setOnClickListener {
            startGame()
        }
    }

    /**
     * 初始化view
     */
    private fun startGame() {
        mNumSelectPos = singleSelectNum.getSelectPos()
        mTypeSelectPos = singleSelectType.getSelectPos()
        svSelect.gone()
        clGame.show()
        mSpanNum = mNumSelectPos + 2
        mAdapter = MultiTypeAdapter()
        mAdapter.register(SchulteGridBinder(mSpanNum) {
            handleItemClick(it)
        })
        rvNumbers.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@RememberEyeActivity, mSpanNum)
        }
        mNumbers.clear()
        var total = mSpanNum * mSpanNum
        for (number in 1..total) {
            val num = NumberBean(number, false)
            mNumbers.add(num)
        }
        mItems = ArrayList(mNumbers)
        mAdapter.items = mItems
        mItems.shuffle()
        mAdapter.notifyDataSetChanged()
        mHandler.sendEmptyMessage(MSG_TIME_COUNT)
    }

    /**
     * 处理点击事件
     * 根据不同游戏类型处理
     */
    private fun handleItemClick(pos: Int) {
        when (mTypeSelectPos) {
            0 -> {
                handleGameOne(pos)
            }
            1 -> {
                handleGameTwo(pos)
            }
            2 -> {
                handleGameThree(pos)
            }
        }
    }

    /**
     * 类型1的处理
     * pos: 点击的条目
     */
    private fun handleGameOne(pos: Int) {
        var item = mItems[pos]
        var number = item.number
        if (item.isSelected) return
        if (number == mSelectedNum) {
            item.isSelected = true
            mAdapter.notifyDataSetChanged()
            if (number == mSpanNum * mSpanNum) {
                //选择完毕,游戏结束
                showRecord()
            }
            mSelectedNum++
        } else if (number != mSelectedNum) {
            if (number > mSelectedNum) {
                ToastUtils.show(this, "应选择$mSelectedNum")
            }
        }
    }

    /**
     * 类型2的处理
     * pos: 点击的条目
     */
    private fun handleGameTwo(pos: Int) {
        var item = mItems[pos]
        var number = item.number
        if (item.isSelected) return
        if (number == mSelectedNum) {
            item.isSelected = true
            mItems.shuffle()
            mAdapter.notifyDataSetChanged()
            if (number == mSpanNum * mSpanNum) {
                //选择完毕,游戏结束
                showRecord()
            }
            mSelectedNum++
        } else if (number != mSelectedNum) {
            if (number > mSelectedNum) {
                ToastUtils.show(this, "应选择$mSelectedNum")
            }
        }
    }

    /**
     * 类型3的处理
     * pos: 点击的条目
     */
    private fun handleGameThree(pos: Int) {
        var item = mItems[pos]
        var number = item.number
        if (!mCanTypeThreeClick) return
        if (number == mSelectedNum) {
            item.isSelected = true
            item.isHidden = false
            mAdapter.notifyDataSetChanged()
            if (number == mSpanNum * mSpanNum) {
                //选择完毕,游戏结束
                showRecord()
            }
            mSelectedNum++
        } else if (number != mSelectedNum) {
            if (!item.isSelected) {
//                ToastUtils.show(this, "此处是$number")
                var child = rvNumbers.getChildAt(pos)
                with(child) {
                    //点击了错误选项,展示5ms
                    tvNumber.setTextColor(Color.WHITE)
                    tvNumber.setBackgroundColor(Color.RED)
                    mCanTypeThreeClick = false
                    mHandler.sendEmptyMessageDelayed(MSG_NOTOFY_DADA_CHANGED, 500L)
                }
            }
        }

    }

    /**
     * 跳转到成绩展示界面
     */
    private fun showRecord() {
        mHandler.removeCallbacksAndMessages(null)
        mSpKey = "$mNumSelectPos$mTypeSelectPos" + SCHULTE_GRID_ACTIVITY2
        var history = SaveSpData.newInstance(this).getCommomLongData(mSpKey)
        if (history == 0L || history > mCountTime - 1000) {
            //新的记录
            SaveSpData.newInstance(this).saveCommonLongData(mSpKey, (mCountTime - 1000))
        }
        ScoreRecordActivity.start(this, mSpKey, mCountTime - 1000)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
