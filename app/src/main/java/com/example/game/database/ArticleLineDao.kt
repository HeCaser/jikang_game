package com.example.game.database

import androidx.room.*

@Dao
interface ArticleLineDao {
    @Query("SELECT * FROM article_line ")
    fun getAll(): List<ArticleLine>

    @Query("SELECT * FROM article_line WHERE id>=:start AND book_name=:bookName LIMIT :num")
    fun getFromIndex(start: Int, num: Int, bookName: String): List<ArticleLine>

    @Query("SELECT * FROM article_line WHERE book_name=:name ORDER BY id DESC LIMIT 1")
    fun getLastLineFromBook(name: String): List<ArticleLine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg line: ArticleLine)

    @Delete
    fun delete(line: ArticleLine)


}