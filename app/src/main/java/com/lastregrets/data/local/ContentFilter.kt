package com.lastregrets.data.local

/**
 * 基础内容过滤器
 * 检测用户提交内容中的广告、不当信息
 */
object ContentFilter {

    private val spamKeywords = listOf(
        "微信", "qq", "加我", "联系方式", "私聊",
        "加群", "扫码", "二维码", "http://", "https://",
        "www.", ".com", ".cn", "关注公众号", "粉丝",
        "下载", "推荐码", "邀请码", "优惠券", "免费领"
    )

    private val sensitiveKeywords = listOf(
        "自杀", "自残", "跳楼", "割腕", "轻生"
    )

    /**
     * 检查内容是否合规
     * @return 错误提示文案，null 表示通过
     */
    fun check(content: String): String? {
        val lower = content.lowercase()

        for (word in spamKeywords) {
            if (lower.contains(word.lowercase())) {
                return "内容中包含疑似广告信息，请修改后重新提交"
            }
        }

        for (word in sensitiveKeywords) {
            if (lower.contains(word.lowercase())) {
                return "检测到敏感内容。如果你正在经历困难，请拨打心理援助热线：400-161-9995"
            }
        }

        return null
    }
}
