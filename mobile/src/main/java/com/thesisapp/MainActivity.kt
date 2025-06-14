package com.thesisapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnConnect: Button
    private var isSmartwatchConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)

        btnConnect = findViewById(R.id.btnConnect)
        val btnStartTracking = findViewById<Button>(R.id.btnStartTracking)
        val btnViewHistory = findViewById<Button>(R.id.btnViewHistory)
        val btnManageSwimmers = findViewById<Button>(R.id.btnManageSwimmers)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        // Initial button setup
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
                val swimmers = DatabaseHelper(this).getAllSwimmers()
                if (swimmers.isNotEmpty()) {
                    // Navigate to track_swimmer_selection
                    val intent = Intent(this, TrackSwimmerSelectionActivity::class.java)
                    startActivity(intent)
                } else {
                    // Navigate to track_no_swimmer
                    val intent = Intent(this, TrackNoSwimmerActivity::class.java)
                    startActivity(intent)
                }
            }
        }


        btnViewHistory.setOnClickListener {
            Toast.makeText(this, "View History clicked!", Toast.LENGTH_SHORT).show()
        }

        btnManageSwimmers.setOnClickListener {
            Toast.makeText(this, "Manage Swimmers clicked!", Toast.LENGTH_SHORT).show()
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSmartwatchButton() {
        if (isSmartwatchConnected) {
            btnConnect.text = "Disconnect Smartwatch"
            btnConnect.backgroundTintList = ContextCompat.getColorStateList(this, R.color.disconnect)

            // Enable Start button
            val btnStart = findViewById<Button>(R.id.btnStartTracking)
            btnStart.isEnabled = true
            btnStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_swim_on, 0, 0, 0)
            btnStart.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            btnConnect.text = "Connect Smartwatch"
            btnConnect.backgroundTintList = ContextCompat.getColorStateList(this, R.color.connect)

            // Disable Start button
            val btnStart = findViewById<Button>(R.id.btnStartTracking)
            btnStart.isEnabled = false
            btnStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_swim_off, 0, 0, 0)
            btnStart.setTextColor(ContextCompat.getColor(this, R.color.disabled_text))
        }
    }

    private fun openGoogleWatchApp() {
        try {
            val intent = packageManager.getLaunchIntentForPackage("com.google.android.wearable.app")
            if (intent != null) {
                startActivity(intent)
            } else {
                // If Wear app not found, open Play Store
                val playIntent = Intent(Intent.ACTION_VIEW)
                playIntent.data = Uri.parse("market://details?id=com.google.android.wearable.app")
                startActivity(playIntent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Google Watch app not found", Toast.LENGTH_SHORT).show()
        }
    }
}