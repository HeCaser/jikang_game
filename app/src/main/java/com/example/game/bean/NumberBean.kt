package com.example.game.bean

data class NumberBean(
        val number: Int,
        var isSelected: Boolean,
        var isError: Boolean = false,

        //是否隐藏数字,优先级大于 isSelected. 默认不隐藏
        var isHidden: Boolean = false
)