package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.QuestionSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionSetDao {
    @Query("SELECT * FROM question_sets ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<QuestionSetEntity>>

    @Query("SELECT * FROM question_sets WHERE categoryId = :categoryId ORDER BY updatedAt DESC")
    fun observeByCategory(categoryId: String): Flow<List<QuestionSetEntity>>

    @Query("SELECT * FROM question_sets WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): QuestionSetEntity?

    @Query("""
        SELECT * FROM question_sets
        WHERE title LIKE '%' || :keyword || '%'
        ORDER BY updatedAt DESC
    """)
    fun search(keyword: String): Flow<List<QuestionSetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<QuestionSetEntity>)

    @Query("DELETE FROM question_sets")
    suspend fun clearAll()
}
