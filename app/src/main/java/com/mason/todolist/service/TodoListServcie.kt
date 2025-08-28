package com.mason.todolist.service

import com.mason.todolist.dto.CreateTodoListDto
import com.mason.todolist.dto.TodoListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoListService {

    @GET("/api/todos")
    suspend fun getList(): List<TodoListDto>

    @POST("/api/todos")
    suspend fun create(@Body createTodoListDto: CreateTodoListDto): TodoListDto


    @PUT("/api/todos/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body updateTodoListDto: TodoListDto
    ): TodoListDto

    @DELETE("/api/todos/{id}")
    suspend fun delete(
        @Path("id") id: Long
    ): Response<Unit>


}