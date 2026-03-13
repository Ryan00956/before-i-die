package com.lastregrets.data.local

import androidx.room.*
import com.lastregrets.data.model.Regret
import kotlinx.coroutines.flow.Flow

@Dao
interface RegretDao {

    @Query("SELECT * FROM regrets ORDER BY resonateCount DESC, createdAt DESC")
    fun getAllRegrets(): Flow<List<Regret>>

    @Query("SELECT * FROM regrets WHERE category = :category ORDER BY resonateCount DESC")
    fun getRegretsByCategory(category: String): Flow<List<Regret>>

    @Query("SELECT * FROM regrets ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomRegret(): Regret?

    @Query("SELECT * FROM regrets WHERE id = :id")
    suspend fun getRegretById(id: Long): Regret?

    @Query("SELECT * FROM regrets ORDER BY resonateCount DESC LIMIT :limit")
    fun getTopRegrets(limit: Int = 10): Flow<List<Regret>>

    @Query("SELECT category, COUNT(*) as count FROM regrets GROUP BY category ORDER BY count DESC")
    fun getCategoryStats(): Flow<List<CategoryStat>>

    @Query("SELECT COUNT(*) FROM regrets")
    fun getTotalCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegret(regret: Regret): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(regrets: List<Regret>)

    @Update
    suspend fun updateRegret(regret: Regret)

    @Query("UPDATE regrets SET resonateCount = resonateCount + 1 WHERE id = :regretId")
    suspend fun incrementResonateCount(regretId: Long)

    @Delete
    suspend fun deleteRegret(regret: Regret)

    @Query("SELECT COUNT(*) FROM regrets")
    suspend fun getCount(): Int
}

data class CategoryStat(
    val category: String,
    val count: Int
)
