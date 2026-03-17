package com.lastregrets.data.local

import androidx.room.*
import com.lastregrets.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTodos(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedTodos(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTodos(): Flow<List<TodoItem>>

    @Query("SELECT COUNT(*) FROM todo_items WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM todo_items")
    fun getTotalTodoCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM todo_items WHERE regretId = :regretId)")
    suspend fun isTodoExists(regretId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM todo_items WHERE regretFirestoreId = :firestoreId)")
    suspend fun isTodoExistsByFirestoreId(firestoreId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoItem: TodoItem): Long

    @Update
    suspend fun updateTodo(todoItem: TodoItem)

    @Query("UPDATE todo_items SET isCompleted = 1, completedAt = :completedAt WHERE id = :todoId")
    suspend fun completeTodo(todoId: Long, completedAt: Long = System.currentTimeMillis())

    @Query("UPDATE todo_items SET isCompleted = 0, completedAt = null WHERE id = :todoId")
    suspend fun uncompleteTodo(todoId: Long)

    @Delete
    suspend fun deleteTodo(todoItem: TodoItem)

    @Query("DELETE FROM todo_items WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: Long)
}
