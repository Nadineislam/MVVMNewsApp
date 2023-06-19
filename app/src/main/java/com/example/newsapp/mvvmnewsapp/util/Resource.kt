package com.example.newsapp.mvvmnewsapp.util

//This is class that recommended by google to be used to wrap around our network responses and that will be a generic class
//it's very useful to differentiate between successful and error responses and also help us to handle the loading state
//so when we make a response that we can show on a progress bar while that response is processing and when we get the answer
//then we can use that resource class to tell us whether that answer was successful or an error and depending on that we can
//handle that error or show that successful response
//it's class like abstract but we decide which classes can inherit from it
sealed class Resource<T>(//data is the body for our response
    val data: T? = null,
//message for our response which can be an error for example
    val message: String? = null

) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}