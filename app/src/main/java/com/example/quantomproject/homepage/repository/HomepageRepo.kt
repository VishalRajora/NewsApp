package com.example.quantomproject.homepage.repository

import android.util.Log
import com.example.quantomproject.homepage.data.News
import com.example.quantomproject.network.RetrofitApiInterface
import com.example.quantomproject.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class HomepageRepo @Inject constructor(
    private val apiInterface: RetrofitApiInterface
) {

    private val _getNews = MutableStateFlow<Resource<News>>(Resource.Init())
    val getNews = _getNews.asStateFlow()

    suspend fun getNews(country: String, apiKey: String) {
        try {
            _getNews.emit(Resource.Loading())
            val result = apiInterface.getNews(country, apiKey)
            if (result.isSuccessful) {
                Log.i("Repo", "getNews: ${result.body()}")
                _getNews.emit(Resource.Success(result.body()))
            } else {
                Log.i("Repo", "getNewsElse:failed ")
                _getNews.emit(Resource.Error("Failed"))
            }
        } catch (e: Exception) {
            Log.i("Repo", "getNews:Error ${e.localizedMessage} ")
            _getNews.emit(Resource.Error("No Internet"))
        }
    }
}