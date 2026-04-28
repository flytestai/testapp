package com.testziliao.app.data.repository.impl

import com.testziliao.app.data.local.dao.HistoryDao
import com.testziliao.app.data.local.entity.HistoryRecordEntity
import com.testziliao.app.data.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class DefaultHistoryRepository(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun observeRecentHistory(): Flow<List<HistoryRecordEntity>> = historyDao.observeRecent()

    override suspend fun addHistory(item: HistoryRecordEntity) {
        historyDao.deleteByTarget(item.targetId, item.targetType)
        historyDao.insert(item)
    }

    override suspend fun clearHistory() {
        historyDao.clearAll()
    }
}
