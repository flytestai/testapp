package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
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
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.ArticleCard
import com.testziliao.app.ui.components.QuestionSetCard
import com.testziliao.app.ui.components.SectionHeader
import com.testziliao.app.ui.favorites.FavoritesViewModel

@Composable
fun FavoritesScreen(
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel(factory = AppViewModelFactories.favorites(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionHeader(title = "本地收藏", subtitle = "离线保留你重点关注的内容") }
        item {
            AppSectionCard(title = "收藏文章", body = if (uiState.favoriteArticles.isEmpty()) "还没有收藏文章。" else "共 ${uiState.favoriteArticles.size} 篇文章。")
        }
        if (uiState.favoriteArticles.isEmpty()) {
            item { AppSectionCard(title = "文章列表", body = "看到合适内容后点右上角收藏，这里会自动更新。") }
        } else {
            items(uiState.favoriteArticles, key = { it.id }) { article -> ArticleCard(article = article, onClick = { onArticleClick(article.id) }) }
        }
        item {
            AppSectionCard(title = "收藏题库", body = if (uiState.favoriteQuestionSets.isEmpty()) "还没有收藏题库专题。" else "共 ${uiState.favoriteQuestionSets.size} 个专题。")
        }
        if (uiState.favoriteQuestionSets.isEmpty()) {
            item { AppSectionCard(title = "题库列表", body = "题库专题收藏后会显示在这里，适合复习时集中回看。") }
        } else {
            items(uiState.favoriteQuestionSets, key = { it.id }) { questionSet -> QuestionSetCard(questionSet = questionSet, onClick = { onQuestionSetClick(questionSet.id) }) }
        }
    }
}
