package com.example.game.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.game.R
import com.example.game.constant.TITLE_MAX
import com.example.game.util.res2String
import com.example.game.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_gradient_base.*
import kotlinx.android.synthetic.main.toolbar_view.*

open class BaseActivity : AppCompatActivity() {


    override fun onStart() {
        hideSoftInput()
        super.onStart()
    }

    override fun finish() {
        hideSoftInput()
        super.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        StatusBarUtils.setStatusBarTransparentAndFullScreen(this)
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_gradient_base)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        StatusBarUtils.addStatusBarHeightToPaddingByView(this, flToolbar)
        ivBack.setOnClickListener {
            finish()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun setContentView(layoutResID: Int) {
        layoutInflater.inflate(layoutResID, toolbarBaseRootView, true)
    }

    fun setCenterTitle(@StringRes resId: Int) {
        setCenterTitle(res2String(resId))
    }

    fun setCenterTitle(title: String) {
        var newTitle = title
        if (title.length > TITLE_MAX) {
            newTitle = title.substring(0, TITLE_MAX) + "..."
        }
        tvTitle.text = newTitle
    }

    fun setRightTitle(@StringRes resId: Int) {
        tvRightTitle.setText(resId)
    }

    fun setRightTitle(title: String) {
        tvRightTitle.text = title
    }

    fun hideToolBar(){
        flToolbar.visibility = View.GONE
    }
    fun hideSoftInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        }
    }

    fun showSoftInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.showSoftInput(currentFocus, 0)
            }
        }
    }

    fun getTrimText(tv: TextView): String {
        return tv.text.toString()
    }

}