package com.mason.todolist.dto

enum class Role {
    ADMIN,
    USER
}

data class UserRegAndLoginDto(
    val userName: String,
    val password: String
)

data class LoginResponseDto(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val userName: String,
    val role: Role
)