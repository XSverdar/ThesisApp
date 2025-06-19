package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var btnProfile: LinearLayout
    private lateinit var btnExport: LinearLayout
    private lateinit var btnClearAllData: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        btnReturn = findViewById(R.id.btnReturn)
        btnProfile = findViewById(R.id.btnProfile)
        btnExport = findViewById(R.id.btnExport)
        btnClearAllData = findViewById(R.id.btnClearAllData)

        btnProfile.setOnClickListener {
            val intent = Intent(this, SettingsProfileActivity::class.java)
            startActivity(intent)
        }

        btnExport.setOnClickListener {
            val intent = Intent(this, SettingsExportActivity::class.java)
            startActivity(intent)
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
                val db = AppDatabase.getInstance(this)

                CoroutineScope(Dispatchers.IO).launch {
                    db.swimDataDao().clearAll()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SettingsActivity, "Swim data cleared", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.dismiss()
            }
            .show()
    }
}