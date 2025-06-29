package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thesisapp.R
import com.thesisapp.utils.animateClick

class HistoryListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnReturn: ImageButton

    // TODO: Change this sessions to retrieve the data from the db
    private val sessions = listOf(
        Session("February 11, 2025", "6:10 AM", "6:10 AM", "7:35 AM"),
        Session("February 12, 2025", "5:30 AM", "5:30 AM", "6:45 AM"),
        Session("February 13, 2025", "7:15 AM", "7:15 AM", "8:30 AM"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_list)

        btnReturn = findViewById(R.id.btnReturn)
        recyclerView = findViewById(R.id.recyclerViewSessions)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HistoryListAdapter(sessions) { session ->
            val intent = Intent(this, HistorySessionActivity::class.java).apply {
                putExtra("date", session.date)
                putExtra("time", session.time)
                putExtra("startTime", session.startTime)
                putExtra("endTime", session.endTime)
                putExtra("distance", session.distance)
                putExtra("stroke", session.stroke)
                putExtra("pace", session.pace)
                putExtra("notes", session.notes)
            }
            startActivity(intent)
        }

        btnReturn.setOnClickListener {
            it.animateClick()
            finish()
        }
    }
}