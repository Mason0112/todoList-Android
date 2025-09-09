package com.mason.todolist.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mason.todolist.dto.CreateTodoListDto
import com.mason.todolist.dto.TodoListDto
import com.mason.todolist.repo.TodoListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class TodoListUiState {
    object Idle : TodoListUiState()
    object Loading : TodoListUiState()
    object Success : TodoListUiState()
    data class Error(val message: String) : TodoListUiState()
}


class TodoListViewModel(private val todoListRepository: TodoListRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Idle)
    val uiState: StateFlow<TodoListUiState> = _uiState

    //準備Flow List給Activity使用
    val todoList = todoListRepository.todoList

    init {
        getList()
    }

    //先把資料放進去Room
    fun getList() {
        viewModelScope.launch {
            _uiState.value = TodoListUiState.Loading
            try {
                todoListRepository.getList()
                _uiState.value = TodoListUiState.Success
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Error(e.message ?: "Failed to fetch todo list")
            }
        }
    }

    fun addItem(item: CreateTodoListDto) {
        viewModelScope.launch {
            _uiState.value = TodoListUiState.Loading
            try {
                todoListRepository.create(item)
                _uiState.value = TodoListUiState.Success
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Error(e.message ?: "Failed to add item")
            }
        }
    }

    fun updateItem(id: Long, item: TodoListDto) {
        viewModelScope.launch {
            _uiState.value = TodoListUiState.Loading
            try {
                todoListRepository.update(id,item)
                _uiState.value = TodoListUiState.Success
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Error(e.message ?: "Failed to update item")
            }
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            _uiState.value = TodoListUiState.Loading
            try {
                todoListRepository.delete(itemId)
                _uiState.value = TodoListUiState.Success
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Error(e.message ?: "Failed to delete item")
            }
        }
    }
}