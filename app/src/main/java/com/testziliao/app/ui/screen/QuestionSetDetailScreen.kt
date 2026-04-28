package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testziliao.app.ui.AppViewModelFactories
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.TagFilterRow
import com.testziliao.app.ui.question.QuestionSetDetailViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSetDetailScreen(
    questionSetId: String,
    onBackClick: () -> Unit,
    viewModel: QuestionSetDetailViewModel = viewModel(factory = AppViewModelFactories.questionSetDetail(LocalContext.current, questionSetId))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val progress = if (uiState.items.isEmpty()) 0f else (uiState.currentIndex + 1).toFloat() / uiState.items.size.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.questionSet?.title ?: "面试题") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "收藏")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    uiState.questionSet?.let { questionSet ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = questionSet.title,
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                        Text(
                                            text = "适合系统刷题、碎片阅读和面试前快速回顾",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AssistChip(onClick = {}, label = { Text("${questionSet.questionCount} 题") })
                                    AssistChip(onClick = {}, label = { Text(uiState.selectedTag) })
                                    AssistChip(onClick = {}, label = { Text(uiState.progressLabel) })
                                }

                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                )

                                Text(
                                    text = "更新于 ${questionSet.updatedAt}",
                                    style = MaterialTheme.typography.bodySmall,
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
                }

                if (uiState.currentItem == null) {
                    item {
                        AppSectionCard(
                            title = if (uiState.allItems.isEmpty()) "题库暂不可用" else "当前筛选下暂无题目",
                            body = uiState.message ?: if (uiState.allItems.isEmpty()) "题库内容还没有加载完成。" else "换一个标签试试。"
                        )
                    }
                } else {
                    item {
                        val item = uiState.currentItem
                        val difficultyText = when (item?.difficulty?.lowercase()) {
                            "easy" -> "基础"
                            "medium" -> "进阶"
                            "hard" -> "高阶"
                            else -> item?.difficulty ?: "题目"
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            SelectionContainer {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = "第 ${uiState.currentIndex + 1} 题",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = item?.question.orEmpty(),
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                        AssistChip(
                                            onClick = {},
                                            label = { Text(difficultyText) }
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        AssistChip(
                                            onClick = {},
                                            label = { Text(uiState.selectedTag) }
                                        )
                                        AssistChip(
                                            onClick = {},
                                            label = { Text("问答卡片") }
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(
                                            imageVector = Icons.Rounded.Code,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "答案支持代码块阅读，适合边看边背。",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    MarkdownText(
                                        markdown = item?.answer.orEmpty(),
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 15.sp,
                                            lineHeight = 27.sp
                                        ),
                                        linkColor = MaterialTheme.colorScheme.primary,
                                        headingBreakColor = MaterialTheme.colorScheme.outlineVariant,
                                        enableUnderlineForLink = false,
                                        isTextSelectable = true,
                                        enableSoftBreakAddsNewLine = true
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = viewModel::goToPrevious,
                                enabled = uiState.canGoPrevious,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = null
                                )
                                Text(
                                    text = "上一题",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Button(
                                onClick = viewModel::goToNext,
                                enabled = uiState.canGoNext,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "下一题",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
