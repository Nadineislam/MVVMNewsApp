package com.example.newsapp.mvvmnewsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.mvvmnewsapp.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles ")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}