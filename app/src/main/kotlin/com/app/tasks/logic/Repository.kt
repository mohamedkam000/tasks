package com.app.tasks.logic

import com.app.tasks.*
import com.app.tasks.logic.database.TasksEntity
import kotlinx.coroutines.flow.Flow

object Repository {
    private val db get() = Tasks.db
    private val toDoDao = db.toDoDao()

    suspend fun insertTodo(toDo: TasksEntity) {
        toDoDao.insert(toDo)
    }

    fun getAllTodos(): Flow<List<TasksEntity>> = toDoDao.getAll()

    suspend fun updateTodo(toDo: TasksEntity) {
        toDoDao.update(toDo)
    }

    suspend fun deleteTodo(toDo: TasksEntity) {
        toDoDao.delete(toDo)
    }

    suspend fun deleteTodoFromIds(toDoItems: List<Int>) {
        toDoDao.deleteFromIds(toDoItems.toSet())
    }
}