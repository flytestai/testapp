package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.FavoriteArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteArticleDao {
    @Query("SELECT * FROM favorite_articles ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<FavoriteArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_articles WHERE articleId = :articleId)")
    fun observeIsFavorite(articleId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_articles WHERE articleId = :articleId)")
    suspend fun isFavorite(articleId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteArticleEntity)

    @Query("DELETE FROM favorite_articles WHERE articleId = :articleId")
    suspend fun deleteByArticleId(articleId: String)
}
