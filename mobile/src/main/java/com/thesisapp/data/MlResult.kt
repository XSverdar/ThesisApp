package com.thesisapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ml_results")
data class MlResult(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int,
    val date: String,
    val timeStart: String,
    val timeEnd: String,
    val distance: Float,

    // Stroke distribution
    val backstroke: Float,
    val breaststroke: Float,
    val butterfly: Float,
    val freestyle: Float,

    // Average pace
    val paceBackstroke: Float,
    val paceBreaststroke: Float,
    val paceButterfly: Float,
    val paceFreestyle: Float,

    val lap: Int,

    // Notes field
    val notes: String
)