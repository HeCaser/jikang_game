package com.example.game.adapter

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
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
                //避免复用产生的问题
                tvBgError.visibility = View.INVISIBLE
                tvBg.visibility = View.VISIBLE
                squareParent.setOnClickListener {
                    if (!mData.isFaceShow) {
                        callBack(mData)
                    }
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

        //展示错误
        fun showError() {
            with(itemView) {
                tvBgError.visibility = View.VISIBLE
            }
        }

        //展示正面,临时展示后需要还原为背面
        fun showTempFace(pos: Int) {
            with(itemView) {

                val scaleX = ObjectAnimator.ofFloat(clParent, "scaleX",  0.8f,1f,1.0f)
                val scaleY = ObjectAnimator.ofFloat(clParent, "scaleY", 0.8f, 1f,1.0f)
                val set = AnimatorSet()
                set.duration = 1000
                set.interpolator = AccelerateDecelerateInterpolator()
                set.playTogether(scaleX, scaleY)
                set.addListener(object :Animator.AnimatorListener{
                    override fun onAnimationEnd(animation: Animator?) {
                        tvBg.visibility = View.VISIBLE
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        tvBg.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }
                })
                set.start()
//                postDelayed({set.start()},pos*100L)

            }
        }
    }
}