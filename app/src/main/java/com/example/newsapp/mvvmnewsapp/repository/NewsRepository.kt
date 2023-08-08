package com.example.newsapp.mvvmnewsapp.repository

import com.example.newsapp.mvvmnewsapp.api.NewsApi
import com.example.newsapp.mvvmnewsapp.db.ArticleDao
import com.example.newsapp.mvvmnewsapp.models.Article
import javax.inject.Inject

//the purpose of repository is to get data from database and our remote data source (from retrofit or api)
//we will have in this repo fun that directly queries our api for the news
class NewsRepository @Inject constructor(private val articleDao: ArticleDao,private val newsAPI: NewsApi) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsAPI.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(search: String, pageNumber: Int) =
        newsAPI.searchForNews(search, pageNumber)

    suspend fun upsert(article: Article) =
        articleDao.upsert(article)

    fun getSavedNews() = articleDao.getAllArticles()

    suspend fun delete(article: Article) = articleDao.delete(article)

}