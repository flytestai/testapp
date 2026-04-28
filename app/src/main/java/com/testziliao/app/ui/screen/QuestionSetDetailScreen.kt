package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Heart
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.testziliao.app.ui.components.QuestionItemCard
import com.testziliao.app.ui.components.TagFilterRow
import com.testziliao.app.ui.question.QuestionSetDetailViewModel

@Composable
fun QuestionSetDetailScreen(
    questionSetId: String,
    onBackClick: () -> Unit,
    viewModel: QuestionSetDetailViewModel = viewModel(
        factory = AppViewModelFactories.questionSetDetail(
            context = LocalContext.current,
            questionSetId = questionSetId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(uiState.questionSet?.title ?: "题库详情")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(
                            imageVector = Icons.Outlined.Heart,
                            contentDescription = "收藏"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.items.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(start = 24.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    uiState.questionSet?.let { questionSet ->
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = questionSet.title,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "�?${questionSet.questionCount} �?,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (uiState.availableTags.isNotEmpty()) {
                                TagFilterRow(
                                    tags = uiState.availableTags,
                                    selectedTag = uiState.selectedTag,
                                    onTagClick = viewModel::selectTag
                                )
                            }
                        }
                    }
                }

                if (uiState.items.isEmpty()) {
                    item {
                        AppSectionCard(
                            title = if (uiState.allItems.isEmpty()) "题库暂时不可�? else "当前筛选结果为�?,
                            body = uiState.message ?: if (uiState.allItems.isEmpty()) "还没有加载到题目内容�? else "换个标签试试看�?
                        )
                    }
                } else {
                    items(uiState.items, key = { it.id }) { item ->
                        QuestionItemCard(item = item)
                    }
                }
            }
        }
    }
}
