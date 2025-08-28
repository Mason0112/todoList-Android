package com.mason.todolist.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mason.todolist.repo.TodoListRepository
import com.mason.todolist.viewModel.TodoListViewModel

class TodoListViewModelFactory(private  val todoListRepository: TodoListRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 檢查請求的 ViewModel 類別是否為 AuthViewModel
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            // 如果是，就創建並返回一個新的 AuthViewModel 實例，並傳入所需的依賴
            @Suppress("UNCHECKED_CAST")
            return TodoListViewModel(todoListRepository) as T
        }
        // 否則，拋出異常
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}