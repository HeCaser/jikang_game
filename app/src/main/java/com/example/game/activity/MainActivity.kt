package com.example.game.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.adapter.MainStringViewBinder
import com.example.game.constant.*
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.MultiTypeAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mItems: MutableList<Any>
    private val mGames = arrayListOf(
        "节拍器","舒尔特",
        "济康1-100", "1SharpEye", "舒尔特注意力",
        "舒尔特方格", "Num25", "搜索词",
        "搜索数", "奇偶数", "差异数字",
        "垂直练习", "EBook树形", "EBook循环",
        "EBook分栏", "速度数", "记忆数", "过目不忘"
    )
//

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
            "济康1-100" -> {
                OneToOneHundredActivity.start(this)
            }
            "1SharpEye" -> {
                OneSharpEyeActivity.start(this)
            }
            "舒尔特注意力" -> {
                SchulteAttentionActivity.start(this)
            }
            "舒尔特方格" -> {
                SchulteGridActivity.start(this)
            }
            "Num25" -> {
                Num25Activity.start(this)
            }
            "搜索词" -> {
                SelectStarActivity.start(this, SEARCH_TYPE_WORD)
            }
            "搜索数" -> {
                SelectStarActivity.start(this, SEARCH_TYPE_NUMBER)
            }
            "奇偶数" -> {
                SelectStarActivity.start(this, SEARCH_TYPE_JIOU)
            }
            "EBook树形" -> {
                BeforeEBookActivity.start(this, EBOOK_TREE)
            }
            "EBook循环" -> {
                BeforeEBookActivity.start(this, EBOOK_LOOP)
            }
            "EBook分栏" -> {
                BeforeEBookActivity.start(this, EBOOK_SUBFIELD)
            }
            "差异数字" -> {
                SelectStarActivity.start(this, SEARCH_DIFF_NUMBER)
            }
            "垂直练习" -> {
                VerticalPracticeActivity.start(this)
            }
            "速度数" -> {
                SelectStarActivity.start(this, SEARCH_SPEED_NUMBER)
            }
            "记忆数" -> {
                SelectStarActivity.start(this, SEARCH_REMEMBER_NUMBER)
            }
            "过目不忘" -> {
                RememberEyeActivity.start(this)
            }
            "节拍器" -> {
                BeatPracticeActivity.start(this)
            }
            "舒尔特"->{
                SchulteActivity.start(this)
            }

        }

    }
}
