package com.example.utilityapp.ui.theme.Screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utilityapp.R
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun StepScreen() {
    val context = LocalContext.current
    var steps by remember { mutableStateOf(1f) }
    var lastShakeTime by remember { mutableStateOf(0L) }
    val shakeThreshold = 3.5f // Adjusted threshold
    val shakeTimeGap = 1500L // Adjusted time gap

    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val sensorEventListener = remember {
        object : SensorEventListener {
            private val alpha = 0.8f
            private var gravity = FloatArray(3)
            private var linearAcceleration = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        // Apply low-pass filter to isolate the force of gravity
                        gravity[0] = alpha * gravity[0] + (1 - alpha) * it.values[0]
                        gravity[1] = alpha * gravity[1] + (1 - alpha) * it.values[1]
                        gravity[2] = alpha * gravity[2] + (1 - alpha) * it.values[2]

                        // Remove the gravity contribution with the high-pass filter
                        linearAcceleration[0] = it.values[0] - gravity[0]
                        linearAcceleration[1] = it.values[1] - gravity[1]
                        linearAcceleration[2] = it.values[2] - gravity[2]

                        val gForce = sqrt(
                            linearAcceleration[0] * linearAcceleration[0] +
                            linearAcceleration[1] * linearAcceleration[1] +
                            linearAcceleration[2] * linearAcceleration[2]
                        )

                        if (gForce > shakeThreshold) {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastShakeTime > shakeTimeGap) {
                                lastShakeTime = currentTime
                                steps++
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        accelerometerSensor?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)

        }
        onDispose {

            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Today's Activity",
            modifier = Modifier.padding(12.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .background(Color(0x8D828CFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.feets),
                    contentDescription = "Footsteps",
                    modifier = Modifier.size(100.dp),
                    tint = Color.Black
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.fire),
                    contentDescription = "Calories",
                    tint = Color(0xFFF4511E),
                    modifier = Modifier.size(40.dp)
                )

                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = "CALORIES", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    val caloriesBurned = (steps * 0.04).roundToInt()
                    Text(text = "$caloriesBurned kcal", fontSize = 16.sp, fontWeight = FontWeight.Normal)
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.step),
                    contentDescription = "Steps",
                    modifier = Modifier.size(34.dp),
                    tint = Color(0xFF43A047)
                )

                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = "WALK", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${steps.roundToInt()} steps", fontSize = 16.sp, fontWeight = FontWeight.Normal)
                }
            }
        }
    }
}