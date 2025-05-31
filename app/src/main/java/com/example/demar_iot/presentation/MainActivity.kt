package com.example.demar_iot.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.wear.compose.material.*
import com.example.demar_iot.presentation.theme.Demar_iotTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.ui.text.style.TextAlign

enum class Screen {
    MainMenu,
    LocationScreen,
    GraphScreen,
    AlertsScreen,
    BoatScreen
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    var currentScreen by remember { mutableStateOf(Screen.MainMenu) }

    Demar_iotTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()

            when (currentScreen) {
                Screen.MainMenu -> MainMenuGrid(
                    onHomeClick = { currentScreen = Screen.LocationScreen },
                    onGraphClick = { currentScreen = Screen.GraphScreen },
                    onAlertsClick = { currentScreen = Screen.AlertsScreen },
                    onBoatClick = { currentScreen = Screen.BoatScreen }
                )
                Screen.LocationScreen -> LocationScreen(onBack = { currentScreen = Screen.MainMenu })
                Screen.GraphScreen -> GraphScreen(onBack = { currentScreen = Screen.MainMenu })
                Screen.AlertsScreen -> AlertsScreen(onBack = { currentScreen = Screen.MainMenu })
                Screen.BoatScreen -> BoatScreen(onBack = { currentScreen = Screen.MainMenu })
            }
        }
    }
}

// Clase auxiliar
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

// Modelo de datos
data class Notificacion(val titulo: String, val importante: Boolean, val fecha: String)

// Menú principal
@Composable
fun MainMenuGrid(
    onHomeClick: () -> Unit,
    onGraphClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onBoatClick: () -> Unit
) {
    val buttons = listOf(
        Quadruple(Icons.Rounded.LocationOn, "Location", onHomeClick, Color(0xFF1E90FF)),
        Quadruple(Icons.Rounded.AutoGraph, "Graph", onGraphClick, Color(0xFFFF4C4C)),
        Quadruple(Icons.Rounded.Info, "Alerts", onAlertsClick, Color(0xFFFFA500)),
        Quadruple(Icons.Rounded.DirectionsBoat, "Boat", onBoatClick, Color(0xFF32CD32))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttons.chunked(2).forEach { rowButtons ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowButtons.forEach { (icon, label, action, color) ->
                    Button(
                        onClick = action,
                        modifier = Modifier.size(70.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color
                        )
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            modifier = Modifier.size(47.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GraphScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        DonutChart(
            percentage = 0.75f,
            statusText = "Óptimo"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onBack) {
            Text("Volver", textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun DonutChart(
    percentage: Float,
    statusText: String
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20f
            val diameter = size.minDimension
            val inset = strokeWidth / 2
            val arcDim = Size(diameter - strokeWidth, diameter - strokeWidth)
            val arcRect = Rect(inset, inset, arcDim.width + inset, arcDim.height + inset)

            drawArc(
                color = Color.DarkGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = arcRect.topLeft,
                size = arcRect.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Arco de progreso
            drawArc(
                color = Color(0xFF0A3D62),
                startAngle = -90f,
                sweepAngle = percentage * 360f,
                useCenter = false,
                topLeft = arcRect.topLeft,
                size = arcRect.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Estado:",
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = statusText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

// Pantalla: Localización
@Composable
fun LocationScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Flecha hacia arriba",
                tint = Color.White,
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(99))
                    .padding(24.dp)
                    .fillMaxWidth(0.4f)
                    .aspectRatio(1f)
            )
            Text(
                text = "Localizando prototipo...",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Pantalla: Alertas
@Composable
fun AlertsScreen(onBack: () -> Unit) {
    val notificaciones = listOf(
        Notificacion("Batería baja", true, "30/05/2025 08:12 AM"),
        Notificacion("Se detectó una alta concentración de plomo", false, "30/05/2025 09:01 AM"),
        Notificacion("Se detectaron niveles altos de mercurio en la zona 3", true, "30/05/2025 09:55 AM"),
        Notificacion("El dispositivo se encendió", false, "30/05/2025 10:02 AM")
    )

    val AlertaImportante = Color(0xFFD32F2F)
    val AlertaNormal = Color(0xFF1976D2)

    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Historial de alertas",
                color = Color.White,
                style = MaterialTheme.typography.title2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        items(notificaciones) { noti ->
            Chip(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack,
                colors = ChipDefaults.primaryChipColors(
                    backgroundColor = if (noti.importante) AlertaImportante else AlertaNormal
                ),
                shape = RoundedCornerShape(99),
                label = {
                    Column {
                        Text(
                            text = noti.titulo,
                            color = Color.White,
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            text = noti.fecha,
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.caption2
                        )
                    }
                }
            )
        }
    }
}

// Pantalla: Prototipo
@Composable
fun BoatScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Información general",
            color = Color.White,
            fontSize = 15.sp,
            style = MaterialTheme.typography.title2,
            modifier = Modifier.padding(bottom = 5.dp, top = 40.dp)
        )

        Text(
            text = "Estatus del prototipo",
            color = Color.LightGray,
            fontSize = 12.sp,
            style = MaterialTheme.typography.caption1,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatusCircle(
                icon = Icons.Rounded.BatteryFull,
                label = "95%",
                backgroundColor = Color(0xFF32CD32)
            )
            StatusCircle(
                icon = Icons.Rounded.PowerSettingsNew,
                label = "Encendido",
                backgroundColor = Color(0xFF009CEA)
            )
            StatusCircle(
                icon = Icons.Rounded.Wifi,
                label = "10ms",
                backgroundColor = Color(0xFF7D32CD)
            )
        }
    }
}

// Icono con texto debajo
@Composable
fun StatusCircle(icon: ImageVector, label: String, backgroundColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = backgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.LightGray,
            style = MaterialTheme.typography.caption2,
            textAlign = TextAlign.Center
        )
    }
}

// Vista previa
@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}