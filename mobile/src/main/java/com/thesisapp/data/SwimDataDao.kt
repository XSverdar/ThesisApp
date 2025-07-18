package com.thesisapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SwimDataDao {
    @Insert
    suspend fun insert(swimData: SwimData)

    @Query("SELECT * FROM swim_data WHERE timestamp BETWEEN :from AND :to")
    suspend fun getSwimsBetweenDates(from: Long, to: Long): List<SwimData>

    @Query("SELECT COUNT(*) FROM swim_data WHERE timestamp BETWEEN :from AND :to")
    suspend fun countSwimsBetweenDates(from: Long, to: Long): Int

    @Query("DELETE FROM swim_data")
    suspend fun clearAll()
}