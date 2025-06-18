package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import com.thesisapp.data.Swimmer

class TrackAddSwimmerActivity : AppCompatActivity() {

    private lateinit var inputName: EditText
    private lateinit var inputAge: EditText
    private lateinit var inputCategory: EditText
    private lateinit var inputWingspan: EditText
    private lateinit var btnNext: Button
    private lateinit var btnReturn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_add_swimmer)

        inputName = findViewById(R.id.inputSwimmerName)
        inputAge = findViewById(R.id.inputSwimmerAge)
        inputCategory = findViewById(R.id.inputSwimmerCategory)
        inputWingspan = findViewById(R.id.inputSwimmerWing)
        btnNext = findViewById(R.id.btnNext)
        btnReturn = findViewById(R.id.btnReturn)

        val editTexts = listOf(inputName, inputAge, inputCategory, inputWingspan)
        editTexts.forEach { editText ->
            editText.setTextColor(resources.getColor(R.color.black, theme))
            editText.setTypeface(editText.typeface, android.graphics.Typeface.BOLD)
        }

        val db = AppDatabase.getInstance(this)

        btnNext.setOnClickListener {
            val name = inputName.text.toString().trim()
            val age = inputAge.text.toString().trim()
            val category = inputCategory.text.toString().trim()
            val wingspan = inputWingspan.text.toString().trim()

            if (name.isEmpty() || age.isEmpty() || category.isEmpty() || wingspan.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val swimmer = Swimmer(
                name = name,
                age = age.toInt(),
                wingspan = wingspan.toFloat(),
                category = category
            )

            Thread {
                db.swimmerDao().insertSwimmer(swimmer)
                runOnUiThread {
                    Toast.makeText(this, "Swimmer saved!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, TrackSwimmerSuccessActivity::class.java))
                }
            }.start()
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }
}