package com.mason.todolist.retrofit

import com.mason.todolist.inrerceptor.AuthInterceptor
import com.mason.todolist.token.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//用來創建Service使用  與後端伺服器產生連結
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun create(tokenManager: TokenManager): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}