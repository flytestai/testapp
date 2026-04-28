package com.testziliao.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import com.testziliao.app.ui.search.SearchViewModel

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    viewModel: SearchViewModel = viewModel(
        factory = AppViewModelFactories.search(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("搜索") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = uiState.keyword,
                    onValueChange = viewModel::onKeywordChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("搜索文章、题库、题�?) },
                    singleLine = true,
                    trailingIcon = {
                        TextButton(onClick = viewModel::submitKeyword) {
                            Text("搜索")
                        }
                    }
                )
            }

            if (uiState.keyword.isBlank()) {
                item {
                    AppSectionCard(
                        title = "最近搜�?,
                        body = if (uiState.recentSearches.isEmpty()) {
                            "还没有搜索记录�?
                        } else {
                            uiState.recentSearches.joinToString(" / ") { it.keyword }
                        }
                    )
                }
                items(uiState.recentSearches, key = { it.keyword }) { record ->
                    AppSectionCard(
                        title = record.keyword,
                        body = "点击再次搜索",
                        modifier = Modifier.clickable {
                            viewModel.useKeyword(record.keyword)
                            viewModel.submitKeyword()
                        }
                    )
                }
            } else {
                item {
                    Text(
                        text = "文章结果",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (uiState.articleResults.isEmpty()) {
                    item {
                        AppSectionCard("文章结果", "没有匹配到文章，试试分类词或技术关键词�?)
                    }
                } else {
                    items(uiState.articleResults, key = { it.id }) { article ->
                        ArticleCard(
                            article = article,
                            onClick = { onArticleClick(article.id) }
                        )
                    }
                }

                item {
                    Text(
                        text = "题库结果",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (uiState.questionSetResults.isEmpty()) {
                    item {
                        AppSectionCard("题库结果", "没有匹配到题库专题�?)
                    }
                } else {
                    items(uiState.questionSetResults, key = { it.id }) { questionSet ->
                        QuestionSetCard(
                            questionSet = questionSet,
                            onClick = { onQuestionSetClick(questionSet.id) }
                        )
                    }
                }

                item {
                    Text(
                        text = "题目结果",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (uiState.questionItemResults.isEmpty()) {
                    item {
                        AppSectionCard("题目结果", "没有匹配到具体题目，试试更短一点的关键词�?)
                    }
                } else {
                    items(uiState.questionItemResults, key = { it.id }) { question ->
                        QuestionItemCard(item = question)
                    }
                }
            }
        }
    }
}
