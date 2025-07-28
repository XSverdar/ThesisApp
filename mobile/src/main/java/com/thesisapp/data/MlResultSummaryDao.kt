package com.thesisapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MlResultSummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSummary(summary: MlResultSummary)

    @Query("SELECT * FROM ml_result_summary WHERE sessionId = :sessionId")
    fun getSummaryForSession(sessionId: Int): MlResultSummary?
}

