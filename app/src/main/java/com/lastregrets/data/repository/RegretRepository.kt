package com.lastregrets.data.repository

import android.util.Log
import com.lastregrets.data.local.CategoryStat
import com.lastregrets.data.local.RegretDao
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.data.remote.FirestoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * 遗憾数据仓库
 *
 * 策略：
 * - 读取：优先从 Firestore 实时获取（所有用户共享数据），失败时 fallback 到本地 Room
 * - 写入：双写，先写 Firestore（确保其他用户可见），再写本地 Room（离线缓存）
 * - 共鸣：写 Firestore（全局计数），本地不再单独维护
 */
class RegretRepository(
    private val regretDao: RegretDao,
    private val firestoreDataSource: FirestoreDataSource
) {
    companion object {
        private const val TAG = "RegretRepository"
    }

    /**
     * 获取所有遗憾 - 从 Firestore 实时获取
     * 所有用户发布的遗憾大家都能看到
     */
    fun getAllRegrets(): Flow<List<Regret>> =
        firestoreDataSource.getAllRegrets()
            .onEach { remoteRegrets ->
                // 同步到本地缓存（可选）
                if (remoteRegrets.isNotEmpty()) {
                    Log.d(TAG, "从云端获取到 ${remoteRegrets.size} 条遗憾")
                }
            }
            .catch { e ->
                Log.w(TAG, "Firestore 获取失败，回退到本地数据", e)
                // Fallback 到本地数据
                regretDao.getAllRegrets().collect { emit(it) }
            }

    /**
     * 按分类获取遗憾 - 从 Firestore 获取
     */
    fun getRegretsByCategory(category: RegretCategory): Flow<List<Regret>> =
        firestoreDataSource.getRegretsByCategory(category)
            .catch { e ->
                Log.w(TAG, "Firestore 分类查询失败，回退到本地", e)
                regretDao.getRegretsByCategory(category.name).collect { emit(it) }
            }

    /**
     * 获取随机遗憾 - 先试 Firestore，失败用本地
     */
    suspend fun getRandomRegret(): Regret? {
        return try {
            firestoreDataSource.getRandomRegret() ?: regretDao.getRandomRegret()
        } catch (e: Exception) {
            Log.w(TAG, "Firestore 随机获取失败，使用本地数据", e)
            regretDao.getRandomRegret()
        }
    }

    /**
     * 获取指定遗憾（仅本地，用于待办关联）
     */
    suspend fun getRegretById(id: Long): Regret? = regretDao.getRegretById(id)

    /**
     * 获取热门遗憾 - 从 Firestore 获取
     */
    fun getTopRegrets(limit: Int = 10): Flow<List<Regret>> =
        firestoreDataSource.getTopRegrets(limit)
            .catch { e ->
                Log.w(TAG, "Firestore 热门查询失败，回退到本地", e)
                regretDao.getTopRegrets(limit).collect { emit(it) }
            }

    /**
     * 获取分类统计 - 从 Firestore 获取
     */
    fun getCategoryStats(): Flow<List<CategoryStat>> =
        firestoreDataSource.getCategoryStats()
            .map { remoteStats ->
                remoteStats.map { CategoryStat(it.category, it.count) }
            }
            .catch { e ->
                Log.w(TAG, "Firestore 统计查询失败，回退到本地", e)
                regretDao.getCategoryStats().collect { emit(it) }
            }

    /**
     * 获取总数 - 从 Firestore 获取
     */
    fun getTotalCount(): Flow<Int> =
        firestoreDataSource.getTotalCount()
            .catch { e ->
                Log.w(TAG, "Firestore 总数查询失败，回退到本地", e)
                regretDao.getTotalCount().collect { emit(it) }
            }

    /**
     * 提交新遗憾 - 双写：Firestore + 本地 Room
     * 发布到 Firestore 后，所有用户都能在广场看到
     */
    suspend fun submitRegret(content: String, category: RegretCategory): Long {
        // 1. 先写入 Firestore（让全球用户可见）
        val firestoreId = firestoreDataSource.submitRegret(content, category)

        // 2. 写入本地 Room（作为缓存 + 离线记录）
        val regret = Regret(
            content = content,
            category = category.name,
            source = "anonymous",
            resonateCount = 0,
            isUserSubmitted = true,
            firestoreId = firestoreId
        )
        val localId = regretDao.insertRegret(regret)

        if (firestoreId == null) {
            Log.w(TAG, "Firestore 写入失败，仅保存到本地")
        } else {
            Log.d(TAG, "遗憾已发布到云端: $firestoreId")
        }

        return localId
    }

    /**
     * 共鸣 - 写入 Firestore（全局共享计数）
     * 这样所有用户看到的共鸣数是一致的
     */
    suspend fun resonate(regret: Regret) {
        val firestoreId = regret.firestoreId

        if (firestoreId != null) {
            // 有 Firestore ID，更新云端（全局生效，实时同步给所有用户）
            val success = firestoreDataSource.resonate(firestoreId)
            if (!success) {
                Log.w(TAG, "Firestore 共鸣失败，回退到本地")
                if (regret.id > 0) {
                    regretDao.incrementResonateCount(regret.id)
                }
            }
        } else if (regret.id > 0) {
            // 没有 Firestore ID（纯本地数据），只更新本地
            regretDao.incrementResonateCount(regret.id)
        }
    }

    /**
     * 上传种子数据到 Firestore（首次初始化时调用）
     */
    suspend fun syncSeedDataToFirestore(seedRegrets: List<Regret>): Boolean {
        return try {
            if (!firestoreDataSource.hasData()) {
                Log.d(TAG, "Firestore 为空，开始上传种子数据...")
                firestoreDataSource.uploadSeedData(seedRegrets)
                Log.d(TAG, "种子数据上传完成，共 ${seedRegrets.size} 条")
                true
            } else {
                Log.d(TAG, "Firestore 已有数据，跳过种子数据上传")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "种子数据上传失败", e)
            false
        }
    }
}
