package com.mason.todolist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mason.todolist.entity.TodoListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_list")
    fun getAllTodoItems(): Flow<List<TodoListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todoItems: List<TodoListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoItem: TodoListEntity)

    @Update
    suspend fun update(todoItem: TodoListEntity)

    @Delete
    suspend fun delete(todoItem: TodoListEntity)
}