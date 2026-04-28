package com.testziliao.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testziliao.app.data.local.entity.HistoryRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_records ORDER BY viewedAt DESC LIMIT 100")
    fun observeRecent(): Flow<List<HistoryRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HistoryRecordEntity)

    @Query("DELETE FROM history_records WHERE targetId = :targetId AND targetType = :targetType")
    suspend fun deleteByTarget(targetId: String, targetType: String)

    @Query("DELETE FROM history_records")
    suspend fun clearAll()
}
