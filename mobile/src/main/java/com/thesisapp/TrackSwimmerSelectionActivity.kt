package com.thesisapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import com.thesisapp.adapter.SwimmerAdapter
import com.thesisapp.model.Swimmer

class TrackSwimmerSelectionActivity : AppCompatActivity() {

    private lateinit var swimmerList: RecyclerView
    private lateinit var btnAddSwimmer: Button
    private lateinit var btnContinue: Button

    private var selectedSwimmer: Swimmer? = null

    // Simulating fetching from database
    private val swimmers = listOf(
        Swimmer(1, "Ryan Suarez", 15, "Junior"),
        Swimmer(2, "Amber Tongson", 14, "Junior"),
        Swimmer(3, "Alexander Chu", 16, "Senior"),
        Swimmer(4, "Joan Perez", 13, "Junior"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_swimmer_selection)

        swimmerList = findViewById(R.id.swimmerList)
        btnAddSwimmer = findViewById(R.id.btnAddSwimmer)
        btnContinue = findViewById(R.id.btnContinue)

        swimmerList.layoutManager = LinearLayoutManager(this)
        swimmerList.adapter = SwimmerAdapter(swimmers) { swimmer ->
            selectedSwimmer = swimmer
        }

        btnAddSwimmer.setOnClickListener {
            val intent = Intent(this, TrackAddSwimmerActivity::class.java)
            startActivity(intent)
        }

        btnContinue.setOnClickListener {
            if (selectedSwimmer != null) {
                val intent = Intent(this, TrackSwimmerActivity::class.java)
                intent.putExtra("SwimmerId", selectedSwimmer!!.id)
                startActivity(intent)
            }
        }
    }
}