package com.thesisapp.presentation

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import com.thesisapp.utils.animateClick

class HistorySessionActivity : AppCompatActivity() {

    private lateinit var btnReturn: ImageButton
    private lateinit var txtDate: TextView
    private lateinit var txtDuration: TextView
    private lateinit var txtStart: TextView
    private lateinit var txtEnd: TextView
    private lateinit var txtDistance: TextView
    private lateinit var txtStrokeBack: TextView
    private lateinit var txtStrokeBreast: TextView
    private lateinit var txtStrokeFly: TextView
    private lateinit var txtStrokeFree: TextView
    private lateinit var txtPaceBack: TextView
    private lateinit var txtPaceBreast: TextView
    private lateinit var txtPaceFly: TextView
    private lateinit var txtPaceFree: TextView
    private lateinit var txtLap: TextView
    private lateinit var inputNotes: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_session)

        btnReturn = findViewById(R.id.btnReturn)
        txtDate = findViewById(R.id.txtDate)
        txtDuration = findViewById(R.id.txtDuration)
        txtStart = findViewById(R.id.txtStart)
        txtEnd = findViewById(R.id.txtEnd)
        txtDistance = findViewById(R.id.txtDistance)
        txtStrokeBack = findViewById(R.id.txtStrokeBack)
        txtStrokeBreast = findViewById(R.id.txtStrokeBreast)
        txtStrokeFly = findViewById(R.id.txtStrokeFly)
        txtStrokeFree = findViewById(R.id.txtStrokeFree)
        txtPaceBack = findViewById(R.id.txtPaceBack)
        txtPaceBreast = findViewById(R.id.txtPaceBreast)
        txtPaceFly = findViewById(R.id.txtPaceFly)
        txtPaceFree = findViewById(R.id.txtPaceFree)
        txtLap = findViewById(R.id.txtLap)
        inputNotes = findViewById(R.id.inputNotes)

        btnReturn.setOnClickListener {
            it.animateClick()
            finish()
        }

        val mlResultId = intent.getIntExtra("mlResultId", -1)
        if (mlResultId != -1) {
            loadMlResult(mlResultId)
        }
    }

    private fun loadMlResult(id: Int) {
        val db = AppDatabase.getInstance(this)

        Thread {
            val result = db.mlResultDao().getById(id)
            result?.let { mlResult ->
                runOnUiThread {
                    txtDate.text = mlResult.date
                    txtStart.text = mlResult.timeStart
                    txtEnd.text = mlResult.timeEnd
                    txtDistance.text = "${mlResult.distance} m"

                    // Calculate and set duration
                    txtDuration.text = "${calculateDuration(mlResult.timeStart, mlResult.timeEnd)}"

                    // Stroke percentages
                    txtStrokeBack.text = "${mlResult.backstroke}%"
                    txtStrokeBreast.text = "${mlResult.breaststroke}%"
                    txtStrokeFly.text = "${mlResult.butterfly}%"
                    txtStrokeFree.text = "${mlResult.freestyle}%"

                    // Pace in m/s (optional: convert to min/100m)
                    txtPaceBack.text = "${mlResult.paceBackstroke} m/s"
                    txtPaceBreast.text = "${mlResult.paceBreaststroke} m/s"
                    txtPaceFly.text = "${mlResult.paceButterfly} m/s"
                    txtPaceFree.text = "${mlResult.paceFreestyle} m/s"

                    txtLap.text = mlResult.lap.toString()
                    inputNotes.setText(mlResult.notes)
                }
            }
        }.start()
    }

    private fun calculateDuration(start: String, end: String): String {
        try {
            val format = java.text.SimpleDateFormat("HH:mm:ss")
            val startTime = format.parse(start)
            val endTime = format.parse(end)
            val diff = endTime.time - startTime.time

            val minutes = (diff / 60000).toInt()
            val seconds = (diff % 60000 / 1000).toInt()
            return String.format("%02d:%02d", minutes, seconds)
        } catch (e: Exception) {
            return "-"
        }
    }
}
