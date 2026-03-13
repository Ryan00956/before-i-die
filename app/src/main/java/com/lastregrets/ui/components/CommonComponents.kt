package com.lastregrets.ui.components

import androidx.compose.ui.graphics.Color
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.ui.theme.*

/**
 * 获取分类对应的颜色
 */
fun RegretCategory.getColor(): Color {
    return when (this) {
        RegretCategory.FAMILY -> FamilyColor
        RegretCategory.LOVE -> LoveColor
        RegretCategory.CAREER -> CareerColor
        RegretCategory.HEALTH -> HealthColor
        RegretCategory.DREAM -> DreamColor
        RegretCategory.FRIENDSHIP -> FriendshipColor
        RegretCategory.SELF -> SelfColor
        RegretCategory.OTHER -> OtherColor
    }
}

/**
 * 根据来源获取标签
 */
fun getSourceLabel(source: String): String {
    return when (source) {
        "interview" -> "临终访谈"
        "book" -> "书籍记录"
        "anonymous" -> "匿名投稿"
        else -> "匿名"
    }
}

/**
 * 格式化共鸣数
 */
fun formatResonateCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}.${(count % 10000) / 1000}万"
        count >= 1000 -> "${count / 1000}.${(count % 1000) / 100}k"
        else -> count.toString()
    }
}
