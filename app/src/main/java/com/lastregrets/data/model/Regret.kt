package com.lastregrets.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 遗憾条目 - 别人在弥留之际最后悔没做的事
 */
@Entity(tableName = "regrets")
data class Regret(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,                    // 遗憾内容
    val category: String,                   // 分类 (RegretCategory.name)
    val source: String = "anonymous",       // 来源：anonymous/book/interview
    val resonateCount: Int = 0,             // "我也是" 共鸣计数
    val createdAt: Long = System.currentTimeMillis(),
    val isSeedData: Boolean = false,        // 是否为种子数据
    val isUserSubmitted: Boolean = false,   // 是否为用户提交
    val firestoreId: String? = null         // Firestore 文档 ID（用于云端同步）
) {
    fun getCategoryEnum(): RegretCategory = RegretCategory.fromName(category)
}
