package com.example.game.constant

import android.os.Build
import android.os.Looper


const val HOUR_MINUTES = 60
const val DAY_MINUTES = 24 * HOUR_MINUTES
const val WEEK_MINUTES = DAY_MINUTES * 7
const val MONTH_MINUTES = DAY_MINUTES * 30
const val YEAR_MINUTES = DAY_MINUTES * 365

const val MINUTE_SECONDS = 60
const val HOUR_SECONDS = HOUR_MINUTES * 60
const val DAY_SECONDS = DAY_MINUTES * 60
const val WEEK_SECONDS = WEEK_MINUTES * 60
const val MONTH_SECONDS = MONTH_MINUTES * 60
const val YEAR_SECONDS = YEAR_MINUTES * 60
const val NETWORK_TIMEOUT = 30000L

// shared preferences,为了版本兼容性,不要修改
const val PREFS_KEY = "Prefs"
const val ONE_TO_HUNDRED_SCORE = "one_to_hundred_score"
const val ONE_TO_HUNDRED_TIME = "one_to_hundred_time"
const val SCHULTE_GRID_ACTIVITY = "schulte_grid_activity"
const val SCHULTE_GRID_ACTIVITY2 = "schulte_grid_activity2"
const val NUMBER_25 = "number_25"
const val IS_FIRST_LOGIN = "is_first_login"
const val IS_LOGIN = "is_login"
const val APPTOKEN = "apptoken"

// 字符常量
const val HENG_LINE = "───"
const val ENGLISH_COMMA = ","
const val CHINESE_COMMA = "，"
const val CHINESE_COLON = "："
const val CHINESE_MONEY = "¥"

const val TITLE_MAX = 10

//速度游戏类型
const val SEARCH_TYPE_WORD = 1 //搜索词
const val SEARCH_TYPE_JIOU = 2 //奇偶数

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()
fun isJellyBean1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
fun isAndroidFour() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH
fun isKitkatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
fun isLollipopPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
fun isMarshmallowPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
fun isNougatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

