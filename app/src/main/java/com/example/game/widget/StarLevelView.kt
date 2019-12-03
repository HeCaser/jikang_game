package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import com.example.game.bean.NumberBean
import com.example.game.util.res2color
import kotlinx.android.synthetic.main.number_view.view.*
import kotlinx.android.synthetic.main.start_level.view.*

/**
 * Author pan.he
 * 五角星等级的view
 * 分为 1-10 10个等级
 */
class StarLevelView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var mLevel = 1
    var mViewList = arrayListOf<ImageView>()

    init {
        LayoutInflater.from(context).inflate(R.layout.start_level, this, true)
        initView()
    }

    private fun initView() {
        if (mViewList.isEmpty()) {
            mViewList.add(ivStar1)
            mViewList.add(ivStar2)
            mViewList.add(ivStar3)
            mViewList.add(ivStar4)
            mViewList.add(ivStar5)
        }
        mViewList[0].setImageResource(R.drawable.star_left)
    }

    fun increaseLevel() {
        if (mLevel in 1..9) {
            mLevel++
            setStarByLevel(mLevel)
            mViewList[0].setImageResource(R.drawable.star_yellow)
        }
    }

    fun subLevel() {
        if (mLevel > 1) {
            mLevel--
            setStarByLevel(mLevel)
        }
    }

    /**
     * 返回范围 1-10
     */
    fun getLevel(): Int {
        return mLevel
    }

    /**
     * 直接设置某个值
     */
    fun setLevel(level: Int) {
        if (level in 1..10) {
            mLevel = level
            setStarByLevel(level)
        }
    }

    private fun setStarByLevel(level: Int) {
        if (level == 1) {
            mViewList[0].setImageResource(R.drawable.star_left)
            return
        }
        //全黄的star数量
        var yelllowCout = (level / 2) - 1
        if (level % 2 == 0) {
            // 2 4 6 8 10 的等级
            mViewList.forEachIndexed { index, imageView ->
                if (index <= yelllowCout) {
                    imageView.setImageResource(R.drawable.star_yellow)
                } else {
                    imageView.setImageResource(R.drawable.star_gray)
                }
            }
        } else {
            //1 3 5 7 9 的等级
            mViewList.forEachIndexed { index, imageView ->
                if (index <= yelllowCout) {
                    imageView.setImageResource(R.drawable.star_yellow)
                } else if (index == yelllowCout + 1) {
                    imageView.setImageResource(R.drawable.star_left)
                } else {
                    imageView.setImageResource(R.drawable.star_gray)

                }
            }
        }

    }
}
