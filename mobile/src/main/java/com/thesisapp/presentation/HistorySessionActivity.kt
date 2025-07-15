package com.thesisapp.presentation

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R
import com.thesisapp.utils.animateClick

class HistorySessionActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var tvDateDetails: TextView
    private lateinit var tvStartTimeDetails: TextView
    private lateinit var tvEndTimeDetails: TextView
    private lateinit var tvDistanceDetails: TextView
    private lateinit var tvStrokeDetails: TextView
    private lateinit var tvPaceDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_session)

        btnReturn = findViewById(R.id.btnReturn)
        tvDateDetails = findViewById(R.id.tvDate)
        tvStartTimeDetails = findViewById(R.id.tvStart)
        tvEndTimeDetails = findViewById(R.id.tvEnd)
        tvDistanceDetails = findViewById(R.id.tvDistance)
        tvStrokeDetails = findViewById(R.id.tvStroke)
        tvPaceDetails = findViewById(R.id.tvPace)

        val date = intent.getStringExtra("date")
        val startTime = intent.getStringExtra("startTime")
        val endTime = intent.getStringExtra("endTime")
        val distance = intent.getStringExtra("distance")
        val stroke = intent.getStringExtra("stroke")
        val pace = intent.getStringExtra("pace")

        // TODO: Retrieve from db
        if (date != null) tvDateDetails.text = "Date: $date"
        if (startTime != null) tvStartTimeDetails.text = "Start: $startTime"
        if (endTime != null) tvEndTimeDetails.text = "End: $endTime"
        if (distance != null) tvDistanceDetails.text = "Distance: $distance m"
        if (stroke != null) tvStrokeDetails.text = "Stroke: $stroke"
        if (pace != null) tvPaceDetails.text = "Average Pace: $pace"

        btnReturn.setOnClickListener {
            it.animateClick()
            finish()
        }
    }
}