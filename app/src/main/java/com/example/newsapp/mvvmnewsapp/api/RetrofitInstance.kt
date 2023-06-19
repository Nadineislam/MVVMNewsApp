package com.example.newsapp.mvvmnewsapp.api

import com.example.newsapp.mvvmnewsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//we create retrofit singleton class to make request from everywhere in our code
class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            //lazy means that we only initialize this here
            //we will attach this logging to our retrofit object to be able to see what requests are made and what the response are
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                //factory is used to determine how the response should actually be interpreted and converted to kotlin object
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()

        }
        public val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}