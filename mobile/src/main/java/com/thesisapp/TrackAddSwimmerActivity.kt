package com.thesisapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TrackAddSwimmerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_add_swimmer)

        val inputSwimmerName = findViewById<EditText>(R.id.inputSwimmerName)
        val inputSwimmerAge = findViewById<EditText>(R.id.inputSwimmerAge)
        val inputSwimmerCategory = findViewById<EditText>(R.id.inputSwimmerCategory)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnReturn = findViewById<ImageButton>(R.id.btnReturn)

        val db = DatabaseHelper(this)

        btnNext.setOnClickListener {
            val name = inputSwimmerName.text.toString()
            val age = inputSwimmerAge.text.toString()
            val category = inputSwimmerCategory.text.toString()

            if (name.isEmpty() ||
                age.isEmpty() ||
                category.isEmpty()
            ) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            } else if (db.swimmerExists(name)) {
                Toast.makeText(this, "Swimmer already exists.", Toast.LENGTH_SHORT).show()
            } else {
                if (db.addSwimmer(name, age.toInt(), category)) {
                    val intent = Intent(this, TrackSwimmerSuccessActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to add.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnReturn.setOnClickListener {
            finish()
        }
    }
}