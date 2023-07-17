package com.example.game.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.game.constant.PREFS_KEY
import com.example.game.utils.SaveSpData
import java.util.regex.Pattern


val Context.sp: SaveSpData get() = SaveSpData(applicationContext)
fun Context.getSharedPrefs() = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        Toast.makeText(applicationContext, msg, length).show()
    } catch (e: Exception) {
    }
}

inline fun Fragment.toastCenter(msg: String) {
    this.activity?.let {
        it.toastCenter(msg)
    }
}

fun Int.dp(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)

inline fun Context.toastCenter(msg: String?) {
    if (TextUtils.isEmpty(msg)) {
        return
    }

    var toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)

    var textView = TextView(this)
    textView.setTextColor(Color.WHITE)

    val drawable = GradientDrawable()
    drawable.setColor(Color.parseColor("#aa000000"))
    drawable.cornerRadius = dp2px(4F).toFloat()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        textView.background = drawable
    } else {
        textView.setBackgroundDrawable(drawable)
    }


    val tb = dp2px(12F)
    val lr = dp2px(24F)

    textView.setPadding(lr, tb, lr, tb)

    textView.text = msg

    toast.view = textView

    toast.setGravity(Gravity.CENTER, 0, 0)

    toast.show()
}


fun Context.res2color(@ColorRes colorResId: Int): Int = this.resources.getColor(colorResId)

fun Context.res2String(@StringRes id: Int): String = this.resources.getString(id)


fun Context.px2dp(pxValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.dp2px(dipValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}


val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels
val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels

fun Context.screenWidth(): Int = this.resources.displayMetrics.widthPixels
fun Context.screenHeight(): Int = this.resources.displayMetrics.heightPixels


fun Context.getDimen(@DimenRes id: Int): Float {
    return this.resources.getDimension(id)
}

