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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.mason.todolist.dto.UserRegAndLoginDto
import com.mason.todolist.factory.AuthViewModelFactory
import com.mason.todolist.factory.TodoListViewModelFactory
import com.mason.todolist.repo.AuthRepository
import com.mason.todolist.repo.TodoListRepository
import com.mason.todolist.retrofit.RetrofitInstance
import com.mason.todolist.service.AuthApiService
import com.mason.todolist.service.TodoListService
import com.mason.todolist.token.TokenManager
import com.mason.todolist.ui.theme.TodoListTheme
import com.mason.todolist.viewModel.AuthUiState
import com.mason.todolist.viewModel.AuthViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 取得 Application Context
        val appContext = applicationContext
        val tokenManager = TokenManager(appContext)

        // 建立 Room 資料庫實例
        val db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "todo-database"
        ).build()

        // 建立所有依賴物件
        val retrofit = RetrofitInstance.create(tokenManager)
        val authApiService = retrofit.create(AuthApiService::class.java)
        val authRepository = AuthRepository(authApiService, tokenManager)
        val todoListService = retrofit.create(TodoListService::class.java)
        val todoListRepository = TodoListRepository(todoListService,db.todoListDao())

        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                val navController = rememberNavController()

                // Corrected: NavHost is the single root Composable inside setContent
                NavHost(navController = navController, startDestination = "login_screen") {
                    composable("login_screen") {
                        // Place Scaffold inside the composable for each screen
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            LoginScreen(
                                modifier = Modifier.padding(innerPadding),
                                authViewModel = viewModel(factory = AuthViewModelFactory(authRepository)),
                                onNavigateToTodoList = {
                                    navController.navigate("todolist_screen") {
                                        popUpTo("login_screen") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                    composable("todolist_screen") {
                        // You can also add a Scaffold here if needed for this screen
                        TodoListScreen(
                            todoListViewModel = viewModel(
                                factory = TodoListViewModelFactory(
                                    todoListRepository
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                authViewModel: AuthViewModel = viewModel(),
                onNavigateToTodoList: () -> Unit) {
    val uiState by authViewModel.uiState.collectAsState()
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
            text = "登入帳號",
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
                Text("登入中...")
            }
            is AuthUiState.Success -> {
                Text("登入成功！")
                LaunchedEffect(Unit) {
                    onNavigateToTodoList()
                }
            }
            is AuthUiState.Error -> {
                val errorMessage = (uiState as AuthUiState.Error).message
                Text("登入失敗: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
            AuthUiState.Idle -> {
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    val loginRequest = UserRegAndLoginDto(username, password)
                    authViewModel.login(loginRequest)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "登入")
        }
    }

}
