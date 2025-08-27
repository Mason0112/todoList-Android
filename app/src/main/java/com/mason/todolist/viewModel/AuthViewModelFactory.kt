package com.mason.todolist.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mason.todolist.service.AuthRepository

class AuthViewModelFactory(private val authApiRepository: AuthRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 檢查請求的 ViewModel 類別是否為 AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // 如果是，就創建並返回一個新的 AuthViewModel 實例，並傳入所需的依賴
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authApiRepository) as T
        }
        // 否則，拋出異常
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}