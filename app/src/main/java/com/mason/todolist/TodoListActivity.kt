package com.mason.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mason.todolist.dto.CreateTodoListDto
import com.mason.todolist.dto.TodoListDto
import com.mason.todolist.factory.TodoListViewModelFactory
import com.mason.todolist.repo.TodoListRepository
import com.mason.todolist.retrofit.RetrofitInstance
import com.mason.todolist.service.TodoListService
import com.mason.todolist.token.TokenManager
import com.mason.todolist.ui.theme.TodoListTheme
import com.mason.todolist.viewModel.AuthUiState
import com.mason.todolist.viewModel.TodoListViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TodoListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 取得 Application Context
        val appContext = applicationContext
        val tokenManager = TokenManager(appContext) // 將 Application Context 傳入

        // 建立所有依賴物件
        val retrofit = RetrofitInstance.create(tokenManager)
        val todoListService = retrofit.create(TodoListService::class.java)
        val todoListRepository = TodoListRepository(todoListService)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoListScreen(
                        modifier = Modifier.padding(innerPadding),
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

@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier,
    todoListViewModel: TodoListViewModel? = null
) {

    val uiState = todoListViewModel?.uiState?.collectAsState()?.value ?: AuthUiState.Idle
    val todoList = todoListViewModel?.todoList?.collectAsState()?.value ?: emptyList()


    var addTodoContent by remember { mutableStateOf("") }
    // Fetch the list of to-do items when the Composable is first launched.
    LaunchedEffect(Unit) {
        todoListViewModel?.getList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "待辦事項清單",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = addTodoContent,
                onValueChange = { addTodoContent = it },
                label = { Text("新增待辦事項") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (
                        addTodoContent.isNotBlank()
                    ) {
                        val createTodoListDto = CreateTodoListDto(addTodoContent, false)
                        todoListViewModel?.addItem(createTodoListDto)
                        addTodoContent = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)

            ) {
                Text(text = "新增")
            }


        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(todoList) { todoItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todoItem.task,
                        modifier = Modifier.weight(1f),
                        // 根據 todoItem.completed 的狀態來設定 TextDecoration
                        textDecoration = if (todoItem.completed) TextDecoration.LineThrough else null,
                        style = MaterialTheme.typography.bodyLarge // 你可以保持原有的 style
                    )
                    Button(
                        onClick = {
                            val updatedTodoItem = todoItem.copy(completed = !todoItem.completed)
                            todoListViewModel?.updateItem(
                                todoItem.id,
                                updatedTodoItem
                            ) // 修正：只需傳入更新後的物件
                        },
                        // 使用預設顏色，通常是主題色 Primary
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (todoItem.completed) {
                            Text(text = "取消完成")
                        } else {
                            Text(text = "完成")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            todoListViewModel?.deleteItem(todoItem.id)
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent, // 背景透明
                            contentColor = MaterialTheme.colorScheme.error, // 文字和邊框使用錯誤色
                        ),
                    ) {
                        Text(text = "刪除")
                    }
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    TodoListTheme {
        TodoListScreen()
    }
}