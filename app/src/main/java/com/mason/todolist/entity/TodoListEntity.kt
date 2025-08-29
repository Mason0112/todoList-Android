package com.mason.todolist.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list")
class TodoListEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val task: String,
    val completed: Boolean
)