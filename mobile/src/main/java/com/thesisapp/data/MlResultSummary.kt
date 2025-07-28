package com.thesisapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ml_result_summary")
data class MlResultSummary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int,
    val date: String,
    val timeStart: String,
    val timeEnd: String,
    val distance: Float,
    val backstroke: Float,
    val breaststroke: Float,
    val butterfly: Float,
    val freestyle: Float,
    val speed: Float,
    val lap: Int
)
