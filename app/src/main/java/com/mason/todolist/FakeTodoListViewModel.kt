package com.mason.todolist

import androidx.lifecycle.ViewModel
import com.mason.todolist.dto.CreateTodoListDto
import com.mason.todolist.dto.TodoListDto
import com.mason.todolist.viewModel.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTodoListViewModel(
    private val fakeTodoList: List<TodoListDto> = emptyList()
) : ViewModel() {
    val uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val todoList = MutableStateFlow<List<TodoListDto>>(fakeTodoList)
    fun getList() {} // 不需要實作
    fun addItem(dto: CreateTodoListDto) {} // 不需要實作
    fun deleteItem(id: Int) {} // 不需要實作
}