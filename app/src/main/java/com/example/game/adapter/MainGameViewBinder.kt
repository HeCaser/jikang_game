package com.example.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.GameBean
import me.drakeet.multitype.ItemViewBinder

class MainGameViewBinder(var callBack: (phone: String) -> Unit) : ItemViewBinder<GameBean, MainGameViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.main_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: GameBean) {
//        holder.fooView.text = item.gameName
//        holder.fooView.setTextColor(item.tvColor)
        holder.ivGameIcon.setImageResource(item.gameIconId)
        holder.itemView.setOnClickListener {
            callBack(item.gameName)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fooView: TextView = itemView.findViewById(R.id.tvTitle)
        val ivGameIcon: ImageView = itemView.findViewById(R.id.ivGameIcon)
    }
}