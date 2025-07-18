package com.thesisapp.data

import androidx.room.*
import com.thesisapp.presentation.Session

@Dao
interface MlResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: MlResult)

    @Query("SELECT id, date, timeStart FROM mlresults ORDER BY date DESC")
    fun getSessionSummaries(): List<Session>

    @Query("SELECT * FROM mlresults WHERE id = :id")
    fun getById(id: Int): MlResult?

    @Query("SELECT * FROM mlresults ORDER BY id DESC")
    suspend fun getAll(): List<MlResult>

    @Query("DELETE FROM mlresults")
    suspend fun clearAll()

    @Delete
    suspend fun delete(result: MlResult)
}