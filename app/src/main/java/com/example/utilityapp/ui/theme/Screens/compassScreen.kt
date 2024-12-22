package com.example.utilityapp.ui.theme.Screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utilityapp.R
import kotlin.math.roundToInt

@Composable
fun CompassScreen()  {

    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var azimuth by remember { mutableStateOf(0f) }


    val sensorEventListener = remember {
        object : SensorEventListener {
            private val gravity = FloatArray(3)
            private val geomagnetic = FloatArray(3)
            private val rotationMatrix = FloatArray(9)
            private val orientationAngles = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        Sensor.TYPE_ACCELEROMETER -> {
                            System.arraycopy(it.values, 0, gravity, 0, it.values.size)
                        }
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            System.arraycopy(it.values, 0, geomagnetic, 0, it.values.size)
                        }
                    }

                    if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                        SensorManager.getOrientation(rotationMatrix, orientationAngles)
                        azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                        if (azimuth < 0) azimuth += 360

                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Register and unregister the sensor listener
    DisposableEffect(Unit) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
        sensorManager.registerListener(
            sensorEventListener,
            magnetometer,
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
        , verticalArrangement = Arrangement.Center
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Compass Screen" ,
            modifier = Modifier.padding(12.dp)  ,
            fontSize = 20.sp , fontWeight = FontWeight.Bold )

        Box(
            modifier = Modifier
                .weight(1f) ,
            contentAlignment = Alignment.TopCenter ,


        ) {


            Image(
                painter = painterResource(id = R.drawable.polygon_1),
                contentDescription = "Compass Image",
                modifier = Modifier.padding(top = 70.dp)

            )

            Image(
                painter = painterResource(id = R.drawable.base),
                contentDescription = "Compass Image",
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(-azimuth)
                    .size(430.dp , 430.dp)
                    .padding(bottom = 18.dp)
            )



        }

        Text(text = "${azimuth.roundToInt()}°" ,  modifier = Modifier.padding(bottom = 40.dp)  ,  fontSize = 32.sp , fontWeight = FontWeight.ExtraBold )


    }
}
