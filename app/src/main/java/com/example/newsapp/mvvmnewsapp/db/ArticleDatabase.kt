package com.example.newsapp.mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.mvvmnewsapp.models.Article

@Database(entities = [Article::class], version = 2)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object {
        //volatile means that other thread can immediately see when a thread changes this instance
        @Volatile
        var instance: ArticleDatabase? = null

        //lock to synchronized that instance
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }

}