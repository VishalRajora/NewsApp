package com.example.quantomproject.utils

import com.example.quantomproject.network.RetrofitApiInterface
import com.example.quantomproject.utils.Constant.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder().serializeNulls().create()
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(
                Constant.CONNECTION_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                Constant.READ_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )
            .writeTimeout(Constant.WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(Interceptor { chain ->
                val authToken = "" //PrefHelper.getStringVal("AuthToken", "")
                val userId = ""//PrefHelper.getStringVal("UserId", "")
                val originalReq = chain.request()
                val newRequest = originalReq.newBuilder().header("UserId", userId)
                    .header("AuthToken", authToken).build()
                chain.proceed(newRequest)
            }).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun forApi(retrofit: Retrofit): RetrofitApiInterface =
        retrofit.create(RetrofitApiInterface::class.java)
}