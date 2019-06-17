package com.example.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.NumberBean
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
                tvNumberView.setData(mData)
                tvNumberView.setOnClickListener {
                    callBack(adapterPosition)
                }
            }
        }
    }
}