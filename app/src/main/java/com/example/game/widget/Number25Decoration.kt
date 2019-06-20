package com.example.game.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.game.util.dp2px

class Number25Decoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val half = 3f
        val pos = parent.getChildAdapterPosition(view)
        if (pos%5==0){
            outRect.right = view.context.dp2px(half)
        }else if (pos%5==4){
            outRect.left = view.context.dp2px(half)
        }else {
            outRect.left = view.context.dp2px(half)
            outRect.right = view.context.dp2px(half)
        }
        
        outRect.bottom=view.context.dp2px(half*2)
    }
}