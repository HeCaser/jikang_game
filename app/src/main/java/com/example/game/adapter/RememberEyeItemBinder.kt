package com.example.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.bean.RememberEyeBean
import kotlinx.android.synthetic.main.remember_eye_item.view.*
import me.drakeet.multitype.ItemViewBinder

class RememberEyeItemBinder(var callBack: (bean: RememberEyeBean) -> Unit) :
    ItemViewBinder<RememberEyeBean, RememberEyeItemBinder.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.remember_eye_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: RememberEyeBean) {
        holder.mData = item
        holder.setData()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var mData: RememberEyeBean
        fun setData() {
            println("hepan总数=${adapterItems.size}")
            with(itemView) {
                //               var para =  tvText.layoutParams
//                val width = ScreenUtils.getScreenSize(context).x*0.8f
//                val total = Math.sqrt(adapterItems.size.toDouble())
//                para.height= (width/total).toInt()
//                para.width= (width/total).toInt()

                squareParent.setOnClickListener {
                    callBack(mData)
                }

                if(mData.isFaceShow){
                    showFace()
                }
            }
        }

        //展示背景,默认状态
        fun showBg() {
            with(itemView) {
                tvBg.visibility = View.VISIBLE
            }
        }

        //展示正面
        fun showFace() {
            with(itemView) {
                tvBg.visibility = View.GONE
            }
        }

        //展示正面,临时展示后需要还原为背面
        fun showTempFace() {
            with(itemView) {
                tvBg.visibility = View.GONE

                postDelayed({ tvBg.visibility = View.VISIBLE }, 1000)
            }
        }
    }
}