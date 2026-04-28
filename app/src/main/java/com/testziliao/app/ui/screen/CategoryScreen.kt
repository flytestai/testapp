package com.testziliao.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
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
import com.testziliao.app.ui.category.CategoryViewModel
import com.testziliao.app.ui.components.AppSectionCard

@Composable
fun CategoryScreen(
    onCategoryClick: (String, String, String) -> Unit,
    viewModel: CategoryViewModel = viewModel(
        factory = AppViewModelFactories.category(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "内容分类",
            style = MaterialTheme.typography.headlineSmall
        )
        if (uiState.categories.isEmpty()) {
            Text(
                text = uiState.message ?: "当前还没有远程分类数据，先保留这个占位�?,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            uiState.categories.forEach { category ->
                AppSectionCard(
                    title = category.name,
                    body = "类型�?{category.type}",
                    modifier = Modifier.clickable {
                        onCategoryClick(category.id, category.name, category.type)
                    }
                )
            }
        }
    }
}
