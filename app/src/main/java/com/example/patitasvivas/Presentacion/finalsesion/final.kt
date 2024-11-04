
package com.example.patitasvivas.Presentacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patitasvivas.ui.theme.Black
// Cierre de Sesion app
@Composable
fun GraciasScreen(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Gracias por visitar Patitas Vivas",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onCerrarSesion() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Salir de la aplicación", color = Color.Black)
        }
    }
}
