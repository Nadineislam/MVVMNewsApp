package com.example.newsapp.mvvmnewsapp.repository

import com.example.newsapp.mvvmnewsapp.api.RetrofitInstance
import com.example.newsapp.mvvmnewsapp.db.ArticleDatabase
import com.example.newsapp.mvvmnewsapp.models.Article

//the purpose of repository is to get data from database and our remote data source (from retrofit or api)
//we will have in this repo fun that directly queries our api for the news
class NewsRepository(private val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(search: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(search, pageNumber)

    suspend fun upsert(article: Article) =
        db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = db.getArticleDao().delete(article)

}