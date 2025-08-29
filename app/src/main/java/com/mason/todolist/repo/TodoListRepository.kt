package com.mason.todolist.repo

import com.mason.todolist.dao.TodoListDao
import com.mason.todolist.dto.CreateTodoListDto
import com.mason.todolist.dto.TodoListDto
import com.mason.todolist.entity.TodoListEntity
import com.mason.todolist.service.TodoListService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoListRepository(
    private val todoListService: TodoListService,
    private val todoListDao: TodoListDao
) {

    val TAG = TodoListRepository::class.java.simpleName

    // 從資料庫讀取資料，並映射為 DTO
    val todoList: Flow<List<TodoListDto>> = todoListDao.getAllTodoItems().map { entities ->
        entities.map { entity ->
            TodoListDto(id = entity.id, task = entity.task, completed = entity.completed)
        }
    }

    suspend fun getList(): List<TodoListDto> {
        try {
            val response = todoListService.getList()
            val entities = response.map { dto ->
                TodoListEntity(id = dto.id, task = dto.task, completed = dto.completed)

            }
            todoListDao.insertAll(entities)
            return response
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun create(createTodoListDto: CreateTodoListDto): TodoListDto {
        val create = todoListService.create(createTodoListDto)
        todoListDao.insert(
            TodoListEntity(
                id = create.id,
                task = create.task,
                completed = create.completed
            )
        )
        return create

    }

    suspend fun update(id: Long, updateTodoListDto: TodoListDto): TodoListDto {
        val update = todoListService.update(id, updateTodoListDto)
        todoListDao.update(TodoListEntity(
            id = update.id,
            task = update.task,
            completed = update.completed
        ))
        return update
    }

    suspend fun delete(id: Long) {
        todoListService.delete(id)
        todoListDao.delete(TodoListEntity(id = id, task = "", completed = false))

    }


}