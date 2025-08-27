package com.mason.todolist.service

import com.mason.todolist.dto.LoginResponseDto
import com.mason.todolist.dto.UserRegAndLoginDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: UserRegAndLoginDto): LoginResponseDto

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: UserRegAndLoginDto): LoginResponseDto
}