package com.testziliao.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.testziliao.app.data.local.dao.ArticleContentDao
import com.testziliao.app.data.local.dao.ArticleDao
import com.testziliao.app.data.local.dao.CategoryDao
import com.testziliao.app.data.local.dao.FavoriteArticleDao
import com.testziliao.app.data.local.dao.FavoriteQuestionSetDao
import com.testziliao.app.data.local.dao.HistoryDao
import com.testziliao.app.data.local.dao.QuestionItemDao
import com.testziliao.app.data.local.dao.QuestionSetDao
import com.testziliao.app.data.local.dao.SearchHistoryDao
import com.testziliao.app.data.local.entity.ArticleContentEntity
import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.CategoryEntity
import com.testziliao.app.data.local.entity.FavoriteArticleEntity
import com.testziliao.app.data.local.entity.FavoriteQuestionSetEntity
import com.testziliao.app.data.local.entity.HistoryRecordEntity
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity
import com.testziliao.app.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        CategoryEntity::class,
        ArticleEntity::class,
        ArticleContentEntity::class,
        QuestionSetEntity::class,
        QuestionItemEntity::class,
        FavoriteArticleEntity::class,
        FavoriteQuestionSetEntity::class,
        HistoryRecordEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun articleDao(): ArticleDao
    abstract fun articleContentDao(): ArticleContentDao
    abstract fun questionSetDao(): QuestionSetDao
    abstract fun questionItemDao(): QuestionItemDao
    abstract fun favoriteArticleDao(): FavoriteArticleDao
    abstract fun favoriteQuestionSetDao(): FavoriteQuestionSetDao
    abstract fun historyDao(): HistoryDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}
