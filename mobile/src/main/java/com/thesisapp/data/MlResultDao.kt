package com.thesisapp.data

import androidx.room.*

@Dao
interface MlResultDao {
    @Insert
    suspend fun insert(mlResult: MlResult)

    @Query("SELECT MAX(sessionId) FROM ml_results")
    fun getMaxSessionId(): Int?

    @Query("SELECT * FROM ml_results WHERE sessionId = :sessionId")
    fun getBySessionId(sessionId: Int): MlResult

    @Query("SELECT sessionId, date FROM ml_results ORDER BY sessionId ASC")
    fun getSessionSummaries(): List<SessionOnly>

    @Query("DELETE FROM ml_results")
    fun clearAll()
}
