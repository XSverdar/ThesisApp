import android.os.PowerManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.net.Uri

override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
    startForeground(1, createNotification())

    // Prevent OS from killing the app
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            .setData(Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    registerSensors() // Start sensors after requesting battery optimization
}
