package com.thesisapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainDashboardActivity : AppCompatActivity() {

    private lateinit var btnConnectWatch: Button
    private lateinit var btnStartTracking: Button
    private lateinit var btnViewHistory: Button
    private lateinit var btnManageSwimmers: Button
    private lateinit var btnSettings: Button

    private var isWatchConnected = false

    // Local Room DB
    private var hasRegisteredSwimmers = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)

        btnConnectWatch = findViewById(R.id.btnConnect)
        btnStartTracking = findViewById(R.id.btnStartTracking)
        btnViewHistory = findViewById(R.id.btnViewHistory)
        btnManageSwimmers = findViewById(R.id.btnManageSwimmers)
        btnSettings = findViewById(R.id.btnSettings)

        updateButtonStates()

        btnConnectWatch.setOnClickListener {
            if (!isWatchConnected) {
                startActivity(Intent(this, ConnectPromptActivity::class.java))
                // Simulate connection complete for demo
                isWatchConnected = true
                updateButtonStates()
            } else {
                isWatchConnected = false
                updateButtonStates()
            }
        }

        btnStartTracking.setOnClickListener {
            if (hasRegisteredSwimmers) {
                startActivity(Intent(this, TrackSwimmerSelectionActivity::class.java))
            } else {
                startActivity(Intent(this, TrackNoSwimmerActivity::class.java))
            }
        }

        btnViewHistory.setOnClickListener {
            startActivity(Intent(this, HistoryListActivity::class.java))
        }

        btnManageSwimmers.setOnClickListener {
            startActivity(Intent(this, SwimmerListActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun updateButtonStates() {
        if (isWatchConnected) {
            btnStartTracking.isEnabled = true
            btnStartTracking.setTextColor(getColor(R.color.black))
            btnStartTracking.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_swim_on, 0, 0)
        } else {
            btnStartTracking.isEnabled = false
            btnStartTracking.setTextColor(getColor(R.color.disabled_text))
            btnStartTracking.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_swim_off, 0, 0)
        }

        btnConnectWatch.text = if (isWatchConnected) "Disconnect Smartwatch" else "Connect Smartwatch"
    }
}