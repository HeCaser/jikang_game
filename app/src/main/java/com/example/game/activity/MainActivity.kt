package com.example.game.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.game.BuildConfig
import com.example.game.R
import com.example.game.adapter.MainGameViewBinder
import com.example.game.bean.GameBean
import com.example.game.constant.*
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.MultiTypeAdapter

class MainActivity : BaseActivity() {

    private lateinit var mAdapter: MultiTypeAdapter
    private var mItems = ArrayList<GameBean>()

    private val mGamesALl = arrayListOf(

        "sharp eye",
        "1-100",
        "舒尔特注意力",
        "舒尔特",
        "舒尔特方格",
        "节拍器",


        "过目不忘",
        "垂直练习",
        "NUM25",
        "甄选奇数和偶数",
        "差异数",
        "EBook循环",
        "EBook分栏",
        "记忆数",
        "速度数",
        "搜索词",
        "搜索数",
        "EBook树形"
    )

    private val mGamesIcon = arrayListOf(

        R.drawable.sharpeye,
        R.drawable.one2100,
        R.drawable.zhuyiili,
        R.drawable.shuert,
        R.drawable.shuetfg,
        R.drawable.jiepai,


//        "过目不忘",
        R.drawable.guomubuwang,
//        "垂直练习",
        R.drawable.chuizhilianxi,
//        "NUM25",
        R.drawable.num25,
//        "甄选奇数和偶数",
        R.drawable.jioushu,
//        "差异数",
        R.drawable.chayishu,
//        "EBook循环",
        R.drawable.exunhuan,
//        "EBook分栏",
        R.drawable.efenlan,
//        "记忆数",
        R.drawable.jiiyiishu,
//        "速度数",
        R.drawable.sudushu,
//        "搜索词",
        R.drawable.sousuoci,
//        "搜索数",
        R.drawable.sousuoshu,
//        "EBook树形"
        R.drawable.shuxing
    )
    private val mGames = arrayListOf<String>()

//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideToolBar()
        initView()

    }

    private val partOneIndex = 6

    private fun initView() {
        when (BuildConfig.GAME_TYPE) {
            0 -> {
                mGames.addAll(mGamesALl)
            }
            1 -> {
                //初级版
                mGames.addAll(mGamesALl.subList(0, partOneIndex))
            }
            2 -> {
                //升级版
                mGames.addAll(mGamesALl.subList(partOneIndex, mGamesALl.size))
                ivTitle.setImageResource(R.drawable.advance_title)
                clParent.setBackgroundResource(R.drawable.advance_ng)
            }

        }

//        mGames.clear();
//        mGames.addAll(mGamesALl)

//        setCenterTitle("济康学习包")
        mAdapter = MultiTypeAdapter()
        mAdapter.register(MainGameViewBinder {
            goToGame(it)
        })
        mGames.forEachIndexed { index, it ->
            val iconIndex = if (BuildConfig.GAME_TYPE == 2) index + partOneIndex else index
            mItems.add(GameBean(it, getColor(it), mGamesIcon[iconIndex]))
        }
//        mGames.forEach {
//
//        }

        val mManager = GridLayoutManager(this, 2)
        val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 根据实际情况处理需要返回的值.
                //例如我的代码中不同data类型对应不同的viewtype. 因此客户根据数据类型返回
//                val item = mItems[position]
//                if ("初级版" == item.gameName || "升级版"==item.gameName) {
//                    return 2
//                }
                return 1
            }
        }
        mManager.spanSizeLookup = spanSizeLookup

        rvGameNames.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = mManager
        }
        mAdapter.items = mItems
        mAdapter.notifyDataSetChanged()

    }

    private fun getColor(name: String): Int {
        var color: Int
        when (name) {
            "sharp eye", "1-100", "舒尔特注意力", "舒尔特方格", "舒尔特" -> {
                //蓝色
                color = 0xff098dfa.toInt()
            }
            "记忆数", "速度数", "节拍器", "过目不忘" -> {
                //浅蓝色
                color = 0xff16c1fd.toInt()
            }
            "第一部分", "第二部分" -> {
                //黑色
                color = 0xFF666666.toInt()
            }
            else -> {
                //橙色
                color = 0xFFFC992C.toInt()
            }
        }
        return color
    }

    private fun goToGame(name: String) {
        when (name) {
            "1-100" -> {
                OneToOneHundredActivity.start(this)
            }
            "sharp eye" -> {
                OneSharpEyeActivity.start(this)
            }
            "舒尔特注意力" -> {
                SchulteAttentionActivity.start(this)
            }
            "舒尔特方格" -> {
                SchulteGridActivity.start(this)
            }
            "NUM25" -> {
                Num25Activity.start(this)
            }
            "搜索词" -> {
                SelectStarActivity.start(this, SEARCH_TYPE_WORD)
            }
            "搜索数" -> {
                SelectStarActivity.start(this, SEARCH_TYPE_NUMBER)
            }
            "甄选奇数和偶数" -> {
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
            "差异数" -> {
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
            "舒尔特" -> {
                SchulteActivity.start(this)
            }
        }

    }

}
