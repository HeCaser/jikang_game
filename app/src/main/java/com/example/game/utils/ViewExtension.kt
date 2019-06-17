package com.example.game.utils

import android.view.View
import android.widget.TextView

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
