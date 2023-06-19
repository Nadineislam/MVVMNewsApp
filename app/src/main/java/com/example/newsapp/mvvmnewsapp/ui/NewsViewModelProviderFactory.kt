package com.example.newsapp.mvvmnewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.mvvmnewsapp.repository.NewsRepository

class NewsViewModelProviderFactory(
    private val app: Application,
    private val repository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, repository) as T
    }

}