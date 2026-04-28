package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.FavoriteQuestionSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteQuestionSetDao {
    @Query("SELECT * FROM favorite_question_sets ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<FavoriteQuestionSetEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_question_sets WHERE questionSetId = :questionSetId)")
    fun observeIsFavorite(questionSetId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_question_sets WHERE questionSetId = :questionSetId)")
    suspend fun isFavorite(questionSetId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteQuestionSetEntity)

    @Query("DELETE FROM favorite_question_sets WHERE questionSetId = :questionSetId")
    suspend fun deleteByQuestionSetId(questionSetId: String)
}
