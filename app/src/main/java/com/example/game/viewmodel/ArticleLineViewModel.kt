/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.game.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.R
import com.example.game.constant.BOOK_ZHONGQIUJIE
import com.example.game.database.ArticleLine
import com.example.game.database.ArticleLineDao
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 */
class ArticleLineViewModel(private val dao: ArticleLineDao) : ViewModel() {

    val lines: MutableLiveData<List<ArticleLine>> = MutableLiveData()
    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    /**
     * 保存书籍,
     * 在协程中运行,隶属于调用者的线程但不会阻塞此线程
     */
    fun saveBook(ctx: Context, name: String) {
        viewModelScope.launch {
            try {
                var mline = 0
                val inputReader = when (name) {
                    BOOK_ZHONGQIUJIE -> InputStreamReader(ctx.resources.openRawResource(R.raw.zhongqiujie))
                    else -> InputStreamReader(ctx.resources.openRawResource(R.raw.zhongqiujie))
                }

                val bufReader = BufferedReader(inputReader)
                var line = bufReader.readLine()
                while (!TextUtils.isEmpty(line)) {
                    dao.insertAll(ArticleLine(mline++, line, line.length, name))
                    line = bufReader.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取某本书的内容,start~start+num 范围
     */
    fun getLineFromIndex(start: Int, num: Int, bookName: String) {
        viewModelScope.launch {
            val mList = dao.getFromIndex(start, num, bookName)
            lines.postValue(mList)
        }
    }

}
