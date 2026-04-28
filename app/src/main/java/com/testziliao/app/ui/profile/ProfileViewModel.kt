package com.testziliao.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.HistoryRepository
import com.testziliao.app.data.repository.SearchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val historyRepository: HistoryRepository,
    searchRepository: SearchRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        historyRepository.observeRecentHistory(),
        searchRepository.observeRecentSearches()
    ) { history, searches ->
        ProfileUiState(history = history, searchHistory = searches)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }

    companion object {
        fun factory(
            historyRepository: HistoryRepository,
            searchRepository: SearchRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileViewModel(historyRepository, searchRepository) as T
                }
            }
        }
    }
}
