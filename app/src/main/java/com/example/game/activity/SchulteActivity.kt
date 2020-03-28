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
import com.example.game.adapter.SchulteBinder
import com.example.game.bean.SchulteItemBean
import com.example.game.constant.SCHULTE_ACTIVITY
import com.example.game.utils.SaveSpData
import kotlinx.android.synthetic.main.activity_schulte.*
import me.drakeet.multitype.MultiTypeAdapter

/**
 * 舒尔特
 */
class SchulteActivity : BaseActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mItems: MutableList<SchulteItemBean>

    val MSG_TIME_COUNT = 1
    val MSG_HIDE_NUMBE = 2
    val MSG_NOTOFY_DADA_CHANGED = 3

    //耗时,毫秒值
    private var mCountTime = 0L

    //是否显示字母
    private var isTakeLetter = false
    //当前进行到的位置
    private var mSelectedNum = 1
    //每行显示的数量
    private var mSpanNum = 5

    private var mSpKey = ""

    var mHandler: Handler = @SuppressLint("HandlerLeak")

    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_TIME_COUNT -> {
                    mCountTime += 1000
                    tvTimeRecord.text = "${mCountTime/1000}"
                    sendEmptyMessageDelayed(MSG_TIME_COUNT, 1000)
                }
                MSG_HIDE_NUMBE -> {
                    mItems.forEachIndexed { index, numberBean ->
                    }
                    mAdapter.notifyDataSetChanged()
                }
                MSG_NOTOFY_DADA_CHANGED -> {
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        fun start(ctx: Context) {
            Intent(ctx, SchulteActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schulte)
        initViewAndData()
        initListener()
        startGame()
    }

    private fun initViewAndData() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCenterTitle("济康-舒尔特")

        mAdapter = MultiTypeAdapter()
        mAdapter.register(SchulteBinder {
            handleItemClick(it)
        })
        mItems = ArrayList()
    }

    private fun initListener() {
        //是否有字母的切换监听
        switchZiMu.setOnCheckedChangeListener { buttonView, isChecked ->
            changeLetterShow(isChecked)
        }
        //尺寸选择发生变化
        rgChiCun.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb1 -> {
                    mSpanNum = 5
                }
                R.id.rb2 -> {
                    mSpanNum = 6
                }
                R.id.rb3 -> {
                    mSpanNum = 7
                }
            }
            startGame()
        }
    }


    /**
     * 初始化view
     */
    private fun startGame() {
        mSelectedNum = 1
        mCountTime=0
        tvTimeRecord.text = ""
        rvNumbers.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@SchulteActivity, mSpanNum)
        }

        mItems.clear()
        var total = mSpanNum * mSpanNum
        for (number in 1..total) {
            val num = SchulteItemBean(number, "")
            mItems.add(num)

        }
        mAdapter.items = mItems
        mItems.shuffle()
        setShowText()
        mAdapter.notifyDataSetChanged()
        mHandler.removeMessages(MSG_TIME_COUNT)
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUNT,1000)
    }

    /**
     * 改变显示字母状态
     */
    private fun changeLetterShow(checked: Boolean) {
        isTakeLetter = checked
        setShowText()
        showNextHint()
        mAdapter.notifyDataSetChanged()
    }

    /**
     *改变下一个的提示内容
     */
    private fun showNextHint() {
        if (isTakeLetter) {
            tvNextValue.text = ('A' + mSelectedNum - 1).toString()
        } else {
            tvNextValue.text = "$mSelectedNum"
        }
    }

    /**
     * 是否显示字母等条件,计算出条目显示的文案
     */
    private fun setShowText() {
        mItems.forEach { item ->
            val pos = item.number
            if (isTakeLetter) {
                //带字母
                if (pos < 26) {
                    item.showText = ('A' + (pos - 1)).toString()
                } else {
                    item.showText = "${pos - 26}"
                }
            } else {
                //不带字母
                item.showText = "$pos"
            }
        }
    }

    /**
     * 处理点击事件
     * 根据不同游戏类型处理
     */
    private fun handleItemClick(pos: Int) {

        handleGameThree(pos)

    }


    /**
     * pos: 点击的条目所在 adapter 中位置
     */
    private fun handleGameThree(pos: Int) {
        var item = mItems[pos]
        var number = item.number
        if (number == mSelectedNum) {
            mAdapter.notifyDataSetChanged()
            if (number == mSpanNum * mSpanNum) {
                //选择完毕,游戏结束
                showRecord()
            }
            mSelectedNum++
        } else if (number != mSelectedNum) {
//                ToastUtils.show(this, "此处是$number")
            var child = rvNumbers.getChildAt(pos)
            with(child) {
                //点击了错误选项,展示5ms
//                    tvNumber.setTextColor(Color.WHITE)
//                    tvNumber.setBackgroundColor(Color.RED)
                mHandler.sendEmptyMessageDelayed(MSG_NOTOFY_DADA_CHANGED, 500L)
            }
        }

    }

    /**
     * 跳转到成绩展示界面
     */
    private fun showRecord() {
        mHandler.removeCallbacksAndMessages(null)
        mSpKey = "$mSpanNum${if (isTakeLetter) 1 else 0}" + SCHULTE_ACTIVITY
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
