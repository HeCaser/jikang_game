package com.example.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.SchulteItemBean
import kotlinx.android.synthetic.main.schulte_item.view.*
import me.drakeet.multitype.ItemViewBinder

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
                itemView.setOnClickListener {
                    callBack(adapterPosition)
                }
            }
        }
    }
}