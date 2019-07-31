package com.example.game.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 文章的一行,作为数据库的一个表
 * 每行包括:id(行号) 内容 长度
 */
@Entity(tableName = "article_line",primaryKeys = ["id","book_name"])
data class ArticleLine(
        var id: Int,
        var content: String?,
        var length: Int?,
        @ColumnInfo(name = "book_name") var bookName:String
)