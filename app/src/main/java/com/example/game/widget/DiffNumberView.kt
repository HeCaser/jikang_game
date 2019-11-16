package com.example.game.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.game.R
import kotlinx.android.synthetic.main.diff_number_view.view.*
import kotlin.random.Random

/**
 * Author pan.he
 * 差异数字viwe
 */
class DiffNumberView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        val BG_NORMAL = 0
        val BG_SELECTED = 1
        val BG_BLUE_HINT = 2
    }

    private val mBgList = arrayListOf(
        R.drawable.circle_solid_white_border_gray,
        R.drawable.circle_solid_yellow_border_gray,
        R.drawable.circle_solid_white_border_blue
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.diff_number_view, this, true)
        initView()
    }

    private fun initView() {
//        llNumParent.setBackgroundResource(mBgList[1])
    }

    fun setTextNumber(lenght: Int, isDiff: Boolean) {
        val buffer = StringBuffer()
        var count = lenght
        while (count >= 1) {
            count--
            if (count == lenght - 1) {
                //首位不要0
                buffer.append(Random.nextInt(1, 10))
            } else {
                buffer.append(Random.nextInt(0, 10))
            }
        }
        tvNumber1.text = buffer.toString()

        if (isDiff) {
            //替换其中一位数字,使得两个值不同
            val pos = Random.nextInt(lenght)
            var num = buffer[pos].toInt() - 48
            var repleace = 0
            while (repleace == num || repleace == 0) {
                repleace = Random.nextInt(10)
            }
            buffer.replace(pos, pos + 1, repleace.toString())
        }
        tvNumber2.text = buffer.toString()
    }


    var isChoosed = false
    /**
     * @param state 1
     *
     */
    fun setBg(state: Int) {
        if (isChoosed){
            return
        }
        if (state in 0..mBgList.size) {
            llNumParent.setBackgroundResource(mBgList[state])
        }
    }



}
