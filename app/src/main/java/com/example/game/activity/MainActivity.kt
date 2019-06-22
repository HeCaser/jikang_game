package com.example.game.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.adapter.MainStringViewBinder
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.MultiTypeAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mItems: MutableList<Any>
    private val mGames = arrayListOf(
        "济康1-100", "1SharpEye", "舒尔特注意力", "舒尔特方格", "Num25","搜索词"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mAdapter = MultiTypeAdapter()
        mAdapter.register(MainStringViewBinder {
            goToGame(it)
        })
        mItems = ArrayList(mGames)

        rvGameNames.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        }
        mAdapter.items = mItems
        mAdapter.notifyDataSetChanged()

    }

    private fun goToGame(name: String) {
        when (name) {
            mGames[0] -> {
                OneToOneHundredActivity.start(this)
            }
            mGames[1] -> {
                OneSharpEyeActivity.start(this)
            }
            mGames[2] -> {
                SchulteAttentionActivity.start(this)
            }
            mGames[3] -> {
                SchulteGridActivity.start(this)
            }
            mGames[4] -> {
                Num25Activity.start(this)
            }
            mGames[5] -> {
                SearchWordActivity.start(this,"",0L)
            }
        }

    }
}
