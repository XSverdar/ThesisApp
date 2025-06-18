package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var btnExport: LinearLayout
    private lateinit var btnWatch: LinearLayout
    private lateinit var btnClearAllData: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        btnReturn = findViewById(R.id.btnReturn)
        btnExport = findViewById(R.id.btnExport)
        btnWatch = findViewById(R.id.btnWatch)
        btnClearAllData = findViewById(R.id.btnClearAllData)

        // Navigate to settings_export.xml screen
        btnExport.setOnClickListener {
            val intent = Intent(this, SettingsExportActivity::class.java)
            startActivity(intent)
        }

        // Show connection status
        btnWatch.setOnClickListener {
            // You could improve this with live connection status using NodeClient if needed
            Toast.makeText(this, "Smartwatch Connection: Under Development", Toast.LENGTH_SHORT).show()
        }

        // Show confirmation popup for clearing all data
        btnClearAllData.setOnClickListener {
            showClearDataPopup()
        }

        // Return to dashboard
        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun showClearDataPopup() {
        val dialogView = layoutInflater.inflate(R.layout.settings_clear, null)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnOK).setOnClickListener {
            // TODO: Clear all local database entries
            Toast.makeText(this, "All data cleared", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}