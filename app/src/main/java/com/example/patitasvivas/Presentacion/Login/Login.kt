package com.example.patitasvivas.Presentacion.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.patitasvivas.R
import com.example.patitasvivas.ui.theme.Black
import com.example.patitasvivas.ui.theme.SelectedField
import com.example.patitasvivas.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(auth: FirebaseAuth, navHostController: NavHostController, navigateToMenu: () -> Unit = {}) {
     var email by remember { mutableStateOf("") }
     var contraseña by remember { mutableStateOf("") }
     var showDialog by remember { mutableStateOf(false) }
     var dialogMessage by remember { mutableStateOf("") }

     Column(modifier = Modifier.fillMaxSize().background(Black), horizontalAlignment = Alignment.CenterHorizontally) {
          Row {
               Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = White,
                    modifier = Modifier.padding(24.dp).clickable { navHostController.popBackStack() }
               )
               Spacer(Modifier.weight(1f))
          }

          Text("Email", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
          TextField(
               value = email,
               onValueChange = { email = it },
               modifier = Modifier.fillMaxWidth(),
               colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = UnselectedField,
                    focusedContainerColor = SelectedField
               )
          )

          Spacer(Modifier.height(40.dp))
          Text("Contraseña", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)

          TextField(
               value = contraseña,
               onValueChange = { contraseña = it },
               modifier = Modifier.fillMaxWidth(),
               visualTransformation = PasswordVisualTransformation(), // Para ocultar la contraseña
               colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = UnselectedField,
                    focusedContainerColor = SelectedField
               )
          )

          Spacer(Modifier.height(40.dp))
          Button(onClick = {
               if (email.isNotEmpty() && contraseña.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                              // Sign-in successful
                              navigateToMenu()
                         } else {
                              // Error en el inicio de sesión
                              dialogMessage = "Correo o contraseña incorrectos"
                              showDialog = true // Mostrar el diálogo
                         }
                    }
               } else {
                    dialogMessage = "Por favor, ingrese su correo y contraseña"
                    showDialog = true // Mostrar el diálogo
               }
          }) {
               Text("Login")
          }
     }

     // AlertDialog para mostrar mensajes de error
     if (showDialog) {
          AlertDialog(
               onDismissRequest = { showDialog = false }, // Cerrar el diálogo
               title = { Text("Error de inicio de sesión") },
               text = { Text(dialogMessage) },
               confirmButton = {
                    Button(onClick = { showDialog = false }) {
                         Text("Aceptar")
                    }
               }
          )
     }
}
