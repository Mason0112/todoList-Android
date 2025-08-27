package com.mason.todolist.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 暴露你的 AuthApiService 實例
    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}