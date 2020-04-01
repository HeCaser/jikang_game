package com.example.game.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.SchulteItemBean
import com.example.game.util.dp2px
import com.example.game.util.screenWidth
import kotlinx.android.synthetic.main.schulte_item.view.*
import me.drakeet.multitype.ItemViewBinder
import kotlin.math.sqrt

class SchulteBinder(var callBack: (phone: Int) -> Unit) :
        ItemViewBinder<SchulteItemBean, SchulteBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.schulte_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: SchulteItemBean) {
        holder.mData = item
        holder.setData()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var mData: SchulteItemBean
        fun setData() {
            with(itemView) {
                tvNumber.text = "${mData.showText}"

                //根据屏幕宽度,item间隔,每行item数量确定文字尺寸
                var number =adapterItems.size
                var screenWidth = screenWidth
                screenWidth-=context.dp2px(60f) //减去rv的padding
                var size2 = screenWidth/ sqrt(number.toDouble())
                tvNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,size2.toFloat())

                itemView.setOnClickListener {
                    callBack(adapterPosition)
                }
            }
        }
    }
}