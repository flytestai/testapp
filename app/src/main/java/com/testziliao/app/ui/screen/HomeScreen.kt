package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testziliao.app.ui.AppViewModelFactories
import com.testziliao.app.ui.components.ArticleCard
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.home.HomeViewModel

@Composable
fun HomeScreen(
    onCategoryClick: (String, String) -> Unit,
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = AppViewModelFactories.home(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "测试内容首页",
            style = MaterialTheme.typography.headlineSmall
        )
        Button(onClick = viewModel::refresh) {
            Text(if (uiState.isRefreshing) "刷新�?.." else "刷新内容")
        }
        if (uiState.lastRefreshAt != null || uiState.message != null) {
            AppSectionCard(
                title = "同步状�?,
                body = uiState.message ?: "最近已同步内容"
            )
        }
        AppSectionCard(
            title = "搜索",
            body = "按文章标题、题库标题和具体题目内容搜索�?,
            modifier = Modifier.clickable(onClick = onSearchClick)
        )
        AppSectionCard(
            title = "推荐文章",
            body = "优先展示你标记为 featured 的文章�?
        )
        uiState.featuredArticles.forEach { article ->
            ArticleCard(
                article = article,
                onClick = { onArticleClick(article.id) }
            )
        }
        AppSectionCard(
            title = "最新面试题",
            body = "这里直接跳题库详情�?
        )
        uiState.latestQuestionSets.forEach { questionSet ->
            com.testziliao.app.ui.components.QuestionSetCard(
                questionSet = questionSet,
                onClick = { onQuestionSetClick(questionSet.id) }
            )
        }
        if (uiState.latestQuestionSets.isEmpty()) {
            AppSectionCard(
                title = "题库状�?,
                body = "当前还没有题库专题数据�?
            )
        }
        AppSectionCard(
            title = "最近更�?,
            body = "下面这几篇可以直接进详情页�?
        )
        uiState.latestArticles.take(5).forEach { article ->
            ArticleCard(
                article = article,
                onClick = { onArticleClick(article.id) }
            )
        }
        if (uiState.latestArticles.isEmpty()) {
            AppSectionCard(
                title = "内容状�?,
                body = uiState.message ?: "等你把内容源地址换掉之后，这里就会展示文章列表�?
            )
        }
        AppSectionCard(
            title = "快捷入口",
            body = "接口测试、自动化测试这些分类可以直接进列表�?
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                "api" to "接口测试",
                "automation" to "自动化测�?,
                "performance" to "性能测试"
            ).forEach { (id, name) ->
                ArticleCard(
                    article = com.testziliao.app.data.local.entity.ArticleEntity(
                        id = "shortcut-$id",
                        title = name,
                        summary = "进入$name分类",
                        categoryId = id,
                        tags = "",
                        cover = null,
                        author = "",
                        publishedAt = "",
                        updatedAt = "",
                        markdownUrl = "",
                        featured = false,
                        cachedAt = 0L
                    ),
                    onClick = { onCategoryClick(id, name) }
                )
            }
        }
    }
}
