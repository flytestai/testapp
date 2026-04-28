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
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.QuestionSetCard
import com.testziliao.app.ui.components.SectionHeader
import com.testziliao.app.ui.question.QuestionSetListViewModel

@Composable
fun QuestionSetListScreen(
    categoryId: String,
    categoryName: String,
    onQuestionSetClick: (String) -> Unit,
    viewModel: QuestionSetListViewModel = viewModel(
        factory = AppViewModelFactories.questionSetList(LocalContext.current, categoryId, categoryName)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = uiState.categoryName, subtitle = "按专题浏览题库内容")
        if (uiState.questionSets.isEmpty()) {
            AppSectionCard(title = "暂无题库", body = uiState.message ?: "这个分类下还没有题库内容。")
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.questionSets, key = { it.id }) { questionSet -> QuestionSetCard(questionSet = questionSet, onClick = { onQuestionSetClick(questionSet.id) }) }
            }
        }
    }
}
