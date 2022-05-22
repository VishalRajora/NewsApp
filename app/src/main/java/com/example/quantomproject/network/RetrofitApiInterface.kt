package com.example.quantomproject.network

import com.example.quantomproject.homepage.data.News
import com.example.quantomproject.utils.Constant.GET_HEADLINES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiInterface {

    @GET(GET_HEADLINES)
    suspend fun getNews(
        @Query("country") country: String,
        @Query("apiKey") key: String
    ): Response<News>

}