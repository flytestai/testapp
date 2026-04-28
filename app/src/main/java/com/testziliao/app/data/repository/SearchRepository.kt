package com.testziliao.app.data.repository

import com.testziliao.app.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun observeRecentSearches(): Flow<List<SearchHistoryEntity>>
    suspend fun saveKeyword(keyword: String)
    suspend fun clearSearchHistory()
}
