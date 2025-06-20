package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R
import com.thesisapp.utils.animateClick

class TrackSwimmerActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_swimmer)

        btnReturn = findViewById(R.id.btnReturn)

        btnReturn.setOnClickListener {
            it.animateClick()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // TODO: Implement actual tracking
    }
}