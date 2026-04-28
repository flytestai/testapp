package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.QuestionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionItemDao {
    @Query("SELECT * FROM question_items WHERE questionSetId = :questionSetId ORDER BY sortOrder ASC")
    fun observeByQuestionSetId(questionSetId: String): Flow<List<QuestionItemEntity>>

    @Query("""
        SELECT * FROM question_items
        WHERE question LIKE '%' || :keyword || '%'
           OR tags LIKE '%' || :keyword || '%'
        ORDER BY sortOrder ASC
    """)
    fun search(keyword: String): Flow<List<QuestionItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<QuestionItemEntity>)

    @Query("DELETE FROM question_items WHERE questionSetId = :questionSetId")
    suspend fun deleteByQuestionSetId(questionSetId: String)
}
