package com.lastregrets.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户待办 - "别等死了才后悔"清单项
 */
@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val regretId: Long,                     // 关联的遗憾ID（本地数据）
    val regretContent: String,              // 遗憾内容快照
    val regretFirestoreId: String? = null,  // 关联的 Firestore ID（云端数据）
    val actionPlan: String = "",            // 用户的行动计划
    val isCompleted: Boolean = false,       // 是否已完成
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null           // 完成时间
)
