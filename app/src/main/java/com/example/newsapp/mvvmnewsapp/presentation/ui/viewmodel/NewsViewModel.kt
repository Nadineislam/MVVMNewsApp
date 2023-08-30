package com.example.newsapp.mvvmnewsapp.presentation.ui.viewmodel


import android.app.Application
import android.app.Service
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.mvvmnewsapp.core.NewsApplication
import com.example.newsapp.mvvmnewsapp.models.Article
import com.example.newsapp.mvvmnewsapp.models.NewsResponse
import com.example.newsapp.mvvmnewsapp.data.repository.NewsRepository
import com.example.newsapp.mvvmnewsapp.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(app: Application, private val repository: NewsRepository) :
    AndroidViewModel(app) {
    private val _breakingNews: MutableStateFlow<Resource<NewsResponse>> = MutableStateFlow(Resource.Loading())
    val breakingNews: StateFlow<Resource<NewsResponse>> = _breakingNews
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null

    private val _searchNews: MutableStateFlow<Resource<NewsResponse>> = MutableStateFlow(Resource.Loading())
    val searchNews:StateFlow<Resource<NewsResponse>> = _searchNews
    private var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }

    fun getSearchNews(search: String) = viewModelScope.launch {
        safeSearchNewsCall(search)

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedNews()
    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        _breakingNews.value=Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNews.value=handleBreakingNewsResponse(response)

            } else {
                _breakingNews.value=Resource.Error("No Internet Connection")
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _breakingNews.value=Resource.Error("Network Failure")
                else -> _breakingNews.value=Resource.Error("Conversion Error")
            }

        }
    }

    private suspend fun safeSearchNewsCall(search: String) {
        _searchNews.value=Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = repository.getSearchNews(search, searchNewsPage)
                _searchNews.value=handleSearchNewsResponse(response)

            } else {
                _searchNews.value=Resource.Error("No Internet Connection")
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchNews.value=Resource.Error("Network Failure")
                else -> _searchNews.value=Resource.Error("Conversion Error")
            }

        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Service.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }

        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_ETHERNET -> true
                    TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
        return false
    }
}