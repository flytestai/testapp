package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.ArticleCard
import com.testziliao.app.ui.components.QuestionSetCard
import com.testziliao.app.ui.favorites.FavoritesViewModel

@Composable
fun FavoritesScreen(
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel(
        factory = AppViewModelFactories.favorites(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "本地收藏",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            AppSectionCard(
                title = "收藏文章",
                body = if (uiState.favoriteArticles.isEmpty()) {
                    "还没有本地收藏文章�?
                } else {
                    "�?${uiState.favoriteArticles.size} 篇文章�?
                }
            )
        }
        if (uiState.favoriteArticles.isEmpty()) {
            item {
                AppSectionCard(
                    title = "文章列表",
                    body = "还没有收藏文章，看到合适内容时点右上角爱心就会出现在这里�?
                )
            }
        } else {
            items(uiState.favoriteArticles, key = { it.id }) { article ->
                ArticleCard(
                    article = article,
                    onClick = { onArticleClick(article.id) }
                )
            }
        }
        item {
            AppSectionCard(
                title = "收藏题库",
                body = if (uiState.favoriteQuestionSets.isEmpty()) {
                    "还没有本地收藏题库�?
                } else {
                    "�?${uiState.favoriteQuestionSets.size} 个题库专题�?
                }
            )
        }
        if (uiState.favoriteQuestionSets.isEmpty()) {
            item {
                AppSectionCard(
                    title = "题库列表",
                    body = "还没有收藏题库专题，收藏后会在这里显示真实标题�?
                )
            }
        } else {
            items(uiState.favoriteQuestionSets, key = { it.id }) { questionSet ->
                QuestionSetCard(
                    questionSet = questionSet,
                    onClick = { onQuestionSetClick(questionSet.id) }
                )
            }
        }
    }
}
