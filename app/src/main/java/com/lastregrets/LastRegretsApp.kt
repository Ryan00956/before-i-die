package com.lastregrets

import android.app.Application
import android.util.Log
import com.lastregrets.data.local.AppDatabase
import com.lastregrets.data.local.SeedData
import com.lastregrets.data.remote.FirestoreDataSource
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LastRegretsApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getDatabase(this) }
    val firestoreDataSource by lazy { FirestoreDataSource() }
    val regretRepository by lazy { RegretRepository(database.regretDao(), firestoreDataSource) }
    val todoRepository by lazy { TodoRepository(database.todoDao()) }

    override fun onCreate() {
        super.onCreate()

        // 首次启动时，将种子数据同步到 Firestore
        applicationScope.launch {
            try {
                val seedRegrets = SeedData.getAllSeedRegrets()
                regretRepository.syncSeedDataToFirestore(seedRegrets)
            } catch (e: Exception) {
                Log.e("LastRegretsApp", "种子数据同步失败（可能未配置 Firebase）", e)
            }
        }
    }
}
