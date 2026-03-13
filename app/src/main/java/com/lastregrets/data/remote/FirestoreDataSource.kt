package com.lastregrets.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

/**
 * Firestore 远程数据源
 * 负责与 Firebase Firestore 交互，实现遗憾数据的云端存储和实时同步
 */
class FirestoreDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    companion object {
        const val COLLECTION_REGRETS = "regrets"
        private const val TIMEOUT_MS = 10_000L // 10秒超时
        private const val TAG = "FirestoreDataSource"
    }

    /**
     * 获取所有遗憾（实时监听）
     */
    fun getAllRegrets(): Flow<List<Regret>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_REGRETS)
            .orderBy("resonateCount", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // 出错时发送空列表，不关闭 flow（允许重试）
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val regrets = snapshot?.documents?.mapNotNull { doc ->
                    doc.toRegret()
                } ?: emptyList()
                trySend(regrets)
            }
        awaitClose { listener.remove() }
    }

    /**
     * 按分类获取遗憾（实时监听）
     */
    fun getRegretsByCategory(category: RegretCategory): Flow<List<Regret>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_REGRETS)
            .whereEqualTo("category", category.name)
            .orderBy("resonateCount", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val regrets = snapshot?.documents?.mapNotNull { doc ->
                    doc.toRegret()
                } ?: emptyList()
                trySend(regrets)
            }
        awaitClose { listener.remove() }
    }

    /**
     * 获取热门遗憾（实时监听）
     */
    fun getTopRegrets(limit: Int = 10): Flow<List<Regret>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_REGRETS)
            .orderBy("resonateCount", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val regrets = snapshot?.documents?.mapNotNull { doc ->
                    doc.toRegret()
                } ?: emptyList()
                trySend(regrets)
            }
        awaitClose { listener.remove() }
    }

    /**
     * 获取分类统计（实时监听）
     */
    fun getCategoryStats(): Flow<List<CategoryStatRemote>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_REGRETS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val stats = snapshot?.documents
                    ?.mapNotNull { it.getString("category") }
                    ?.groupBy { it }
                    ?.map { (category, items) -> CategoryStatRemote(category, items.size) }
                    ?.sortedByDescending { it.count }
                    ?: emptyList()
                trySend(stats)
            }
        awaitClose { listener.remove() }
    }

    /**
     * 获取总数（实时监听）
     */
    fun getTotalCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection(COLLECTION_REGRETS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(0)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0)
            }
        awaitClose { listener.remove() }
    }

    /**
     * 获取随机遗憾
     */
    suspend fun getRandomRegret(): Regret? {
        return try {
            withTimeout(TIMEOUT_MS) {
                val snapshot = firestore.collection(COLLECTION_REGRETS)
                    .get()
                    .await()
                if (snapshot.isEmpty) return@withTimeout null
                val randomDoc = snapshot.documents.random()
                randomDoc.toRegret()
            }
        } catch (e: Exception) {
            Log.w(TAG, "获取随机遗憾超时或失败", e)
            null
        }
    }

    /**
     * 提交新遗憾到 Firestore
     */
    suspend fun submitRegret(content: String, category: RegretCategory): String? {
        return try {
            withTimeout(TIMEOUT_MS) {
                val data = hashMapOf(
                    "content" to content,
                    "category" to category.name,
                    "source" to "anonymous",
                    "resonateCount" to 0,
                    "createdAt" to System.currentTimeMillis(),
                    "isSeedData" to false,
                    "isUserSubmitted" to true
                )
                val docRef = firestore.collection(COLLECTION_REGRETS)
                    .add(data)
                    .await()
                docRef.id
            }
        } catch (e: Exception) {
            Log.w(TAG, "提交遗憾超时或失败", e)
            null
        }
    }

    /**
     * 共鸣（原子递增）
     * 使用 Firestore 的 FieldValue.increment 保证并发安全
     */
    suspend fun resonate(firestoreId: String): Boolean {
        return try {
            withTimeout(TIMEOUT_MS) {
                firestore.collection(COLLECTION_REGRETS)
                    .document(firestoreId)
                    .update("resonateCount", com.google.firebase.firestore.FieldValue.increment(1))
                    .await()
                true
            }
        } catch (e: Exception) {
            Log.w(TAG, "共鸣操作超时或失败", e)
            false
        }
    }

    /**
     * 批量上传种子数据（仅首次使用）
     */
    suspend fun uploadSeedData(regrets: List<Regret>): Boolean {
        return try {
            withTimeout(30_000L) { // 批量上传给更长的超时时间
                val batch = firestore.batch()
                regrets.forEach { regret ->
                    val docRef = firestore.collection(COLLECTION_REGRETS).document()
                    val data = hashMapOf(
                        "content" to regret.content,
                        "category" to regret.category,
                        "source" to regret.source,
                        "resonateCount" to regret.resonateCount,
                        "createdAt" to regret.createdAt,
                        "isSeedData" to true,
                        "isUserSubmitted" to false
                    )
                    batch.set(docRef, data)
                }
                batch.commit().await()
                true
            }
        } catch (e: Exception) {
            Log.w(TAG, "上传种子数据超时或失败", e)
            false
        }
    }

    /**
     * 检查 Firestore 中是否已有数据
     */
    suspend fun hasData(): Boolean {
        return try {
            withTimeout(TIMEOUT_MS) {
                val snapshot = firestore.collection(COLLECTION_REGRETS)
                    .limit(1)
                    .get()
                    .await()
                !snapshot.isEmpty
            }
        } catch (e: Exception) {
            Log.w(TAG, "检查数据超时或失败", e)
            false
        }
    }
}

/**
 * Firestore 文档转换为 Regret 模型
 * 使用 Firestore document ID 作为标识符（存到 firestoreId 字段）
 */
private fun com.google.firebase.firestore.DocumentSnapshot.toRegret(): Regret? {
    return try {
        Regret(
            id = 0, // Room 的 ID 在本地自动生成
            content = getString("content") ?: return null,
            category = getString("category") ?: RegretCategory.OTHER.name,
            source = getString("source") ?: "anonymous",
            resonateCount = getLong("resonateCount")?.toInt() ?: 0,
            createdAt = getLong("createdAt") ?: System.currentTimeMillis(),
            isSeedData = getBoolean("isSeedData") ?: false,
            isUserSubmitted = getBoolean("isUserSubmitted") ?: false,
            firestoreId = id // Firestore 文档 ID
        )
    } catch (e: Exception) {
        null
    }
}

/**
 * 远程分类统计数据
 */
data class CategoryStatRemote(
    val category: String,
    val count: Int
)
