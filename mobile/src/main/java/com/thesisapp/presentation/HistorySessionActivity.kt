package com.thesisapp.presentation

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R

class HistorySessionActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var tvDateDetails: TextView
    private lateinit var tvStartTimeDetails: TextView
    private lateinit var tvEndTimeDetails: TextView
    private lateinit var tvDistanceDetails: TextView
    private lateinit var tvStrokeDetails: TextView
    private lateinit var tvPaceDetails: TextView
    private lateinit var tvNotesDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_session)

        btnReturn = findViewById(R.id.btnReturn)
        tvDateDetails = findViewById(R.id.tvDateDetails)
        tvStartTimeDetails = findViewById(R.id.tvStartTimeDetails)
        tvEndTimeDetails = findViewById(R.id.tvEndTimeDetails)
        tvDistanceDetails = findViewById(R.id.tvDistanceDetails)
        tvStrokeDetails = findViewById(R.id.tvStrokeDetails)
        tvPaceDetails = findViewById(R.id.tvPaceDetails)
        tvNotesDetails = findViewById(R.id.tvNotesDetails)

        val date = intent.getStringExtra("date")
        val startTime = intent.getStringExtra("startTime")
        val endTime = intent.getStringExtra("endTime")
        val distance = intent.getStringExtra("distance")
        val stroke = intent.getStringExtra("stroke")
        val pace = intent.getStringExtra("pace")
        val notes = intent.getStringExtra("notes")

        // TODO: Retrieve from db
        if (date != null) tvDateDetails.text = "Date: $date"
        if (startTime != null) tvStartTimeDetails.text = "Start: $startTime"
        if (endTime != null) tvEndTimeDetails.text = "End: $endTime"
        if (distance != null) tvDistanceDetails.text = "Distance: $distance m"
        if (stroke != null) tvStrokeDetails.text = "Stroke: $stroke"
        if (pace != null) tvPaceDetails.text = "Average Pace: $pace"
        if (notes != null) tvNotesDetails.text = "Notes: $notes"

        btnReturn.setOnClickListener {
            finish()
        }
    }
}