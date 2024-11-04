package com.example.patitasvivas.Presentacion.Login

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.patitasvivas.R
import com.example.patitasvivas.ui.theme.Black
import com.example.patitasvivas.ui.theme.Green
import com.example.patitasvivas.ui.theme.SelectedField
import com.example.patitasvivas.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth
@Composable
fun Login(auth: FirebaseAuth, navHostController: NavHostController, navigateToMenu: () -> Unit = {}) {
     var email by remember { mutableStateOf("") }
     var contraseña by remember { mutableStateOf("") }
     var showDialog by remember { mutableStateOf(false) }
     var dialogMessage by remember { mutableStateOf("") }

     // Fondo con un gradiente
     Box(
          modifier = Modifier
               .fillMaxSize()
               .background(
                    Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF5BBF7A)) // Gradiente azul y verde
               ))
     ) {
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp), // Padding alrededor
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center // Centrar los elementos verticalmente
          ) {
               Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                         painter = painterResource(id = R.drawable.back),
                         contentDescription = "Volver",
                         tint = Color.White,
                         modifier = Modifier
                              .padding(8.dp)
                              .clickable { navHostController.popBackStack() }
                    )
                    Spacer(Modifier.weight(1f)) // Espaciador para empujar el icono a la izquierda
               }

               Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    modifier = Modifier.padding(bottom = 32.dp) // Espaciado inferior
               )

               // Campo de texto para el email
               TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(vertical = 8.dp), // Espaciado vertical
                    colors = TextFieldDefaults.colors(
                         unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                         focusedContainerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = MaterialTheme.shapes.medium // Bordes redondeados
               )

               // Campo de texto para la contraseña
               TextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña", color = Color.White.copy(alpha = 0.7f)) },
                    visualTransformation = PasswordVisualTransformation(), // Para ocultar la contraseña
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                         unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                         focusedContainerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = MaterialTheme.shapes.medium // Bordes redondeados
               )

               Spacer(modifier = Modifier.height(24.dp)) // Espacio entre campos y botón

               // Botón de inicio de sesión
               Button(
                    onClick = {
                         if (email.isNotEmpty() && contraseña.isNotEmpty()) {
                              auth.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener { task ->
                                   if (task.isSuccessful) {
                                        navigateToMenu()
                                   } else {
                                        dialogMessage = "Correo o contraseña incorrectos"
                                        showDialog = true
                                   }
                              }
                         } else {
                              dialogMessage = "Por favor, ingrese su correo y contraseña"
                              showDialog = true
                         }
                    },
                    modifier = Modifier
                         .fillMaxWidth()
                         .height(56.dp) // Altura del botón
                         .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                         containerColor = Color(0xFF5BBF7A), // Color verde para el botón
                         contentColor = Color.White // Texto blanco en el botón
                    ),
                    shape = MaterialTheme.shapes.medium // Bordes redondeados
               ) {
                    Text("Iniciar Sesión")
               }

               Spacer(modifier = Modifier.height(16.dp)) // Espacio para el botón de registro

               // Opción de registro
               TextButton(onClick = {
                    navHostController.navigate("Signup")
               }) {
                    Text("¿No tienes cuenta? Regístrate aquí", color = Color(0xFF4A90E2)) // Color azul
               }
          }

          // Diálogo de error
          if (showDialog) {
               AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Error de inicio de sesión", color = Color.Black) },
                    text = { Text(dialogMessage, color = Color.Black) },
                    confirmButton = {
                         Button(onClick = { showDialog = false }) {
                              Text("Aceptar")
                         }
                    }
               )
          }
     }
}
