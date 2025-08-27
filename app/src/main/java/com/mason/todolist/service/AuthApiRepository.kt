package com.mason.todolist.service

import android.content.Context
import android.util.Log
import com.mason.todolist.dto.LoginResponseDto
import com.mason.todolist.dto.UserRegAndLoginDto
import com.mason.todolist.token.TokenManager

class AuthRepository(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager

) {
    val TAG = AuthRepository::class.java.simpleName
    suspend fun login(request: UserRegAndLoginDto): LoginResponseDto {
        val response = authApiService.login(request)
        tokenManager.saveToken(response.token)
        Log.d(TAG, "login: $response")
        return response
    }

    suspend fun register(request: UserRegAndLoginDto): LoginResponseDto {
        val response = authApiService.register(request)
        tokenManager.saveToken(response.token)
        return response
    }
}