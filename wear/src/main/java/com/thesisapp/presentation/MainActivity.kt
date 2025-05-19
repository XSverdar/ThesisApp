package com.thesisapp.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.thesisapp.service.SensorService

class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSensorServiceAndFinish()
        } else {
            Toast.makeText(this, "Permission required for sensor access", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        checkPermissionAndStartService()
    }

    private fun checkPermissionAndStartService() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) ==
                    PackageManager.PERMISSION_GRANTED -> {
                startSensorServiceAndFinish()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.BODY_SENSORS) -> {
                Toast.makeText(this, "Sensor access is required to collect data", Toast.LENGTH_LONG).show()
                permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
            }
        }
    }

    private fun startSensorServiceAndFinish() {
        val intent = Intent(this, SensorService::class.java)
        ContextCompat.startForegroundService(this, intent)
        finish()
    }
}
