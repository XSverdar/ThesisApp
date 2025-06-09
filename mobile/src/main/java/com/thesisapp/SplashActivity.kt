package com.thesisapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 3 seconds, then move to MainDashboardActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainDashboardActivity::class.java)
            startActivity(intent)
            finish() // Prevent user from returning to splash
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}