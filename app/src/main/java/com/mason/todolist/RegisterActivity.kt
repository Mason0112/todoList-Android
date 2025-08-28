package com.mason.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mason.todolist.ui.theme.TodoListTheme
import com.mason.todolist.viewModel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mason.todolist.dto.UserRegAndLoginDto
import com.mason.todolist.service.AuthApiService
import com.mason.todolist.repo.AuthRepository
import com.mason.todolist.retrofit.RetrofitInstance
import com.mason.todolist.token.TokenManager
import com.mason.todolist.viewModel.AuthUiState
import com.mason.todolist.factory.AuthViewModelFactory

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 取得 Application Context
        val appContext = applicationContext
        val tokenManager = TokenManager(appContext) // 將 Application Context 傳入

        // 建立所有依賴物件
        val retrofit = RetrofitInstance.create(tokenManager)
        val authApiService = retrofit.create(AuthApiService::class.java)
        val authRepository = AuthRepository(authApiService, tokenManager)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 在這裡調用我們新建立的註冊畫面
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = viewModel(factory = AuthViewModelFactory(authRepository))
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier,
                   authViewModel: AuthViewModel? = null ) {

    val uiState = authViewModel?.uiState?.collectAsState()?.value ?: AuthUiState.Idle
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "註冊帳號",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("使用者名稱") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密碼") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        when (uiState) {
            is AuthUiState.Loading -> {
                Text("註冊中...")
                // You could also show a CircularProgressIndicator here
            }
            is AuthUiState.Success -> {
                // Navigate to the next screen or show a success message
                Text("註冊成功！歡迎加入！")
                // You might want to navigate to the main screen here.
                // For example: navController.navigate("main_screen")
            }
            is AuthUiState.Error -> {
                // Show an error message
                val errorMessage = uiState.message
                Text("註冊失敗: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
            AuthUiState.Idle -> {
                // The UI is in its normal idle state
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    val registerRequest = UserRegAndLoginDto(username, password)
                    authViewModel?.register(registerRequest)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "註冊")
        }
    }
}

// Preview 現在不會傳入 authViewModel
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    TodoListTheme {
        RegisterScreen()  // 不傳入 authViewModel
    }
}