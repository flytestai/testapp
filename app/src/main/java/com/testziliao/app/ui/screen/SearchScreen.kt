package com.testziliao.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.testziliao.app.ui.components.QuestionItemCard
import com.testziliao.app.ui.components.QuestionSetCard
import com.testziliao.app.ui.components.SectionHeader
import com.testziliao.app.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelFactories.search(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("搜索") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    OutlinedTextField(
                        value = uiState.keyword,
                        onValueChange = viewModel::onKeywordChange,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        label = { Text("搜索文章、题库、题目") },
                        singleLine = true,
                        trailingIcon = { TextButton(onClick = viewModel::submitKeyword) { Text("搜索") } }
                    )
                }
            }
            if (uiState.keyword.isBlank()) {
                item {
                    SectionHeader(
                        title = "最近搜索",
                        subtitle = if (uiState.recentSearches.isEmpty()) "还没有搜索记录。" else "点一下即可再次搜索",
                        trailing = {
                            if (uiState.recentSearches.isNotEmpty()) {
                                TextButton(onClick = viewModel::clearRecentSearches) {
                                    Text("清空")
                                }
                            }
                        }
                    )
                }
                items(uiState.recentSearches, key = { it.keyword }) { record ->
                    AppSectionCard(title = record.keyword, body = "点击再次搜索", modifier = Modifier.clickable {
                        viewModel.useKeyword(record.keyword)
                        viewModel.submitKeyword()
                    })
                }
            } else {
                item { SectionHeader(title = "文章结果", subtitle = "按标题与摘要匹配") }
                if (uiState.articleResults.isEmpty()) item { AppSectionCard("文章结果", "没有匹配到文章，试试更宽泛的关键词。") }
                else items(uiState.articleResults, key = { it.id }) { article -> ArticleCard(article = article, onClick = { onArticleClick(article.id) }) }

                item { SectionHeader(title = "题库结果", subtitle = "按专题标题匹配") }
                if (uiState.questionSetResults.isEmpty()) item { AppSectionCard("题库结果", "没有匹配到题库专题。") }
                else items(uiState.questionSetResults, key = { it.id }) { questionSet -> QuestionSetCard(questionSet = questionSet, onClick = { onQuestionSetClick(questionSet.id) }) }

                item { SectionHeader(title = "题目结果", subtitle = "适合直接查找知识点") }
                if (uiState.questionItemResults.isEmpty()) item { AppSectionCard("题目结果", "没有匹配到具体题目，试试更短一点的关键词。") }
                else items(uiState.questionItemResults, key = { it.id }) { question -> QuestionItemCard(item = question) }
            }
        }
    }
}
