package com.mason.todolist.token

import android.content.Context
import android.content.SharedPreferences

// 將 Context 參數放入建構子，並使用 Application Context
class TokenManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    private companion object {
        const val TOKEN_KEY = "jwt_token"
    }

    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun deleteToken() {
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .apply()
    }
}


