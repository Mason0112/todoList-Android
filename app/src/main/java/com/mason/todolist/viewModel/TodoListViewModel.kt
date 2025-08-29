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

    val todoList = todoListRepository.todoList

    init {
        // We start a coroutine to fetch the list from the network
        // and put it into the Room database.
        // The UI, in the meantime, might show old data from Room,
        // which is a good user experience.
        getList()
    }

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