package com.example.game.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.NumberBean
import com.example.game.util.dp2px
import kotlinx.android.synthetic.main.schulte_attention_item.view.*
import me.drakeet.multitype.ItemViewBinder

class SchulteAttentionBinder(var span: Int, var callBack: (phone: Int) -> Unit) :
        ItemViewBinder<NumberBean, SchulteAttentionBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.schulte_attention_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: NumberBean) {
        holder.mData = item
        holder.setData()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var mData: NumberBean
        fun setData() {
            with(itemView) {
                tvNumber.text = "${mData.number}"
                var size = context.dp2px(50f - (span - 2) * 3)
                tvNumber.textSize = size.toFloat()
                if (mData.isSelected) {
                    tvNumber.setBackgroundColor(Color.WHITE)
                    tvNumber.setTextColor(context.resources.getColor(R.color.colorPrimary))
                } else {
                    tvNumber.setTextColor(Color.WHITE)
                    tvNumber.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                if (mData.isHidden){
                    tvNumber.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    tvNumber.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                itemView.setOnClickListener {
                    callBack(adapterPosition)
                }
            }
        }
    }
}