package com.testziliao.app.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.ContentRepository
import com.testziliao.app.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    favoriteRepository: FavoriteRepository,
    contentRepository: ContentRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = combine(
        favoriteRepository.observeFavoriteArticles(),
        favoriteRepository.observeFavoriteQuestionSets(),
        contentRepository.observeArticles(),
        contentRepository.observeQuestionSets()
    ) { articleFavorites, questionSetFavorites, articles, questionSets ->
        val articleIds = articleFavorites.map { it.articleId }.toSet()
        val questionSetIds = questionSetFavorites.map { it.questionSetId }.toSet()
        FavoritesUiState(
            articleFavorites = articleFavorites,
            questionSetFavorites = questionSetFavorites,
            favoriteArticles = articles.filter { it.id in articleIds },
            favoriteQuestionSets = questionSets.filter { it.id in questionSetIds }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoritesUiState()
    )

    companion object {
        fun factory(
            favoriteRepository: FavoriteRepository,
            contentRepository: ContentRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritesViewModel(
                        favoriteRepository = favoriteRepository,
                        contentRepository = contentRepository
                    ) as T
                }
            }
        }
    }
}
