package com.testziliao.app.data.repository

import com.testziliao.app.data.local.entity.HistoryRecordEntity
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeRecentHistory(): Flow<List<HistoryRecordEntity>>
    suspend fun addHistory(item: HistoryRecordEntity)
    suspend fun clearHistory()
}
