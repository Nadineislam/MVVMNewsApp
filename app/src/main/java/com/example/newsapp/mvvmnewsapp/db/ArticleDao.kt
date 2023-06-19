package com.example.newsapp.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.mvvmnewsapp.models.Article

@Dao
interface ArticleDao {
    //this dao access our local database but the newsApi access our api and how we can make api requests
    //on conflict because if we insert an article that we have the same in database, it will be replaced with new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //long is the id that was inserted
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles ")
    //livedata doesn't work with suspend fun
    //livedata is class that enables us (fragments) to subscribe the changes of that livedata and whenever data changes then the
    //livedata will notify the fragments about these changes so we can update our recycler view in our case
    //here if the article changes then the livedata will notify
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}