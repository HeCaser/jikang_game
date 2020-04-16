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
import kotlinx.android.synthetic.main.number_25_item.view.*
import me.drakeet.multitype.ItemViewBinder

class Number25ItemBinder(var callBack: (phone: Int) -> Unit) :
    ItemViewBinder<NumberBean, Number25ItemBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.number_25_item, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, item: NumberBean) {
        holder.mData = item
        holder.setData()
    }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var mData: NumberBean
        fun setData() {
            with(itemView) {
                val half = 3f //item 之间的间隔,
                var width = screenWidth
                //减去item直接的间隔 以及 rv 距离左右两边的距离(3dp)
                width -= context.dp2px(half)*6
                var itemWidth = width/5

                var size2 = width/ 10
                with(tvNumberView.getTv()){
                    setTextSize(TypedValue.COMPLEX_UNIT_PX,size2.toFloat())
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