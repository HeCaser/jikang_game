package com.example.game.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.game.R
import com.example.game.utils.gone
import com.example.game.utils.show
import kotlinx.android.synthetic.main.activity_one_sharp_eye.*


class OneSharpEyeActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            Intent(ctx, OneSharpEyeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    private var TIME_SPACE = 1500L
    private var mTime = 3
    private var index = 1
    private var WAHT_COUNT_DOWN = 1
    private var mTimeList = arrayListOf(60, 80, 80, 80)
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            mTime--
            if (mTime in 1..2) {
                tvTime.text = mTime.toString()
                sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
            } else {
                mTime = 3

                when (index) {
                    1 -> startShareEyeOne()
                    2 -> startTwo()
                    3 -> startThree()
                    4 -> startFour()
                    5 -> {
                        startFive()
                    }
                    6 -> finish()
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_sharp_eye)
        mHandler.sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
//        startShareEyeOne()


    }

    /**
     * 第一个游戏,红色球. 左上角,右上角,左下角,右下角.循环
     * 速度变化
     */
    private fun startShareEyeOne() {
        tvHint.gone()
        tvTime.gone()
        sharpView.showView(0)
        var currentValue: Int

        var mValue1 = generateAnimation(0, 396, mTimeList[0] * 1000L)
        mValue1?.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int

            var range = 0
            if (currentValue < 150) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(1)
                } else if (range < 9) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            } else if (currentValue < 260) {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(1)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            } else {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(1)
                } else if (range < 9) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            }

        }


        val animSet = AnimatorSet()
        animSet.play(mValue1)
        animSet.start()
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                index = 2
                mHandler.sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
                tvHint.show("第二组")
                tvTime.show(mTime.toString())
                sharpView.hideAll()
            }
        })

    }

    @SuppressLint("ResourceType")
    private fun startTwo() {
        sharpView.setBackground(R.drawable.icon_green)
        tvHint.gone()
        tvTime.gone()
        sharpView.showView(0)
        var currentValue: Int

        var mValue1 = generateAnimation(0, 528, mTimeList[1] * 1000L)
        mValue1?.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int

            var range = 0
            if (currentValue < 132) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(3)
                } else if (range < 9) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(1)
                }
            } else if (currentValue < 264) {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(3)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(1)
                }
            } else if (currentValue < 396) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(3)
                } else if (range < 9) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(1)
                }
            } else {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(3)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(1)
                }
            }
        }

        mValue1.start()
        mValue1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                index = 3
                mHandler.sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
                tvHint.show("第三组")
                tvTime.show(mTime.toString())
                sharpView.hideAll()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

    }

    @SuppressLint("ResourceType")
    private fun startThree() {
        sharpView.setBackground(R.drawable.icon_blue)
        tvHint.gone()
        tvTime.gone()
        sharpView.showView(0)
        var currentValue: Int

        var mValue1 = generateAnimation(0, 528, mTimeList[2] * 1000L)
        mValue1?.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int

            var range = 0
            if (currentValue < 132) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(1)
                } else if (range < 9) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            } else if (currentValue < 264) {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(1)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            } else if (currentValue < 396) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(1)
                } else if (range < 9) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            } else {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(1)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else {
                    sharpView.showView(3)
                }
            }
        }

        mValue1.start()
        mValue1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                index = 4
                mHandler.sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
                tvHint.show("第四组")
                tvTime.show(mTime.toString())
                sharpView.hideAll()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

    }

    private fun startFour() {
        sharpView.setBackground(R.drawable.icon_orange)
        tvHint.gone()
        tvTime.gone()
        sharpView.showView(0)
        var currentValue: Int

        var mValue1 = generateAnimation(0, 528, mTimeList[3] * 1000L)
        mValue1?.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int

            var range = 0
            if (currentValue < 132) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else if (range < 9) {
                    sharpView.showView(3)
                } else {
                    sharpView.showView(1)
                }
            } else if (currentValue < 264) {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(2)
                } else if (range < 6) {
                    sharpView.showView(3)
                } else {
                    sharpView.showView(1)
                }
            } else if (currentValue < 396) {
                range = currentValue % 12
                if (range < 3) {
                    sharpView.showView(0)
                } else if (range < 6) {
                    sharpView.showView(2)
                } else if (range < 9) {
                    sharpView.showView(3)
                } else {
                    sharpView.showView(1)
                }
            } else {
                range = currentValue % 8
                if (range < 2) {
                    sharpView.showView(0)
                } else if (range < 4) {
                    sharpView.showView(2)
                } else if (range < 6) {
                    sharpView.showView(3)
                } else {
                    sharpView.showView(1)
                }
            }
        }

        mValue1.start()
        mValue1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                index = 5
                mHandler.sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
                tvHint.show("第五组")
                tvTime.show(mTime.toString())
                sharpView.hideAll()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

    }

    /**
     * 四个小球动画
     */
    private fun Handler.startFive() {
        tvHint.gone()
        tvTime.gone()
        sharpView.moveChildTogether {
            sharpView.hideAll()
            index = 6
            sendEmptyMessageDelayed(WAHT_COUNT_DOWN, TIME_SPACE)
            tvHint.show("很棒, 已完成全部训练.")
            tvTime.show(mTime.toString())
            sharpView.hideAll()
        }
    }

    private fun generateAnimation(start: Int, end: Int, duration: Long): ValueAnimator {
        var mValue1 = ValueAnimator.ofInt(start, end)
        mValue1.duration = duration
        mValue1.interpolator = LinearInterpolator()
        return mValue1
    }

}
