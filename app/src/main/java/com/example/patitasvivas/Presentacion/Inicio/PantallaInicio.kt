package com.example.patitasvivas.Presentacion.Inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patitasvivas.R
import com.example.patitasvivas.ui.theme.Black
import com.example.patitasvivas.ui.theme.Gray
import com.example.patitasvivas.ui.theme.Green
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

@Composable
fun Inicio(navigateToLogin: () -> Unit = {}, navigateToSignUp: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Gray, Black))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "", modifier = Modifier.clip(CircleShape).size(200.dp))
        Spacer(modifier = Modifier.weight(1f))
        Text("Salvando y dando un mejor", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text("hogar", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { navigateToSignUp() }, modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 32.dp), colors = ButtonDefaults.buttonColors(containerColor = Green)) {
            Text(text = "Sign Up", color = Black, fontWeight = FontWeight.Bold)
        }
        Text(text = "Log In", color = Color.White, modifier = Modifier.padding(48.dp).clickable { navigateToLogin() })
        Spacer(modifier = Modifier.weight(1f))
    }
}
