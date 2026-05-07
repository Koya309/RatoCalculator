package com.example.ratocalculator.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao{
    @Insert
    suspend fun insert(history: HistoryEntity)

    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM calculation_history")
    suspend fun clearAll()
}