package com.example.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import me.drakeet.multitype.ItemViewBinder

class MainStringViewBinder(var callBack: (phone: String) -> Unit) : ItemViewBinder<String, MainStringViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.main_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: String) {
        holder.fooView.text = item
        holder.fooView.setOnClickListener {
            callBack(item)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fooView: TextView = itemView.findViewById(R.id.tvTitle)
    }
}