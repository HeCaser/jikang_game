package com.example.game.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.game.util.dp

/**
 *  clipPath 实现裁切的问题
 *  1 有毛边
 *  2 不能裁切 background
 */
class RoundedRectangleImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    public var radius = 4.dp()
    private var mWidth = 0
    private var mHeight = 0
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }


    override fun onDraw(canvas: Canvas) {
        if (mWidth==0 || mHeight==0){return}
        val min = mWidth.coerceAtMost(mHeight)
        path.addRoundRect(RectF(0f,0f,mWidth.toFloat(),mHeight.toFloat()),radius,radius,Path.Direction.CW)

        canvas.clipPath(path)
        super.onDraw(canvas)

    }
}