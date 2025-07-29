package com.thesisapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.thesisapp.R
import com.thesisapp.communication.PhoneReceiver
import com.thesisapp.communication.PhoneSender
import com.thesisapp.data.AppDatabase
import com.thesisapp.data.MlResult
import com.thesisapp.data.SwimData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class TrackSwimmerActivity : AppCompatActivity() {
    private lateinit var receiver: PhoneReceiver
    private lateinit var sender: PhoneSender
    private lateinit var classifier: StrokeClassifier
    private lateinit var db: AppDatabase
    private val liveSensorData = MutableStateFlow<SwimData?>(null)

    init {
        try {
            System.loadLibrary("filament-jni")
            System.loadLibrary("gltfio-jni")
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        receiver = PhoneReceiver(this, liveSensorData)
        sender = PhoneSender(this)
        classifier = StrokeClassifier(this)
        db = AppDatabase.getInstance(applicationContext)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.track_swimmer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        receiver.register()

        lifecycleScope.launch(Dispatchers.IO) {
            val maxSessionId = db.mlResultDao().getMaxSessionId() ?: 0
            val newSessionId = maxSessionId + 1

            runOnUiThread {
                setContent {
                    RealtimeSensorScreen(
                        sensorDataFlow = liveSensorData,
                        phoneSender = sender,
                        predictedLabel = receiver.predictedLabel,
                        db = db,
                        sessionId = newSessionId
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.unregister()
    }
}

fun Float?.format(decimals: Int = 2): String {
    return if (this == null) "--" else String.format(Locale.US, "%.${decimals}f", this)
}

@Composable
fun RealtimeSensorScreen(
    sensorDataFlow: Flow<SwimData?>,
    phoneSender: PhoneSender,
    predictedLabel: State<String>,
    db: AppDatabase,
    sessionId: Int
) {
    val sensorData by sensorDataFlow.collectAsState(initial = null)
    var isRecording by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(sensorData) {
        sensorData?.let { data ->
            if (isRecording) {
                val swim = SwimData(
                    sessionId = sessionId,
                    timestamp = System.currentTimeMillis(),
                    accel_x = data.accel_x,
                    accel_y = data.accel_y,
                    accel_z = data.accel_z,
                    gyro_x = data.gyro_x,
                    gyro_y = data.gyro_y,
                    gyro_z = data.gyro_z,
                    heart_rate = data.heart_rate,
                    ppg = data.ppg,
                    ecg = data.ecg
                )

                (context as? AppCompatActivity)?.lifecycleScope?.launch {
                    db.swimDataDao().insert(swim)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ReturnButton()

            Spacer(modifier = Modifier.height(12.dp))

            Text("LATEST SENSOR DATA", color = Color.White)

            sensorData?.let { data ->
                HandViewerComposable(
                    gyroX = data.gyro_x ?: 0f,
                    gyroY = data.gyro_y ?: 0f,
                    gyroZ = data.gyro_z ?: 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                StrokePrediction(predictedLabel)

                Text(
                    "Accel: x=${data.accel_x.format()}, y=${data.accel_y.format()}, z=${data.accel_z.format()}",
                    color = Color.White
                )
                Text(
                    "Gyro: x=${data.gyro_x.format()}, y=${data.gyro_y.format()}, z=${data.gyro_z.format()}",
                    color = Color.White
                )
                Text(
                    "HR: ${data.heart_rate.format()}, PPG: ${data.ppg.format()}, ECG: ${data.ecg.format()}",
                    color = Color.White
                )
            } ?: Text("Start recording to see swimmer data.", color = Color.White)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val target = !isRecording
                phoneSender.sendCommand(
                    start = target,
                    onSuccess = {
                        isRecording = target

                        if (!target) { // Recording stopped → save MLResult
                            (context as? AppCompatActivity)?.lifecycleScope?.launch {
                                val swimDataList = db.swimDataDao().getSwimDataForSession(sessionId)

                                if (swimDataList.isNotEmpty()) {
                                    val formatterDate = java.text.SimpleDateFormat(
                                        "MMMM dd, yyyy",
                                        Locale.getDefault()
                                    )
                                    val formatterTime =
                                        java.text.SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                                    val firstTime = java.util.Date(swimDataList.first().timestamp)
                                    val lastTime = java.util.Date(swimDataList.last().timestamp)

                                    val timeStart = formatterTime.format(firstTime)
                                    val timeEnd = formatterTime.format(lastTime)
                                    val date = formatterDate.format(firstTime)

                                    // Assign 0f to stroke percentages since no label info available
                                    val mlResult = MlResult(
                                        sessionId = sessionId,
                                        date = date,
                                        timeStart = timeStart,
                                        timeEnd = timeEnd,
                                        backstroke = 0f,
                                        breaststroke = 0f,
                                        butterfly = 0f,
                                        freestyle = 0f,
                                        notes = "[Editable Text Field]"
                                    )

                                    db.mlResultDao().insert(mlResult)
                                }
                            }
                        }
                    },
                    onFailure = {
                        Toast.makeText(
                            context,
                            "Watch not listening. Please open the app on your watch.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }) {
                Text(if (isRecording) "Stop Recording" else "Start Recording")
            }
        }
    }
}

@Composable
fun HandViewerComposable(
    gyroX: Float,
    gyroY: Float,
    gyroZ: Float,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context -> HandModelView(context) },
        modifier = modifier,
        update = { view -> view.setRotation(gyroX, gyroY, gyroZ) }
    )
}

@Composable
fun StrokePrediction(predictedLabel: State<String>) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Detected Stroke: ",
                fontSize = 16.sp,
                color = Color.White,
            )
            Text(
                text = predictedLabel.value,
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ReturnButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Button(
        onClick = {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        },
        modifier = modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        Text("Return")
    }
}