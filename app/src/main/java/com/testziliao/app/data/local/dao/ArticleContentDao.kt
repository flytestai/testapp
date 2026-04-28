package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.ArticleContentEntity

@Dao
interface ArticleContentDao {
    @Query("SELECT * FROM article_contents WHERE articleId = :articleId LIMIT 1")
    suspend fun getByArticleId(articleId: String): ArticleContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ArticleContentEntity)

    @Query("DELETE FROM article_contents WHERE articleId = :articleId")
    suspend fun deleteByArticleId(articleId: String)
}
