package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import com.thesisapp.data.Swimmer
import com.thesisapp.data.SwimmerDao
import kotlinx.coroutines.launch

class TrackAddSwimmerActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var dao: SwimmerDao

    private lateinit var inputSwimmerName: EditText
    private lateinit var inputSwimmerAge: EditText
    private lateinit var inputSwimmerCategory: EditText
    private lateinit var btnSave: Button
    private lateinit var btnReturn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_add_swimmer)

        inputSwimmerName = findViewById(R.id.inputSwimmerName)
        inputSwimmerAge = findViewById(R.id.inputSwimmerAge)
        inputSwimmerCategory = findViewById(R.id.inputSwimmerCategory)
        btnSave = findViewById(R.id.btnNext)
        btnReturn = findViewById(R.id.btnReturn)

        db = AppDatabase.getDatabase(this)
        dao = db.swimmerDao()

        btnSave.setOnClickListener {
            val name = inputSwimmerName.text.toString()
            val age = inputSwimmerAge.text.toString().toIntOrNull()
            val cat = inputSwimmerCategory.text.toString()

            if (name.isNotEmpty() && age != null && cat.isNotEmpty()) {

                val swimmer = Swimmer(name = name, age = age, category = cat)

                lifecycleScope.launch {
                    try {
                        dao.insertSwimmer(swimmer)
                        startActivity(Intent(this@TrackAddSwimmerActivity, TrackSwimmerSuccessActivity::class.java))
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@TrackAddSwimmerActivity, "Failed to add swimmer: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }
}