package com.example.utilityapp.ui.theme.Screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utilityapp.R
import com.example.utilityapp.ui.theme.components.ToggleOptionItem
import com.example.utilityapp.utils.lockPhone
import com.example.utilityapp.utils.setFlashlight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShakeScreen() {

    // Detect Shake Gesture
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // States
    var isChecked_Flashlight by remember { mutableStateOf(false) }
    var isChecked_Lock by remember { mutableStateOf(false) }

    val sensorListener = remember {
        object : SensorEventListener {
            private var lastShakeTime = 0L

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    val gX = x / SensorManager.GRAVITY_EARTH
                    val gY = y / SensorManager.GRAVITY_EARTH
                    val gZ = z / SensorManager.GRAVITY_EARTH

                    val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

                    if (gForce > 2.5f) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastShakeTime > 1000) {
                            lastShakeTime = currentTime

                            if (isChecked_Flashlight) {
                                setFlashlight(context, true) // Turn on flashlight
                            }

                            if (isChecked_Lock) {
                                lockPhone(context) // Lock the phone
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    LaunchedEffect(Unit) {
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }



     // animation
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val scale_two by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

     // drawer

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            sheetState = sheetState
        ) {




            ToggleOptionItem(
                icon = Icons.Default.Search,
                title = "Toggle Flashlight",
                isChecked = isChecked_Flashlight,
                onCheckedChange = {
                     isChecked_Flashlight =  it
                    if (it) isChecked_Lock = false
                }
            )

            ToggleOptionItem(
                icon = Icons.Default.Lock,
                title = "Lock Mobile",
                isChecked = isChecked_Lock,
                onCheckedChange = { isChecked_Lock = it
                    if (it) isChecked_Flashlight = false
                }
            )



            Spacer(modifier = Modifier.height(20.dp))

        }
    }

    // main screen

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Set Actions" ,
            modifier = Modifier.padding(12.dp)  ,
            fontSize = 20.sp , fontWeight = FontWeight.Bold )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)

        ) {

            Box(
                modifier = Modifier
                    .size(200.dp * scale_two)
                    .background(
                        color = Color(0x8D828CFF),
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(150.dp * scale)
                    .background(
                        color = Color(0xD1828CFF),
                        shape = CircleShape
                    )
            )

            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = Color(0xFF6D79FF),
                        shape = CircleShape ,

                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mobile) , // Replace with your fire icon
                    contentDescription = "Calories",
                    modifier = Modifier.size(34.dp) ,
                )
            }
        }



        Button(
            onClick = {
                coroutineScope.launch {
                    sheetState.show()
                    showBottomSheet = true
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .width(160.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x8D828CFF),
                contentColor = Color(0xFF000000)
            )
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon" ,  Modifier.background(color = Color.White, shape = CircleShape))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Add Actions" , fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}


