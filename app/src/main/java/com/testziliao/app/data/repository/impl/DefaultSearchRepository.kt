package com.testziliao.app.data.repository.impl

import com.testziliao.app.data.local.dao.SearchHistoryDao
import com.testziliao.app.data.local.entity.SearchHistoryEntity
import com.testziliao.app.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class DefaultSearchRepository(
    private val searchHistoryDao: SearchHistoryDao
) : SearchRepository {

    override fun observeRecentSearches(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.observeRecent()
    }

    override suspend fun saveKeyword(keyword: String) {
        val normalized = keyword.trim()
        if (normalized.isEmpty()) return

        searchHistoryDao.insert(
            SearchHistoryEntity(
                keyword = normalized,
                searchedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun clearSearchHistory() {
        searchHistoryDao.clearAll()
    }
}
