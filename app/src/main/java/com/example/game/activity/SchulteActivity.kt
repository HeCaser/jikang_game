package com.example.game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.example.game.widget.SchulteDecoration
import kotlinx.android.synthetic.main.activity_schulte.*
import kotlinx.android.synthetic.main.schulte_item.view.*
import me.drakeet.multitype.MultiTypeAdapter

/**
 * 九宫格
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
                    tvTimeRecord.text = "${mCountTime / 1000}"
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
        setCenterTitle("济康-九宫格")

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
        switchColor.setOnCheckedChangeListener{ buttonView, isChecked ->
            changeColor(isChecked)
        }

        switchBackground.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                rvNumbers.setBackgroundColor(0xffc7edcc.toInt())
            }else{
                rvNumbers.setBackgroundColor(Color.TRANSPARENT)

            }
        }
    }


    /**
     * 改变文字颜色
     */
    private fun changeColor(checked: Boolean) {
        mItems.forEach {
            it.isFullColor = checked
        }
        mAdapter.notifyDataSetChanged()
    }


    /**
     * 初始化view
     */
    private fun startGame() {
        mSelectedNum = 1
        mCountTime = 0
        tvTimeRecord.text = ""
        rvNumbers.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@SchulteActivity, mSpanNum)
            addItemDecoration(SchulteDecoration())
        }

        mItems.clear()
        var total = mSpanNum * mSpanNum
        for (number in 1..total) {
            val num = SchulteItemBean(number, "")
            num.fullColor =getTvColor(number)
            num.isFullColor = switchColor.isChecked
            mItems.add(num)

        }
        mAdapter.items = mItems
        mItems.shuffle()
        setShowText()
        mAdapter.notifyDataSetChanged()
        mHandler.removeMessages(MSG_TIME_COUNT)
        mHandler.sendEmptyMessageDelayed(MSG_TIME_COUNT, 1000)
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
                if (pos <= 26) {
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
        val total = mSpanNum * mSpanNum
        val showNext = tvNextValue.text
        val clickText = mItems[pos].showText
        val child = rvNumbers.getChildAt(pos)

        if (showNext == clickText) {
            mSelectedNum++
            if (mSelectedNum > total) {
                showRecord()
                return
            }
            showNextHint()
            with(child) {
                tvNumber.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                mHandler.postDelayed({
                    tvNumber.setBackgroundColor(android.graphics.Color.WHITE)
                }, 200)
            }
        } else {
            //点击错误
            with(child) {
                tvNumber.setBackgroundColor(Color.RED)
                mHandler.postDelayed({
                    tvNumber.setBackgroundColor(Color.WHITE)
                }, 200)
            }
        }

    }


    /**
     * 跳转到成绩展示界面
     */
    private fun showRecord() {
        mHandler.removeCallbacksAndMessages(null)
        mSpKey = "$mSpanNum" + SCHULTE_ACTIVITY
        var history = SaveSpData.newInstance(this).getCommomLongData(mSpKey)
        if (history == 0L || history > mCountTime - 1000) {
            //新的记录
            SaveSpData.newInstance(this).saveCommonLongData(mSpKey, (mCountTime - 1000))
        }
        ScoreRecordActivity.start(this, mSpKey, mCountTime - 1000)
        finish()
    }

    /**
     * 获取 tv 颜色
     */
    private var mTvColorList = arrayListOf(0xff098dfa.toInt(),
        0xffe60012.toInt(),0xff2fcd1e.toInt(),0xffff8e00.toInt())
    private fun getTvColor(pos:Int):Int{
        return mTvColorList.random()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
