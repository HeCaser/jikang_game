package com.example.game.utils

import android.content.Context
import com.example.game.constant.ONE_TO_HUNDRED_SCORE
import com.example.game.constant.ONE_TO_HUNDRED_TIME
import com.example.game.constant.PREFS_KEY

class SaveSpData(val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    companion object {
        fun newInstance(context: Context) = SaveSpData(context)
    }

    var oneToHundredRecord: String
        get() = prefs.getString(ONE_TO_HUNDRED_SCORE, "0")
        set(vale) = prefs.edit().putString(ONE_TO_HUNDRED_SCORE, vale).apply()

    var oneToHundredTime: Long
        get() = prefs.getLong(ONE_TO_HUNDRED_TIME, 0)
        set(vale) = prefs.edit().putLong(ONE_TO_HUNDRED_TIME, vale).apply()

    fun getCommomStringData(key: String): String {
        return prefs.getString(key, "")
    }

    fun saveCommonStringData(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getCommomLongData(key: String): Long {
        return prefs.getLong(key, 0)
    }

    fun saveCommonLongData(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }
    fun getCommomIntData(key: String): Int {
        return prefs.getInt(key, 0)
    }

    fun saveCommonIntData(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
}