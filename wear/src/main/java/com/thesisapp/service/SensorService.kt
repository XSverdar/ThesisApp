package com.thesisapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import com.thesisapp.data.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SensorService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var heartRate: Sensor? = null
    private lateinit var database: AppDatabase
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())

        database = AppDatabase.getInstance(this)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        heartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        requestBatteryOptimizationExemption()
        registerSensors()
    }

    private fun registerSensors() {
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST, 100000) }
        gyroscope?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST, 100000) }
        heartRate?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST, 100000) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        serviceScope.launch {
            when (event.sensor.type) {
                Sensor.TYPE_LINEAR_ACCELERATION -> handleSensorData("Accelerometer", event.values)
                Sensor.TYPE_GYROSCOPE -> handleSensorData("Gyroscope", event.values)
                Sensor.TYPE_HEART_RATE -> handleHeartRate(event.values[0])
            }
        }
    }

    private suspend fun handleSensorData(type: String, values: FloatArray) {
        val (x, y, z) = values
        database.sensorDataDao().insertSensorData(SensorData(type = type, x = x, y = y, z = z))
        Log.d("SensorService", "$type: $x, $y, $z")
    }

    private suspend fun handleHeartRate(hr: Float) {
        database.sensorDataDao().insertSensorData(SensorData(type = "HeartRate", x = hr, y = null, z = null))
        Log.d("SensorService", "HeartRate: $hr")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Ensures service restarts if killed
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sensor_service",
                "Sensor Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "sensor_service")
            .setContentTitle("Swimming Data Collection")
            .setContentText("Sensors are running in the background")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    private fun requestBatteryOptimizationExemption() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                .setData(Uri.parse("package:$packageName"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
