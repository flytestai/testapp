package com.testziliao.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testziliao.app.R
import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.ui.AppViewModelFactories
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.ArticleCard
import com.testziliao.app.ui.components.QuestionSetCard
import com.testziliao.app.ui.components.SectionHeader
import com.testziliao.app.ui.home.HomeViewModel

@Composable
fun HomeScreen(
    onCategoryClick: (String, String) -> Unit,
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelFactories.home(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_brand_mark),
                            contentDescription = "应用标志",
                            modifier = Modifier
                                .size(72.dp)
                                .padding(10.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "测试资料库",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "围绕测试理论、自动化、题库和面试准备整理的一站式学习工具。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text("文章 ${uiState.latestArticles.size}") })
                    AssistChip(onClick = {}, label = { Text("题库 ${uiState.latestQuestionSets.size}") })
                    if (uiState.featuredArticles.isNotEmpty()) {
                        AssistChip(onClick = {}, label = { Text("推荐 ${uiState.featuredArticles.size}") })
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onSearchClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                        Text("搜索内容", modifier = Modifier.padding(start = 6.dp))
                    }
                    Button(
                        onClick = viewModel::refresh,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(Icons.Outlined.Sync, contentDescription = null)
                        Text(
                            text = if (uiState.isRefreshing) "同步中" else "更新内容",
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppSectionCard(
                title = "文章",
                body = "已加载 ${uiState.latestArticles.size} 篇",
                modifier = Modifier.weight(1f)
            )
            AppSectionCard(
                title = "题库",
                body = "专题 ${uiState.latestQuestionSets.size} 个",
                modifier = Modifier.weight(1f)
            )
        }

        if (uiState.lastRefreshAt != null || uiState.message != null) {
            AppSectionCard(
                title = "同步状态",
                body = uiState.message ?: "内容索引已完成同步"
            )
        }

        SectionHeader(title = "推荐文章", subtitle = "优先阅读这些核心主题")
        uiState.featuredArticles.forEach { article ->
            ArticleCard(article = article, onClick = { onArticleClick(article.id) })
        }

        SectionHeader(title = "题库专题", subtitle = "按专题整理的面试题与知识点")
        uiState.latestQuestionSets.forEach { questionSet ->
            QuestionSetCard(
                questionSet = questionSet,
                onClick = { onQuestionSetClick(questionSet.id) }
            )
        }
        if (uiState.latestQuestionSets.isEmpty()) {
            AppSectionCard(
                title = "题库状态",
                body = "当前还没有题库数据，确认内容源同步后会在这里显示。"
            )
        }

        SectionHeader(title = "最近更新", subtitle = "适合从这里继续阅读")
        uiState.latestArticles.take(5).forEach { article ->
            ArticleCard(article = article, onClick = { onArticleClick(article.id) })
        }
        if (uiState.latestArticles.isEmpty()) {
            AppSectionCard(
                title = "内容状态",
                body = uiState.message ?: "内容源地址更新后，这里会展示最新文章。"
            )
        }

        SectionHeader(title = "快捷入口", subtitle = "面向高频浏览分类")
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(
                Triple("api", "接口测试", "适合快速复习接口框架、鉴权和断言思路。"),
                Triple("automation", "自动化测试", "集中看 UI、接口和 App 自动化专题。"),
                Triple("performance", "性能测试", "直接进入性能、压测和瓶颈分析相关内容。")
            ).forEach { (id, name, summary) ->
                ArticleCard(
                    article = ArticleEntity(
                        id = "shortcut-$id",
                        title = name,
                        summary = summary,
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

        Spacer(modifier = Modifier.height(2.dp))
        AppSectionCard(
            title = "搜索",
            body = "支持按文章标题、题库标题和具体题目内容快速检索。",
            modifier = Modifier.clickable(onClick = onSearchClick)
        )
    }
}
