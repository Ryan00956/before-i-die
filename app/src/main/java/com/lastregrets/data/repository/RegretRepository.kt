package com.lastregrets.data.repository

import com.lastregrets.data.local.CategoryStat
import com.lastregrets.data.local.RegretDao
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory
import kotlinx.coroutines.flow.Flow

class RegretRepository(private val regretDao: RegretDao) {

    fun getAllRegrets(): Flow<List<Regret>> = regretDao.getAllRegrets()

    fun getRegretsByCategory(category: RegretCategory): Flow<List<Regret>> =
        regretDao.getRegretsByCategory(category.name)

    suspend fun getRandomRegret(): Regret? = regretDao.getRandomRegret()

    suspend fun getRegretById(id: Long): Regret? = regretDao.getRegretById(id)

    fun getTopRegrets(limit: Int = 10): Flow<List<Regret>> = regretDao.getTopRegrets(limit)

    fun getCategoryStats(): Flow<List<CategoryStat>> = regretDao.getCategoryStats()

    fun getTotalCount(): Flow<Int> = regretDao.getTotalCount()

    suspend fun submitRegret(content: String, category: RegretCategory): Long {
        val regret = Regret(
            content = content,
            category = category.name,
            source = "anonymous",
            resonateCount = 0,
            isUserSubmitted = true
        )
        return regretDao.insertRegret(regret)
    }

    suspend fun resonate(regretId: Long) {
        regretDao.incrementResonateCount(regretId)
    }
}
