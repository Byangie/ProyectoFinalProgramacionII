package com.example.patitasvivas.Presentacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import coil.compose.rememberImagePainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale

@Composable
fun PantallaInicio(navHostController: NavHostController) {
    val backgroundColor = Color.Black // Cambiar a fondo negro
    val primaryColor = Color(0xFF6DBE45) // Verde fresco
    val textColor = Color.White // Color de texto blanco

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Permite el desplazamiento si es necesario
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cargar imagen desde URL
        Image(
            painter = rememberImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZyPcpvY03xjkmrHNzBgWfqkPt0cS4riCb8g&s"), // Cambia a la URL de tu imagen
            contentDescription = "Logo Patitas Vivas",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Crop // Escala la imagen
        )

        // Título
        Text(
            text = "¡Bienvenido a Patitas Vivas!",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor // Mantener en color verde
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtítulo
        Text(
            text = "Donde cada pata cuenta una historia",
            style = TextStyle(
                fontSize = 20.sp,
                color = textColor // Cambiar a color blanco
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

    }
}

