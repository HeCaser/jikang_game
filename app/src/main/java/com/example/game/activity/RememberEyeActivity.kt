package com.example.game.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.game.R
import com.example.game.adapter.RememberEyeItemBinder
import com.example.game.bean.RememberEyeBean
import kotlinx.android.synthetic.main.activity_remember_eye.*
import me.drakeet.multitype.MultiTypeAdapter


/**
 * 第四阶段 过目不忘
 */
class RememberEyeActivity : BaseActivity() {

    private lateinit var mAdapter: MultiTypeAdapter

    private var SPAN_COUNT = 3
    lateinit var list: ArrayList<RememberEyeBean>

    companion object {
        fun start(ctx: Context) {
            Intent(ctx, RememberEyeActivity::class.java).apply {
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remember_eye)
        initViewAndData()
        initListener()
    }

    private fun initViewAndData() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCenterTitle("济康-过目不忘")

        mAdapter = MultiTypeAdapter()
        mAdapter.register(RememberEyeItemBinder {
            //            SPAN_COUNT = 2
//            recyclerView.layoutManager = GridLayoutManager(this@RememberEyeActivity, SPAN_COUNT)
//            mAdapter.notifyDataSetChanged()
            test()
        })
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@RememberEyeActivity, SPAN_COUNT)
//            addItemDecoration(NumberItemDecoration())
        }

        list = arrayListOf<RememberEyeBean>()
        for (count in 0 until SPAN_COUNT * SPAN_COUNT) {
            list.add(RememberEyeBean(0))
        }

        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }

    private fun initListener() {

    }

    private fun test() {
        getHorder(0)?.showTempFace()
    }


    private fun getHorder(pos: Int): RememberEyeItemBinder.ViewHolder? {
        val view = recyclerView.getChildAt(0)
        val horder = recyclerView.getChildViewHolder(view)
        return horder as? RememberEyeItemBinder.ViewHolder

    }
}
