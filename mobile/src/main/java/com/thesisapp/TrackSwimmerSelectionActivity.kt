package com.thesisapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity

class TrackSwimmerSelectionActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnAddSwimmer: Button
    private lateinit var btnContinue: Button
    private lateinit var btnReturn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_swimmer_selection)

        db = DatabaseHelper(this)

        radioGroup = findViewById(R.id.swimmerRadioGroup)
        btnAddSwimmer = findViewById(R.id.btnAddSwimmer)
        btnContinue = findViewById(R.id.btnContinue)
        btnReturn = findViewById(R.id.btnReturn)

        // Clear hardcoded radio buttons first (since you defined them in your XML).
        radioGroup.removeAllViews()

        // Retrieve swimmers from database
        val swimmers = db.getAllSwimmers()
        for (s in swimmers) {
            val radio = RadioButton(this).apply {
                text = s
                setTextSize(18f)
                setPadding(20, 20, 20, 20)
                setTextColor(resources.getColor(R.color.text, theme))
                // You can apply a custom style if you have one:
                // setBackgroundResource(R.drawable/radio_background)
            }
            radioGroup.addView(radio)
        }

        btnAddSwimmer.setOnClickListener {
            startActivity(Intent(this, TrackAddSwimmerActivity::class.java))
        }

        btnContinue.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a swimmer first.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadio = findViewById<RadioButton>(selectedId)
                val selectedSwimmer = selectedRadio.text.toString()

                Toast.makeText(this, "Swimmer $selectedSwimmer selected.", Toast.LENGTH_SHORT).show()

                // Handle proceeding to next activity with this swimmer
                val intent = Intent(this, TrackSwimmerActivity::class.java)
                intent.putExtra("selectedSwimmer", selectedSwimmer)
                startActivity(intent)
            }
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }
}