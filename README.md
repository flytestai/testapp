# 测试资料库

一个面向软件测试学习与面试准备的零后端内容型 Android App。

当前工程已经包含：

- Kotlin + Jetpack Compose 应用骨架
- Room 本地缓存与本地收藏/历史/搜索记录
- 文章与题库两条内容路径
- Markdown 正文渲染
- 静态内容源示例 `content/`
- 可配置的应用名、包名、内容源地址

## 项目配置

核心配置在 [gradle.properties](/D:/360MoveData/Users/66674/Desktop/AI/testapp/gradle.properties)：

- `APP_NAMESPACE`
- `APP_APPLICATION_ID`
- `APP_DISPLAY_NAME`
- `APP_VERSION_CODE`
- `APP_VERSION_NAME`
- `CONTENT_BASE_URL`

默认值：

```properties
APP_NAMESPACE=com.testziliao.app
APP_APPLICATION_ID=com.testziliao.app
APP_DISPLAY_NAME=测试资料库
APP_VERSION_CODE=1
APP_VERSION_NAME=1.0
CONTENT_BASE_URL=https://example.com/
```

可选的 release 签名参数：

```properties
SIGNING_STORE_FILE=C:\\path\\to\\release.keystore
SIGNING_STORE_PASSWORD=your-store-password
SIGNING_KEY_ALIAS=your-key-alias
SIGNING_KEY_PASSWORD=your-key-password
```

如果不提供这些参数，`assembleRelease` 会继续输出 `app-release-unsigned.apk`。

## 本地运行

1. 用 Android Studio 打开工程根目录。
2. 等待 Gradle Sync 完成。
3. 把 `CONTENT_BASE_URL` 改成你自己的静态托管地址。
4. 启动模拟器或连接 Android 真机。
5. 运行 `app` 模块。

## 内容目录

示例静态内容在 [content](/D:/360MoveData/Users/66674/Desktop/AI/testapp/content)：

- 分类：[content/categories/index.json](/D:/360MoveData/Users/66674/Desktop/AI/testapp/content/categories/index.json)
- 文章列表：[content/articles/index.json](/D:/360MoveData/Users/66674/Desktop/AI/testapp/content/articles/index.json)
- 文章正文 Markdown：[content/articles/posts](/D:/360MoveData/Users/66674/Desktop/AI/testapp/content/articles/posts)
- 题库列表：[content/questions/index.json](/D:/360MoveData/Users/66674/Desktop/AI/testapp/content/questions/index.json)
- 题库详情：[content/questions/items](/D:/360MoveData/Users/66674/Desktop/AI/testapp/content/questions/items)

JSON 里现在使用的是相对路径，所以通常只需要改 `CONTENT_BASE_URL`。

## 发布静态内容

### 方案 A：GitHub Pages

1. 新建一个 GitHub 仓库。
2. 把当前项目里的 `content/` 目录内容推上去。
3. 仓库 Settings -> Pages -> 选择分支发布。
4. 得到地址，例如：

```text
https://yourname.github.io/test-content/
```

5. 把 `CONTENT_BASE_URL` 改成：

```properties
CONTENT_BASE_URL=https://yourname.github.io/test-content/
```

### 方案 B：Cloudflare Pages

1. 新建一个 Git 仓库并提交 `content/`。
2. 在 Cloudflare Pages 里导入这个仓库。
3. Build command 留空或填最简单的静态发布配置。
4. Output directory 设为仓库根目录或 `content` 所在发布目录。
5. 发布后拿到域名。
6. 把 `CONTENT_BASE_URL` 改成你的 Pages 域名。

如果你单独托管 `content/`，确保最终线上地址满足这些请求：

```text
{CONTENT_BASE_URL}/content/categories/index.json
{CONTENT_BASE_URL}/content/articles/index.json
{CONTENT_BASE_URL}/content/questions/index.json
```

## 上线前检查

1. 确认 `CONTENT_BASE_URL` 指向真实可访问地址。
2. 真机测试首页、分类、搜索、收藏、浏览记录。
3. 检查文章 Markdown 与题库详情是否能正常打开。
4. 确认 `APP_APPLICATION_ID` 不再变动。
5. 检查图标、应用名、启动页是否符合预期。

## 现在还没做的事

这套工程目前偏 MVP，下面这些你后面可以继续补：

- 下拉刷新
- 文章/题库封面图
- 深色模式细化
- 浏览记录时间分组
- 题库按难度筛选
- 发布签名与商店上架素材

## 建议的下一步

最值得先做的是：

1. 把 `content/` 真正发布到 GitHub Pages 或 Cloudflare Pages
2. 改掉 `CONTENT_BASE_URL`
3. 在 Android Studio 里真机跑通一遍
4. 再根据体验修第一轮 UI
