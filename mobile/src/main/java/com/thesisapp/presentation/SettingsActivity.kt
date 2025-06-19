package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        btnExport.setOnClickListener {
            val intent = Intent(this, SettingsExportActivity::class.java)
            startActivity(intent)
        }

        btnWatch.setOnClickListener {
            Toast.makeText(this, "Smartwatch Connection: Under Development", Toast.LENGTH_SHORT).show()
        }

        btnClearAllData.setOnClickListener {
            showClearDataPopup()
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun showClearDataPopup() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Clear All Data")
            .setMessage("This will clear ALL SWIM DATA from the database.")
            .setCancelable(false)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("OK") { dialog, _ ->
                // TODO: Check if db is working
                val db = com.thesisapp.data.AppDatabase.getInstance(this)

                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    db.swimDataDao().clearAll()

                    withContext(kotlinx.coroutines.Dispatchers.Main) {
                        Toast.makeText(this@SettingsActivity, "Swim data cleared", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.dismiss()
            }
            .show()
    }
}