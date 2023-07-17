package com.example.game.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.NumberBean
import com.example.game.util.dp2px
import com.example.game.util.screenWidth
import kotlinx.android.synthetic.main.number_item.view.*
import me.drakeet.multitype.ItemViewBinder

class NumberViewBinder(var callBack: (phone: Int) -> Unit) :
    ItemViewBinder<NumberBean, NumberViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.number_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: NumberBean) {
        holder.mData = item
        holder.setData()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var mData: NumberBean
        fun setData() {


            with(itemView) {

                    val half = 3f //item 之间的间隔
                    var width = context.screenWidth()
                    //减去item直接的间隔,以及rv左右间距
                    width -= context.dp2px(half) * 11
                    var itemWidth = width / 10

                    var size2 = width / 22
                    with(tvNumberView.getTv()) {
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, size2.toFloat())
                        var para = layoutParams
                        para.height = itemWidth
//                    layoutParams = para
                    }

                    tvNumberView.setData(mData)
                    tvNumberView.setOnClickListener {
                        callBack(adapterPosition)
                    }
                }

            }
        }

}