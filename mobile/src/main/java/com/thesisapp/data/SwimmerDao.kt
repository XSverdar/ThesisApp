package com.thesisapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SwimmerDao {

    @Insert
    suspend fun insertSwimmer(swimmer: Swimmer)

    @Query("SELECT * FROM swimmer")
    suspend fun getAllSwimmers(): List<Swimmer>
}