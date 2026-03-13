package com.lastregrets.data.repository

import com.lastregrets.data.local.TodoDao
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {

    fun getActiveTodos(): Flow<List<TodoItem>> = todoDao.getActiveTodos()

    fun getCompletedTodos(): Flow<List<TodoItem>> = todoDao.getCompletedTodos()

    fun getAllTodos(): Flow<List<TodoItem>> = todoDao.getAllTodos()

    fun getCompletedCount(): Flow<Int> = todoDao.getCompletedCount()

    fun getTotalTodoCount(): Flow<Int> = todoDao.getTotalTodoCount()

    suspend fun addRegretToTodo(regret: Regret, actionPlan: String = ""): Long {
        // 检查是否已存在
        if (todoDao.isTodoExists(regret.id)) return -1

        val todoItem = TodoItem(
            regretId = regret.id,
            regretContent = regret.content,
            actionPlan = actionPlan
        )
        return todoDao.insertTodo(todoItem)
    }

    suspend fun completeTodo(todoId: Long) {
        todoDao.completeTodo(todoId)
    }

    suspend fun uncompleteTodo(todoId: Long) {
        todoDao.uncompleteTodo(todoId)
    }

    suspend fun updateActionPlan(todoItem: TodoItem, newPlan: String) {
        todoDao.updateTodo(todoItem.copy(actionPlan = newPlan))
    }

    suspend fun deleteTodo(todoId: Long) {
        todoDao.deleteTodoById(todoId)
    }
}
