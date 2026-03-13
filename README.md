# 🕯️ 临终清单 - 别等死了才后悔

> 收集真实的人在弥留之际最后悔没做的事，做成匿名数据库。用户看一眼别人的遗憾清单，然后被问：你现在可以做哪条？生成你的"别等死了才后悔"待办列表。

## 📱 功能特性

- **🏠 首页** - 每日一条遗憾 + 快速探索入口
- **📜 遗憾广场** - 浏览所有遗憾，按分类筛选，支持「我也是」共鸣
- **✏️ 发布遗憾** - 完全匿名提交
- **✅ 待办清单** - 「别等死了才后悔」行动清单，打卡记录已挽救的遗憾
- **📊 数据洞察** - Top共鸣排行 + 分类分布统计

## 🛠️ 技术栈

- **Kotlin** + **Jetpack Compose** (Material3)
- **MVVM** 架构
- **Room** 本地数据库
- **Compose Navigation** 导航
- **Coroutines + Flow** 异步

## 🚀 构建运行

用 Android Studio 打开项目，Sync Gradle 后直接运行即可。

## 📁 项目结构

```
app/src/main/java/com/lastregrets/
├── data/
│   ├── model/        # Regret, TodoItem, RegretCategory
│   ├── local/        # Room DB, DAO, SeedData
│   └── repository/   # RegretRepository, TodoRepository
├── ui/
│   ├── theme/        # 暗色主题 (Color, Theme, Type)
│   ├── screens/      # 5个页面
│   ├── viewmodel/    # 4个ViewModel
│   ├── navigation/   # Bottom Nav + NavHost
│   └── components/   # 公共工具
├── MainActivity.kt
└── LastRegretsApp.kt
```

## 📄 License

MIT
