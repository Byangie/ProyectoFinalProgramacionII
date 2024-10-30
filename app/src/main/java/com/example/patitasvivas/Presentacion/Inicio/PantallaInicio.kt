package com.example.patitasvivas.Presentacion.Inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patitasvivas.R

@Composable
fun Inicio(
    navigateToLogin: () -> Unit = {},
    navigateToSignUp: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF81D4FA), // Celeste pastel
                        Color(0xFF80E27E)  // Verde pastel
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(200.dp)
        )
        Spacer(modifier = Modifier.weight(0.5f)) // Menos espacio entre la imagen y el texto

        Text(
            text = "Salvando y dando",
            color = Color(0xFF001F3F), // Azul marino
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = "un mejor hogar",
            color = Color(0xFF001F3F), // Azul marino
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.5f)) // Menos espacio entre el texto y los botones

        // Botón Sign Up en verde fuerte
        Button(
            onClick = { navigateToSignUp() },
            modifier = Modifier
                .fillMaxWidth(0.8f) // Hacer el botón un poco más pequeño
                .height(48.dp) // Ajustar la altura
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde fuerte
        ) {
            Text(
                text = "Sign Up",
                color = Color.White, // Texto en blanco
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre los botones

        // Botón Log In en naranja
        Button(
            onClick = { navigateToLogin() },
            modifier = Modifier
                .fillMaxWidth(0.8f) // Hacer el botón de Log In también un poco más pequeño
                .height(48.dp) // Ajustar la altura
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)) // Naranja
        ) {
            Text(
                text = "Log In",
                color = Color.White, // Texto en blanco
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Espacio flexible al final
    }
}



