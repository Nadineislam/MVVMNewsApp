package com.example.newsapp.mvvmnewsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.mvvmnewsapp.data.api.NewsApi
import com.example.newsapp.mvvmnewsapp.data.db.ArticleDao
import com.example.newsapp.mvvmnewsapp.data.db.ArticleDatabase
import com.example.newsapp.mvvmnewsapp.data.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context):ArticleDatabase =
        Room.databaseBuilder(context,ArticleDatabase::class.java,"postDatabase")
            .build()

    @Provides
    fun providesPostDao(articleDatabase: ArticleDatabase):ArticleDao =
        articleDatabase.getArticleDao()
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)


}