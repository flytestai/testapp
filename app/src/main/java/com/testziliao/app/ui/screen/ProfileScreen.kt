package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testziliao.app.BuildConfig
import com.testziliao.app.data.model.AppConstants
import com.testziliao.app.ui.AppViewModelFactories
import com.testziliao.app.ui.components.AppSectionCard
import com.testziliao.app.ui.components.HistoryCard
import com.testziliao.app.ui.components.SectionHeader
import com.testziliao.app.ui.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    onArticleClick: (String) -> Unit,
    onQuestionSetClick: (String) -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelFactories.profile(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val packageName = LocalContext.current.packageName

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionHeader(title = "我的", subtitle = "浏览记录、搜索历史与本地设置") }
        item {
            AppSectionCard(
                "应用信息",
                "版本 ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n包名 $packageName"
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppSectionCard(
                    "浏览记录",
                    if (uiState.historyCount == 0) "还没有记录" else "共 ${uiState.historyCount} 条",
                    modifier = Modifier.weight(1f)
                )
                AppSectionCard(
                    "搜索历史",
                    if (uiState.searchCount == 0) "还没有记录" else "共 ${uiState.searchCount} 条",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (uiState.history.isNotEmpty()) {
            items(uiState.history, key = { it.id }) { history ->
                HistoryCard(
                    item = history,
                    onClick = {
                        when (history.targetType) {
                            AppConstants.TARGET_TYPE_ARTICLE -> onArticleClick(history.targetId)
                            AppConstants.TARGET_TYPE_QUESTION_SET -> onQuestionSetClick(history.targetId)
                        }
                    }
                )
            }
        }
        item {
            AppSectionCard(
                "搜索历史",
                if (uiState.searchHistory.isEmpty()) "还没有搜索历史。" else uiState.searchHistory.joinToString(" / ") { it.keyword }
            )
        }
        item { AppSectionCard("缓存与设置", "后面可以继续扩展缓存清理、主题和阅读偏好。") }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = viewModel::clearHistory,
                        enabled = uiState.history.isNotEmpty()
                    ) {
                        Text("清空浏览记录")
                    }
                    OutlinedButton(
                        onClick = viewModel::clearSearchHistory,
                        enabled = uiState.searchHistory.isNotEmpty()
                    ) {
                        Text("清空搜索历史")
                    }
                }
            }
        }
    }
}
