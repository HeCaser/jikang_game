package com.example.game.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.game.R
import com.example.game.utils.ScreenUtils
import kotlin.math.abs

/**
 * 节拍器的速度控制 View
 * 可以
 */
class BeatSpeedView : View {

    private lateinit var mPaint: Paint
    private var mPreX = 0f
    private var mPreY = 0f
    private var mWidth = 0
    private var mHeight = 0
    //绘图半径,最小宽度的0.45
    var radius = 0f

    //波动范围 0-100,如果需要其他范围请自行在外转换
    private val MAX = 360f
    private val MIN = 1f
    private var mPercent = 55f

    //回调
    private var mCallBack: ((p: Float) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = height
        radius = mWidth.coerceAtMost(mHeight) * 0.45f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawInnerArc(canvas)
        drawOutArc(canvas)

    }

    //内部弧线
    private fun drawInnerArc(canvas: Canvas) {
        mPaint.color = Color.argb(255, 60, 166, 208)
        mPaint.style = Paint.Style.STROKE
        canvas.save()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        canvas.rotate(135f)
        mPaint.strokeWidth = ScreenUtils.dip2px(context, 2f).toFloat()
        val arcRadius = radius * 0.8f
        canvas.drawArc(
            RectF(-arcRadius, -arcRadius, arcRadius, arcRadius), 0f, 270f, false, mPaint
        )
        canvas.restore()
    }

    //外部弧线
    private fun drawOutArc(canvas: Canvas) {
        mPaint.color = Color.argb(255, 100, 117, 135)
        mPaint.style = Paint.Style.STROKE

        canvas.save()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        canvas.rotate(135f)
        mPaint.strokeWidth = ScreenUtils.dip2px(context, 5f).toFloat()
        //背景
        val arcRadius = radius * 0.9f
        canvas.drawArc(
            RectF(-arcRadius, -arcRadius, arcRadius, arcRadius), 0f, 270f, false, mPaint
        )
        //显示比例
        mPaint.color = resources.getColor(R.color.colorPrimary)
        val sweep = mPercent / MAX * 270f
        canvas.drawArc(
            RectF(-arcRadius, -arcRadius, arcRadius, arcRadius), 0f, sweep, false, mPaint
        )
        canvas.restore()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPreX = event.x
                mPreY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val diffX = x - mPreX
                val diffY = y - mPreY
                // 是否是水平移动
                val isHorizon = abs(diffX) > abs(diffY)
                val gate = 10
                if (isHorizon) {
                    //以水平方向移动为准
                    if (diffX > gate) {
                        addPercent(2)
                    } else if (diffX < -gate) {
                        subPercent(2)
                    }
                } else {
                    //以竖直方向为准
                    if (diffY > gate) {
                        subPercent(2)
                    } else if (diffY < -gate) {
                        addPercent(2)
                    }
                }

                mPreX = event.x
                mPreY = event.y
            }
            MotionEvent.ACTION_UP -> {
                mCallBack?.invoke(mPercent)
            }
        }

        return true
    }

    fun subPercent(step: Int = 1) {
        mPercent -= step
        if (mPercent < MIN) {
            mPercent = MIN
        }
        postInvalidate()
        mCallBack?.invoke(mPercent)
    }

    fun addPercent(step: Int = 1) {
        mPercent += step
        if (mPercent > MAX) {
            mPercent = MAX
        }
        postInvalidate()
        mCallBack?.invoke(mPercent)
    }

    /**
     * 设置回调
     */
    fun setCallBack(call: (p: Float) -> Unit) {
        mCallBack = call
    }


    fun setPercent(p: Float) {
        if (p > MAX || p < MIN) {
            return
        }
        mPercent = p
        postInvalidate()
        mCallBack?.invoke(mPercent)
    }

    fun getPercent(): Float {
        return mPercent
    }

}