package com.thesisapp.data

import androidx.room.*

@Dao
interface MlResultDao {
    @Insert
    fun insert(mlResult: MlResult): Long

    @Query("SELECT MAX(sessionId) FROM ml_results")
    fun getMaxSessionId(): Int?

    @Query("SELECT * FROM ml_results WHERE sessionId = :sessionId")
    fun getBySessionId(sessionId: Int): MlResult

    @Query("SELECT sessionId, date FROM ml_results ORDER BY sessionId ASC")
    fun getSessionSummaries(): List<SessionOnly>

    @Query("DELETE FROM ml_results")
    fun clearAll()
}

data class MlResultSummary(
    val sessionId: Int,
    val date: String
)
