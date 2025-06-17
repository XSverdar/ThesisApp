package com.thesisapp.presentation

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var btnExport: LinearLayout
    private lateinit var btnWatch: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        btnReturn = findViewById(R.id.btnReturnSettings)
        btnExport = findViewById(R.id.btnExport)
        btnWatch = findViewById(R.id.btnWatch)

        btnReturn.setOnClickListener { finish() }
        btnExport.setOnClickListener { showExportMenu() }
        btnWatch.setOnClickListener { showWatchMenu() }
    }

    private fun showExportMenu() {
        val popup = PopupMenu(this, btnExport)
        popup.menu.add(0, 0, 0, "Export Swim History")
        popup.menu.add(0, 1, 1, "Clear All Data")

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                0 -> {
                    Toast.makeText(this, "Exporting Swim History", Toast.LENGTH_SHORT).show()
                    true
                }
                1 -> {
                    Toast.makeText(this, "Clearing All Data", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showWatchMenu() {
        val popup = PopupMenu(this, btnWatch)
        popup.menu.add(0, 0, 0, "View Connection Status")

        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == 0) {
                Toast.makeText(this, "Showing Connection Status", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
        popup.show()
    }
}