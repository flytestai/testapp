package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testziliao.app.ui.AppViewModelFactories
import com.testziliao.app.ui.article.ArticleListViewModel
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.ArticleCard
import com.testziliao.app.ui.components.SectionHeader

@Composable
fun ArticleListScreen(
    categoryId: String,
    categoryName: String,
    onArticleClick: (String) -> Unit,
    viewModel: ArticleListViewModel = viewModel(
        factory = AppViewModelFactories.articleList(LocalContext.current, categoryId, categoryName)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = uiState.categoryName, subtitle = "按更新时间浏览这个分类下的内容")
        if (uiState.articles.isEmpty()) {
            AppSectionCard(title = "暂无文章", body = uiState.message ?: "这个分类下还没有文章内容。")
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.articles, key = { it.id }) { article -> ArticleCard(article = article, onClick = { onArticleClick(article.id) }) }
            }
        }
    }
}
