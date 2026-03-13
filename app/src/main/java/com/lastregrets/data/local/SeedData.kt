package com.lastregrets.data.local

import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory

/**
 * 种子数据 - 来自真实临终访谈、书籍和研究的遗憾清单
 * 参考：《临终前最后悔的五件事》(Bronnie Ware) 等
 */
object SeedData {

    fun getAllSeedRegrets(): List<Regret> = familyRegrets + loveRegrets + careerRegrets +
            healthRegrets + dreamRegrets + friendshipRegrets + selfRegrets + otherRegrets

    private val familyRegrets = listOf(
        Regret(
            content = "后悔没有多陪陪父母，总觉得来日方长，却不知道时间从不等人",
            category = RegretCategory.FAMILY.name,
            source = "interview",
            resonateCount = 892,
            isSeedData = true
        ),
        Regret(
            content = "后悔没对爸妈说过'我爱你'，中国人的含蓄让我错过了太多表达",
            category = RegretCategory.FAMILY.name,
            source = "interview",
            resonateCount = 756,
            isSeedData = true
        ),
        Regret(
            content = "后悔小时候嫌妈妈唠叨，现在她不在了，好想再听她多说几句",
            category = RegretCategory.FAMILY.name,
            source = "anonymous",
            resonateCount = 634,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有给父母拍更多照片和视频，记忆会模糊，但影像不会",
            category = RegretCategory.FAMILY.name,
            source = "anonymous",
            resonateCount = 521,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有问爷爷奶奶他们年轻时的故事，那些历史随他们一起消失了",
            category = RegretCategory.FAMILY.name,
            source = "interview",
            resonateCount = 445,
            isSeedData = true
        ),
        Regret(
            content = "后悔为了工作错过了孩子的成长，第一次走路、第一次叫爸爸，都是别人告诉我的",
            category = RegretCategory.FAMILY.name,
            source = "interview",
            resonateCount = 678,
            isSeedData = true
        ),
        Regret(
            content = "后悔跟兄弟姐妹因为钱闹翻了，到最后才明白，血浓于水不是说说而已",
            category = RegretCategory.FAMILY.name,
            source = "anonymous",
            resonateCount = 389,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有带父母出去旅行，他们一辈子都没离开过那个小县城",
            category = RegretCategory.FAMILY.name,
            source = "anonymous",
            resonateCount = 567,
            isSeedData = true
        ),
    )

    private val loveRegrets = listOf(
        Regret(
            content = "后悔没有对那个人说出口，怕被拒绝的恐惧远不如一辈子的'如果当初'痛苦",
            category = RegretCategory.LOVE.name,
            source = "interview",
            resonateCount = 934,
            isSeedData = true
        ),
        Regret(
            content = "后悔把爱人当作理所当然，等到失去才知道，那些平淡的日常就是最好的时光",
            category = RegretCategory.LOVE.name,
            source = "interview",
            resonateCount = 823,
            isSeedData = true
        ),
        Regret(
            content = "后悔因为面子不肯先道歉，一段十年的感情就这样断了",
            category = RegretCategory.LOVE.name,
            source = "anonymous",
            resonateCount = 567,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有给她一个拥抱，那天她在哭，而我只会说'别哭了'",
            category = RegretCategory.LOVE.name,
            source = "anonymous",
            resonateCount = 445,
            isSeedData = true
        ),
        Regret(
            content = "后悔选了'条件合适'的人而不是真正心动的人，将就了一辈子",
            category = RegretCategory.LOVE.name,
            source = "interview",
            resonateCount = 712,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有认真经营婚姻，总以为感情不需要维护，结果它就像花一样枯萎了",
            category = RegretCategory.LOVE.name,
            source = "interview",
            resonateCount = 534,
            isSeedData = true
        ),
    )

    private val careerRegrets = listOf(
        Regret(
            content = "后悔一辈子都在做自己不喜欢的工作，为了稳定放弃了热爱",
            category = RegretCategory.CAREER.name,
            source = "book",
            resonateCount = 876,
            isSeedData = true
        ),
        Regret(
            content = "后悔太在意别人的评价，选专业、选工作都是为了让别人觉得体面",
            category = RegretCategory.CAREER.name,
            source = "interview",
            resonateCount = 654,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有在年轻时多试错，等到中年被裁员才发现自己什么都不会",
            category = RegretCategory.CAREER.name,
            source = "anonymous",
            resonateCount = 543,
            isSeedData = true
        ),
        Regret(
            content = "后悔工作占据了我全部的人生，升职加薪的快乐远不如陪家人吃顿饭",
            category = RegretCategory.CAREER.name,
            source = "book",
            resonateCount = 789,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有勇气辞职创业，安全感是个牢笼，我在里面待了三十年",
            category = RegretCategory.CAREER.name,
            source = "interview",
            resonateCount = 456,
            isSeedData = true
        ),
    )

    private val healthRegrets = listOf(
        Regret(
            content = "后悔年轻时不注意身体，熬夜、喝酒、不运动，身体是一切的本钱",
            category = RegretCategory.HEALTH.name,
            source = "interview",
            resonateCount = 912,
            isSeedData = true
        ),
        Regret(
            content = "后悔忽视了身体的警告信号，总说'再忙完这阵就去检查'，结果一拖就是癌症晚期",
            category = RegretCategory.HEALTH.name,
            source = "interview",
            resonateCount = 834,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有好好吃饭，年轻时觉得外卖方便，老了才知道一日三餐是最大的养生",
            category = RegretCategory.HEALTH.name,
            source = "anonymous",
            resonateCount = 567,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有坚持运动，不是为了身材，而是为了老了还能自己走路",
            category = RegretCategory.HEALTH.name,
            source = "anonymous",
            resonateCount = 623,
            isSeedData = true
        ),
        Regret(
            content = "后悔抽了几十年的烟，戒了无数次都没戒掉，现在肺已经不行了",
            category = RegretCategory.HEALTH.name,
            source = "interview",
            resonateCount = 445,
            isSeedData = true
        ),
    )

    private val dreamRegrets = listOf(
        Regret(
            content = "后悔没有去看看这个世界，总说等退休了再去旅行，结果退休了走不动了",
            category = RegretCategory.DREAM.name,
            source = "book",
            resonateCount = 867,
            isSeedData = true
        ),
        Regret(
            content = "后悔放弃了画画的梦想，别人说画画没出路，我就信了",
            category = RegretCategory.DREAM.name,
            source = "interview",
            resonateCount = 534,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有写下自己的故事，每个人的人生都值得被记录",
            category = RegretCategory.DREAM.name,
            source = "anonymous",
            resonateCount = 456,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有学一门乐器，音乐能治愈人心，而我到死都不会弹一首曲子",
            category = RegretCategory.DREAM.name,
            source = "anonymous",
            resonateCount = 389,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有勇气站上舞台，哪怕只有一次，让别人听到我的声音",
            category = RegretCategory.DREAM.name,
            source = "interview",
            resonateCount = 345,
            isSeedData = true
        ),
        Regret(
            content = "后悔一直在准备却从未开始，完美主义是梦想最大的敌人",
            category = RegretCategory.DREAM.name,
            source = "anonymous",
            resonateCount = 678,
            isSeedData = true
        ),
    )

    private val friendshipRegrets = listOf(
        Regret(
            content = "后悔弄丢了那个最好的朋友，成年后再也交不到那样的知己了",
            category = RegretCategory.FRIENDSHIP.name,
            source = "anonymous",
            resonateCount = 723,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有在朋友需要帮助的时候伸出手，觉得多一事不如少一事",
            category = RegretCategory.FRIENDSHIP.name,
            source = "interview",
            resonateCount = 456,
            isSeedData = true
        ),
        Regret(
            content = "后悔把太多时间花在无效社交上，真正的朋友不需要那么多",
            category = RegretCategory.FRIENDSHIP.name,
            source = "anonymous",
            resonateCount = 534,
            isSeedData = true
        ),
        Regret(
            content = "后悔毕业后就散了，那些一起哭一起笑的人，现在连微信都不聊了",
            category = RegretCategory.FRIENDSHIP.name,
            source = "anonymous",
            resonateCount = 612,
            isSeedData = true
        ),
    )

    private val selfRegrets = listOf(
        Regret(
            content = "后悔活在别人的期待里，从来没问过自己真正想要什么",
            category = RegretCategory.SELF.name,
            source = "book",
            resonateCount = 945,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有允许自己快乐，总觉得快乐是有罪的，要吃苦才对得起人生",
            category = RegretCategory.SELF.name,
            source = "book",
            resonateCount = 734,
            isSeedData = true
        ),
        Regret(
            content = "后悔花太多时间焦虑那些从未发生的事，恐惧偷走了我大半辈子",
            category = RegretCategory.SELF.name,
            source = "interview",
            resonateCount = 678,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有学会说'不'，为了讨好所有人，最后把自己活丢了",
            category = RegretCategory.SELF.name,
            source = "interview",
            resonateCount = 589,
            isSeedData = true
        ),
        Regret(
            content = "后悔一直在和别人比较，别人的人生看起来再好，那也不是我的",
            category = RegretCategory.SELF.name,
            source = "anonymous",
            resonateCount = 623,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有好好读书，不是为了文凭，而是为了拥有更广阔的内心世界",
            category = RegretCategory.SELF.name,
            source = "anonymous",
            resonateCount = 512,
            isSeedData = true
        ),
    )

    private val otherRegrets = listOf(
        Regret(
            content = "后悔没有养过一只猫或一只狗，它们能教会你什么是无条件的爱",
            category = RegretCategory.OTHER.name,
            source = "anonymous",
            resonateCount = 456,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有认真看过一次日出，总觉得太阳每天都会升起，直到有一天我看不到了",
            category = RegretCategory.OTHER.name,
            source = "interview",
            resonateCount = 378,
            isSeedData = true
        ),
        Regret(
            content = "后悔攒了一辈子钱，却没有真正享受过生活，钱是花不完的，但命会用完",
            category = RegretCategory.OTHER.name,
            source = "interview",
            resonateCount = 567,
            isSeedData = true
        ),
        Regret(
            content = "后悔没有对伤害过的人说声对不起，那份愧疚跟了我一辈子",
            category = RegretCategory.OTHER.name,
            source = "anonymous",
            resonateCount = 489,
            isSeedData = true
        ),
    )
}
