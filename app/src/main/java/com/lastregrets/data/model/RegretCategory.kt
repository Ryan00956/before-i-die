package com.lastregrets.data.model

/**
 * 遗憾分类
 */
enum class RegretCategory(val displayName: String, val emoji: String) {
    FAMILY("亲情", "👨‍👩‍👧‍👦"),
    LOVE("爱情", "❤️"),
    CAREER("事业", "💼"),
    HEALTH("健康", "🏃"),
    DREAM("梦想", "🌟"),
    FRIENDSHIP("友情", "🤝"),
    SELF("自我成长", "🌱"),
    OTHER("其他", "💭");

    companion object {
        fun fromName(name: String): RegretCategory {
            return entries.find { it.name == name } ?: OTHER
        }
    }
}
