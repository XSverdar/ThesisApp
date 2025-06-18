package com.thesisapp.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.thesisapp.R
import com.thesisapp.data.AppDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var btnConnect: Button
    private var isSmartwatchConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)

        btnConnect = findViewById(R.id.btnConnect)
        val btnStartTracking = findViewById<Button>(R.id.btnStartTracking)
        val btnViewHistory = findViewById<Button>(R.id.btnViewHistory)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        updateSmartwatchButton()

        btnConnect.setOnClickListener {
            if (!isSmartwatchConnected) {
                // Attempt to open Google Wear OS app
                openGoogleWatchApp()
                // For demonstration: assume connection succeeds after opening
                isSmartwatchConnected = true
                updateSmartwatchButton()
            } else {
                // Simulate disconnect
                isSmartwatchConnected = false
                updateSmartwatchButton()
                Toast.makeText(this, "Smartwatch disconnected", Toast.LENGTH_SHORT).show()
            }
        }

        btnStartTracking.setOnClickListener {
            if (isSmartwatchConnected) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "swimmers"
                ).allowMainThreadQueries().build()

                val swimmers = db.swimmerDao().getAllSwimmers()

                val intent = if (swimmers.isNotEmpty()) {
                    Intent(this, TrackSwimmerActivity::class.java)
                } else {
                    Intent(this, TrackNoSwimmerActivity::class.java)
                }
                startActivity(intent)
            }
        }

        btnViewHistory.setOnClickListener {
            startActivity(Intent(this, HistoryListActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun updateSmartwatchButton() {
        val btnStart = findViewById<Button>(R.id.btnStartTracking)

        if (isSmartwatchConnected) {
            btnConnect.text = "Disconnect Smartwatch"
            btnConnect.backgroundTintList = ContextCompat.getColorStateList(this, R.color.disconnect)

            btnStart.isEnabled = true
            btnStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_swim_on, 0, 0, 0)
            btnStart.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            btnConnect.text = "Connect Smartwatch"
            btnConnect.backgroundTintList = ContextCompat.getColorStateList(this, R.color.connect)

            btnStart.isEnabled = false
            btnStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_swim_off, 0, 0, 0)
            btnStart.setTextColor(ContextCompat.getColor(this, R.color.disabled_text))
        }
    }

    private fun openGoogleWatchApp() {
        try {
            val intent = packageManager.getLaunchIntentForPackage("com.google.android.apps.wear.companion")
            if (intent != null) {
                startActivity(intent)
            } else {
                // If Wear app not found, open Play Store
                val playIntent = Intent(Intent.ACTION_VIEW)
                playIntent.data = Uri.parse("market://details?id=com.google.android.apps.wear.companion")
                startActivity(playIntent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Google Watch app not found", Toast.LENGTH_SHORT).show()
        }
    }
}