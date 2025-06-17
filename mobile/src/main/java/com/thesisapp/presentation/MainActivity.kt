package com.thesisapp.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.widget.ImageView
import android.content.ContentResolver
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var btnConnect: Button
    private var isSmartwatchConnected = false
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)

        btnConnect = findViewById(R.id.btnConnect)
        val btnStartTracking = findViewById<Button>(R.id.btnStartTracking)
        val btnViewHistory = findViewById<Button>(R.id.btnViewHistory)
        val btnManageSwimmers = findViewById<Button>(R.id.btnManageSwimmers)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        // Initialize Database
        db = AppDatabase.getDatabase(this)

        // Initial button setup
        updateSmartwatchButton()

        btnConnect.setOnClickListener {
            if (!isSmartwatchConnected) {
                // Attempt to connect
                openGoogleWatchApp()
                isSmartwatchConnected = true
                updateSmartwatchButton()
            } else {
                // Disconnect
                isSmartwatchConnected = false
                updateSmartwatchButton()
                Toast.makeText(this, "Smartwatch disconnected", Toast.LENGTH_SHORT).show()
            }
        }

        btnStartTracking.setOnClickListener {
            if (isSmartwatchConnected) {
                lifecycleScope.launch {
                    val swimmers = db.swimmerDao().getAllSwimmers()
                    if (swimmers.isNotEmpty()) {
                        // Navigate to track_swimmer_selection
                        val intent = Intent(this@MainActivity, TrackSwimmerSelectionActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Navigate to track_no_swimmer
                        val intent = Intent(this@MainActivity, TrackNoSwimmerActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        btnViewHistory.setOnClickListener {
            val intent = Intent(this, HistoryListActivity::class.java)
            startActivity(intent)
        }

        btnManageSwimmers.setOnClickListener {
            Toast.makeText(this, "Manage Swimmers clicked!", Toast.LENGTH_SHORT).show()
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateSmartwatchButton() {
        if (isSmartwatchConnected) {
            btnConnect.text = "Disconnect Smartwatch"
            btnConnect.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.disconnect)

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