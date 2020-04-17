package com.example.game.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.sqrt

class SchulteDecoration : RecyclerView.ItemDecoration() {

    var mPaint = Paint()

    init {
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.color = Color.BLACK
    }

    /**
     *
     * 在 item 之前绘制. n*n 宫格
     * 使用局限性: 标准 n*n 展示, 按照宽度来判断横线间距
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
       var totalCount: Int? = parent.adapter?.itemCount ?: return

        var row = sqrt(totalCount!!.toDouble()).toInt()
        var phw = parent.measuredWidth
        var span = phw/row

        for (i in 0 .. row){
            //横线
            c.drawLine(0.0f, (i*span+1).toFloat(), phw.toFloat(), (i*span+1).toFloat(), mPaint)
            //竖线
            c.drawLine((i*span+1).toFloat(),0f, (i*span+1).toFloat(), phw.toFloat(),mPaint)
        }
    }
}