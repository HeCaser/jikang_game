package com.example.game.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_view_test.*
import android.text.TextPaint
import android.widget.TextView
import androidx.core.view.get
import com.example.game.R


/**
 *测试类
 */
class ViewTestActivity : BaseActivity() {

    companion object {


        fun start(ctx: Context) {
            Intent(ctx, ViewTestActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_test)
        initViewAndData()
        initListener()

    }

    private fun initViewAndData() {

    }

    private fun initListener() {
        clContent.setOnClickListener {
            initShowView()
        }
    }


    private fun initShowView() {
        val text1 = "我的"
        val text2 = "我额,"
        val text3 = "我。"

        val length1 = getTextWidth(text1, subFieldView.getChild(0).textSize)
        val length2 = getTextWidth(text2, subFieldView.getChild(0).textSize)
        val length3 = getTextWidth(text3, subFieldView.getChild(0).textSize)

        subFieldView.setContent(text1,text2,text3)
        val tv = subFieldView.getChild(1)
//        tv.setPadding((length2/3).toInt(), 0, 0, 0)
        println("length=$length1 $length2 $length3")

    }

    fun getTextWidth(text: String, textSize: Float): Float {
        val paint = TextPaint()
        paint.textSize = textSize
        return paint.measureText(text)
    }


}
