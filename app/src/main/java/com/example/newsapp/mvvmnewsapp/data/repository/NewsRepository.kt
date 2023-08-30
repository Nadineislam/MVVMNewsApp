package com.example.newsapp.mvvmnewsapp.data.repository

import com.example.newsapp.mvvmnewsapp.data.api.NewsApi
import com.example.newsapp.mvvmnewsapp.data.db.ArticleDao
import com.example.newsapp.mvvmnewsapp.models.Article
import javax.inject.Inject

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