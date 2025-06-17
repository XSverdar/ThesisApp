package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.PopupWindow
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var exportData: LinearLayout
    private lateinit var clearAllData: LinearLayout
    private lateinit var watchStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        btnReturn = findViewById(R.id.btnReturn)
        exportData = findViewById(R.id.exportData)
        clearAllData = findViewById(R.id.clearAllData)
        watchStatus = findViewById(R.id.watchStatus)

        // Handle return button
        btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handle export data
        exportData.setOnClickListener {
            val intent = Intent(this, SettingsExportActivity::class.java)
            startActivity(intent)
        }

        // Handle clear all data
        clearAllData.setOnClickListener {
            showClearConfirmation()
        }
    }

    private fun showClearConfirmation() {
        val view = LayoutInflater.from(this).inflate(R.layout.settings_clear, null)

        val popup = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popup.showAtLocation(findViewById(R.id.rootLayout), Gravity.CENTER, 0, 0)

        val btnCancel = view.findViewById<Button>(0) // Update IDs in your settings_clear
        val btnOK = view.findViewById<Button>(0)

        // Handle cancel
        btnCancel?.setOnClickListener {
            popup.dismiss()
        }
        // Handle clear
        btnOK?.setOnClickListener {
            clearAllSwimmers()
            Toast.makeText(this, "All data cleared.", Toast.LENGTH_SHORT).show()
            popup.dismiss()
        }
    }

    private fun clearAllSwimmers() {
        Thread {
            val db = com.thesisapp.data.AppDatabase.getDatabase(this)
            db.swimmerDao().deleteAll()
        }.start()
    }
}