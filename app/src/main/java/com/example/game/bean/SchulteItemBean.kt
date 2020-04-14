package com.example.game.bean

import android.graphics.Color

data class SchulteItemBean(
        //位置
        val number: Int,
        //真正展示的内容
        var showText: String,
        //文字是否彩色
        var isFullColor :Boolean=false,
        var fullColor:Int= Color.BLACK

)