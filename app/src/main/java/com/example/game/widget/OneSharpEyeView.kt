package com.example.game.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import kotlinx.android.synthetic.main.one_sharp_eye_view.view.*


/**
 * Author pan.he
 * Date 2019/04/20
 */
class OneSharpEyeView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var duration = 1800L
    var repetCount = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.one_sharp_eye_view, this, true)
    }

    private var mViewList = ArrayList<ImageView>()
    private var mViewListLittle = ArrayList<ImageView>()

    private fun initView() {
        if (mViewList.isEmpty()) {
            mViewList.add(view1)
            mViewList.add(view2)
            mViewList.add(view3)
            mViewList.add(view4)
        }
    }

    private fun initViewLittle() {
        if (mViewListLittle.isEmpty()) {
            mViewListLittle.add(view11)
            mViewListLittle.add(view22)
            mViewListLittle.add(view33)
            mViewListLittle.add(view44)
        }
    }

    fun setBackground(@DrawableRes id: Int) {
        initView()
        for (item in mViewList) {
            item.setBackgroundResource(id)
        }
    }

    /**
     * 展示指定位置的view
     */
    fun showView(index: Int) {
        initView()
        if (index in 0..mViewList.size) {
            visibility = View.VISIBLE
            for (item in mViewList) {
                if (item == mViewList[index]) {
                    item.visibility = View.VISIBLE
                } else {
                    item.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * 隐藏所有子view
     */
    fun hideAll() {
        initView()
        for (item in mViewList) {
            item.visibility = View.INVISIBLE
        }
        for (item in mViewListLittle) {
            item.visibility = View.INVISIBLE
        }
    }

    /**
     * 隐藏所有子view
     */
    private fun showAllLittle() {
        initViewLittle()
        for (item in mViewListLittle) {
            item.visibility = View.VISIBLE
        }
    }

    /**
     * 子view一起移动
     */
    fun moveChildTogether(callBack: () -> Unit) {
        initViewLittle()
        hideAll()
        showAllLittle()
        visibility = View.VISIBLE
        var width = width
        var height = height
        var centerX = width / 2
        var centerY = height / 2


        var view1 = mViewListLittle[0]
        var view2 = mViewListLittle[1]
        var view3 = mViewListLittle[2]
        var view4 = mViewListLittle[3]

        //横向,纵向相对中心点的偏移
        var absoluteTx = centerX - getViewCenter(view1).x
        var absoluteTy = centerY - getViewCenter(view1).y

//        view2.setBackgroundResource(R.drawable.icon_red_50)
//        view3.setBackgroundResource(R.drawable.icon_red_50)
//        view4.setBackgroundResource(R.drawable.icon_red_50)
//        view1.background = ColorDrawable(Color.TRANSPARENT)
//        view1.setImageResource(R.drawable.icon_red)
        val valueAnimator1 = ObjectAnimator.ofFloat(view1, "translationX", absoluteTx.toFloat())
        valueAnimator1.duration = duration
        val valueAnimator11 = ObjectAnimator.ofFloat(view1, "translationY", absoluteTy.toFloat())
        valueAnimator11.duration = duration

        val valueAnimator2 = ObjectAnimator.ofFloat(view2, "translationX", -absoluteTx.toFloat())
        valueAnimator2.duration = duration
        val valueAnimator22 = ObjectAnimator.ofFloat(view2, "translationY", absoluteTy.toFloat())
        valueAnimator22.duration = duration

        val valueAnimator3 = ObjectAnimator.ofFloat(view3, "translationX", absoluteTx.toFloat())
        valueAnimator3.duration = duration
        val valueAnimator33 = ObjectAnimator.ofFloat(view3, "translationY", -absoluteTy.toFloat())
        valueAnimator33.duration = duration


        val valueAnimator4 = ObjectAnimator.ofFloat(view4, "translationX", -absoluteTx.toFloat())
        valueAnimator4.duration = duration
        val valueAnimator44 = ObjectAnimator.ofFloat(view4, "translationY", -absoluteTy.toFloat())
        valueAnimator44.duration = duration


        val animSet = AnimatorSet()
        animSet.play(valueAnimator1).with(valueAnimator11).with(valueAnimator2).with(valueAnimator22)
            .with(valueAnimator3).with(valueAnimator33).with(valueAnimator4).with(valueAnimator44)
        animSet.start()
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (repetCount++ < 35) {
                    if (repetCount < 8) {
                        duration = 1800
                    } else if (repetCount < 16) {
                        duration = 1400
                    } else if (repetCount < 24) {
                        duration = 1000
                    } else {
                        duration = 800
                    }
                    moveChildTogether(callBack)
                } else {
                    callBack()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    /**
     * top和left表示的是原始左上角的位置信息，其值不会发生改变。
     * 而在平移过程中发生改变的是x,y,translationX,translationY。
     * x,y是View左上角的坐标，translationX,translationY是平移的距离。即
     *
     * x = left + translationX
     * y = top + translationY
     */

    fun getViewCenter(view: View): Point {
        var x = view.x
//        var left = view.left
//        var tx = view.translationX

        var y = view.y

        var width = view.measuredWidth
        var height = view.measuredHeight

        var point = Point((x + width / 2).toInt(), (y + height / 2).toInt())
        return point
    }
}
