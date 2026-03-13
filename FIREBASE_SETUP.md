# 🔥 Firebase 配置指南

## 为什么需要 Firebase？

「临终清单」需要一个云端数据库来让所有用户**共享遗憾数据**：
- 用户 A 发布的遗憾，用户 B 在广场能看到
- 所有用户的"共鸣"计数是全局共享的
- **不需要自己搭建服务器**，Firebase 提供免费的 Serverless 后端

## 架构说明

```
用户手机 App
    ├── 本地 Room 数据库（离线缓存 + 待办清单）
    └── Firebase Firestore（云端共享数据）
         ├── 读取：实时获取所有用户的遗憾
         ├── 写入：双写（Firestore + Room）
         └── 失败回退：Firestore 不可用时自动使用本地数据
```

**无需自建服务器！** Firebase Firestore 就是你的后端。

## 配置步骤

### 1. 创建 Firebase 项目

1. 访问 [Firebase Console](https://console.firebase.google.com/)
2. 点击「添加项目」，输入项目名称（如 `before-i-die`）
3. 可以关闭 Google Analytics（不需要）
4. 点击「创建项目」

### 2. 注册 Android 应用

1. 在 Firebase 项目概览页面，点击 Android 图标
2. 填写包名：`com.lastregrets`
3. （可选）填写应用昵称
4. 点击「注册应用」

### 3. 下载配置文件

1. 下载 `google-services.json`
2. 将文件放到 `app/` 目录下：
   ```
   before-i-die/
   ├── app/
   │   ├── google-services.json  ← 放这里！
   │   ├── build.gradle.kts
   │   └── src/
   └── build.gradle.kts
   ```

### 4. 设置 Firestore 数据库

1. 在 Firebase Console 左侧菜单，点击「Firestore Database」
2. 点击「创建数据库」
3. 选择区域（建议选 `asia-east1` 或 `asia-southeast1`）
4. 选择「以测试模式启动」（开发阶段）

### 5. 配置安全规则（正式上线时）

测试模式的规则 30 天后会过期。上线前请设置以下规则：

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /regrets/{regretId} {
      // 所有人可以读
      allow read: if true;
      // 所有人可以创建新遗憾
      allow create: if true;
      // 只允许更新 resonateCount 字段（防止篡改）
      allow update: if request.resource.data.diff(resource.data).affectedKeys()
                      .hasOnly(['resonateCount']);
    }
  }
}
```

### 6. 构建运行

```bash
./gradlew assembleDebug
```

## 免费额度

Firebase Spark（免费）计划包含：
- **Firestore**：1 GiB 存储 + 每天 50,000 次读取 + 20,000 次写入
- 对于这个小项目来说**完全够用**

## 数据结构

Firestore 中 `regrets` 集合的文档结构：

```json
{
  "content": "没有在毕业时和喜欢的人告白",
  "category": "LOVE",
  "source": "anonymous",
  "resonateCount": 42,
  "isUserSubmitted": true,
  "createdAt": "2024-01-01T00:00:00Z"
}
```

## 常见问题

**Q: 不配置 Firebase 能运行吗？**
A: 可以！App 会自动回退到本地 Room 数据库，只是数据不会在用户之间共享。

**Q: 需要自己搭建服务器吗？**
A: 不需要！Firebase Firestore 是 Google 提供的 Serverless 数据库，直接从 App 连接。

**Q: 数据安全吗？**
A: 所有遗憾都是匿名提交的，不关联任何用户身份信息。配合 Firestore 安全规则可以防止恶意操作。
