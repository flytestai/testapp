package com.testziliao.app.ui.profile

import com.testziliao.app.data.local.entity.HistoryRecordEntity
import com.testziliao.app.data.local.entity.SearchHistoryEntity

data class ProfileUiState(
    val history: List<HistoryRecordEntity> = emptyList(),
    val searchHistory: List<SearchHistoryEntity> = emptyList()
)
