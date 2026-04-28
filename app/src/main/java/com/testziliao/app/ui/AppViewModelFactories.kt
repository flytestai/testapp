package com.testziliao.app.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.testziliao.app.TestAppApplication
import com.testziliao.app.ui.article.ArticleDetailViewModel
import com.testziliao.app.ui.article.ArticleListViewModel
import com.testziliao.app.ui.category.CategoryViewModel
import com.testziliao.app.ui.favorites.FavoritesViewModel
import com.testziliao.app.ui.home.HomeViewModel
import com.testziliao.app.ui.profile.ProfileViewModel
import com.testziliao.app.ui.question.QuestionSetDetailViewModel
import com.testziliao.app.ui.question.QuestionSetListViewModel
import com.testziliao.app.ui.search.SearchViewModel

object AppViewModelFactories {
    fun home(context: Context): ViewModelProvider.Factory {
        val container = context.appContainer()
        return HomeViewModel.factory(container.contentRepository)
    }

    fun category(context: Context): ViewModelProvider.Factory {
        val container = context.appContainer()
        return CategoryViewModel.factory(container.contentRepository)
    }

    fun articleList(
        context: Context,
        categoryId: String,
        categoryName: String
    ): ViewModelProvider.Factory {
        val container = context.appContainer()
        return ArticleListViewModel.factory(
            contentRepository = container.contentRepository,
            categoryId = categoryId,
            categoryName = categoryName
        )
    }

    fun articleDetail(
        context: Context,
        articleId: String
    ): ViewModelProvider.Factory {
        val container = context.appContainer()
        return ArticleDetailViewModel.factory(
            articleId = articleId,
            contentRepository = container.contentRepository,
            favoriteRepository = container.favoriteRepository,
            historyRepository = container.historyRepository
        )
    }

    fun questionSetList(
        context: Context,
        categoryId: String,
        categoryName: String
    ): ViewModelProvider.Factory {
        val container = context.appContainer()
        return QuestionSetListViewModel.factory(
            contentRepository = container.contentRepository,
            categoryId = categoryId,
            categoryName = categoryName
        )
    }

    fun questionSetDetail(
        context: Context,
        questionSetId: String
    ): ViewModelProvider.Factory {
        val container = context.appContainer()
        return QuestionSetDetailViewModel.factory(
            questionSetId = questionSetId,
            contentRepository = container.contentRepository,
            favoriteRepository = container.favoriteRepository,
            historyRepository = container.historyRepository
        )
    }

    fun favorites(context: Context): ViewModelProvider.Factory {
        val container = context.appContainer()
        return FavoritesViewModel.factory(
            favoriteRepository = container.favoriteRepository,
            contentRepository = container.contentRepository
        )
    }

    fun profile(context: Context): ViewModelProvider.Factory {
        val container = context.appContainer()
        return ProfileViewModel.factory(
            historyRepository = container.historyRepository,
            searchRepository = container.searchRepository
        )
    }

    fun search(context: Context): ViewModelProvider.Factory {
        val container = context.appContainer()
        return SearchViewModel.factory(
            contentRepository = container.contentRepository,
            searchRepository = container.searchRepository
        )
    }
}

private fun Context.appContainer() =
    (applicationContext as TestAppApplication).appContainer
