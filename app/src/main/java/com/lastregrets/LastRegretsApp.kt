package com.lastregrets

import android.app.Application
import com.lastregrets.data.local.AppDatabase
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository

class LastRegretsApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val regretRepository by lazy { RegretRepository(database.regretDao()) }
    val todoRepository by lazy { TodoRepository(database.todoDao()) }
}
