package com.thesisapp.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class SettingsExportActivity : AppCompatActivity() {

    private lateinit var btnFromDate: Button
    private lateinit var btnToDate: Button
    private lateinit var btnExport: Button
    private var fromDate: Long = 0L
    private var toDate: Long = 0L

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_export)

        btnFromDate = findViewById(R.id.fromDate)
        btnToDate = findViewById(R.id.toDate)
        btnExport = findViewById(R.id.btnExport)
        val btnReturn = findViewById<Button>(R.id.btnReturn)

        btnFromDate.setOnClickListener {
            showDatePicker { timestamp ->
                fromDate = timestamp
                btnFromDate.text = dateFormat.format(Date(fromDate))
            }
        }

        btnToDate.setOnClickListener {
            showDatePicker { timestamp ->
                toDate = timestamp
                btnToDate.text = dateFormat.format(Date(toDate))
            }
        }

        btnExport.setOnClickListener {
            if (fromDate == 0L || toDate == 0L) {
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show()
            } else if (fromDate > toDate) {
                Toast.makeText(this, "From date must be before To date", Toast.LENGTH_SHORT).show()
            } else {
                exportSwimDataToCSV()
            }
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker(onDateSelected: (Long) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            calendar.set(y, m, d, 0, 0, 0)
            onDateSelected(calendar.timeInMillis)
        }, year, month, day).show()
    }

    // TODO: Check if db is working
    private fun exportSwimDataToCSV() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(applicationContext)
            val swimDataList = db.swimDataDao().getSwimsBetweenDates(fromDate, toDate)

            if (swimDataList.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(this@SettingsExportActivity, "No swim data found", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val fileName = "swim_data_export_${System.currentTimeMillis()}.csv"
            val file = File(getExternalFilesDir(null), fileName)

            FileWriter(file).use { writer ->
                writer.appendLine("timestamp,accel_x,accel_y,accel_z,gyro_x,gyro_y,gyro_z,heart_rate,ppg,ecg")

                for (data in swimDataList) {
                    writer.appendLine("${data.timestamp},${data.accel_x},${data.accel_y},${data.accel_z}," +
                            "${data.gyro_x},${data.gyro_y},${data.gyro_z}," +
                            "${data.heart_rate},${data.ppg},${data.ecg}")
                }
            }

            runOnUiThread {
                Toast.makeText(this@SettingsExportActivity, "Data exported to ${file.name}", Toast.LENGTH_LONG).show()
            }
        }
    }
}