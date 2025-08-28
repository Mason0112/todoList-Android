package com.mason.todolist.repo

import com.mason.todolist.dto.CreateTodoListDto
import com.mason.todolist.dto.TodoListDto
import com.mason.todolist.service.TodoListService

class TodoListRepository(
    private val todoListService: TodoListService
) {

    val TAG = TodoListRepository::class.java.simpleName

    suspend fun getList(): List<TodoListDto> {
        return todoListService.getList()
    }

    suspend fun create(createTodoListDto: CreateTodoListDto): TodoListDto {
        return todoListService.create(createTodoListDto)
    }

    suspend fun update(id: Long, updateTodoListDto: TodoListDto): TodoListDto {
        return todoListService.update(id, updateTodoListDto)
    }

    suspend fun delete(id: Long) {
        todoListService.delete(id)
    }



}