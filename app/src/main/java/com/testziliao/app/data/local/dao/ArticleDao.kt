package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun observeAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE categoryId = :categoryId ORDER BY publishedAt DESC")
    fun observeByCategory(categoryId: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE featured = 1 ORDER BY publishedAt DESC")
    fun observeFeatured(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ArticleEntity?

    @Query("""
        SELECT * FROM articles
        WHERE title LIKE '%' || :keyword || '%'
           OR summary LIKE '%' || :keyword || '%'
           OR tags LIKE '%' || :keyword || '%'
        ORDER BY publishedAt DESC
    """)
    fun search(keyword: String): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}
