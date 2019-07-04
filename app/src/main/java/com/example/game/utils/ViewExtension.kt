package com.example.game.utils

import android.view.View
import android.widget.TextView
import java.lang.StringBuilder

/**
 * Author pan.he
 * Date 2018/12/27
 */
var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.isInVisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

fun View.gone() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.show(content: String) {
    this.visibility = View.VISIBLE
    if (this is TextView) {
        this.text = content
    }
}

val TextView.content: String
    get() {
        return this.text.toString().trim()
    }

/**
 * 给tv的文本直接加上空格
 */
fun TextView.setLetterSpacingText(text: String) {
    val size = text.length
    val builder = StringBuilder()
    for (i in 0 until size) {
        builder.append(text[i])
        if (i!=size){
            builder.append(" ")
        }
    }
    this.text = builder.toString()
}