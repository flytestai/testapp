package com.testziliao.app.ui.navigation

import android.net.Uri

object AppRoutes {
    const val HOME = "home"
    const val CATEGORY = "category"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val ARTICLE_LIST = "article_list"
    const val ARTICLE_DETAIL = "article_detail"
    const val QUESTION_SET_LIST = "question_set_list"
    const val QUESTION_SET_DETAIL = "question_set_detail"
    const val SEARCH = "search"

    const val ARG_CATEGORY_ID = "categoryId"
    const val ARG_CATEGORY_NAME = "categoryName"
    const val ARG_ARTICLE_ID = "articleId"
    const val ARG_QUESTION_SET_ID = "questionSetId"

    fun articleListRoute(categoryId: String, categoryName: String): String {
        return "$ARTICLE_LIST/${Uri.encode(categoryId)}/${Uri.encode(categoryName)}"
    }

    fun articleDetailRoute(articleId: String): String {
        return "$ARTICLE_DETAIL/${Uri.encode(articleId)}"
    }

    fun questionSetListRoute(categoryId: String, categoryName: String): String {
        return "$QUESTION_SET_LIST/${Uri.encode(categoryId)}/${Uri.encode(categoryName)}"
    }

    fun questionSetDetailRoute(questionSetId: String): String {
        return "$QUESTION_SET_DETAIL/${Uri.encode(questionSetId)}"
    }
}
