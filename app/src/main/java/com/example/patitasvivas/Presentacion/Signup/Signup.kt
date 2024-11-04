package com.example.patitasvivas.Presentacion.Signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.patitasvivas.Modelo.DatosUsuario.DatosUsuario
import com.example.patitasvivas.R
import com.example.patitasvivas.mensaje
import com.example.patitasvivas.ui.theme.Black
import com.example.patitasvivas.ui.theme.Green
import com.example.patitasvivas.ui.theme.SelectedField
import com.example.patitasvivas.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Signup(auth: FirebaseAuth, navHostController: NavHostController) {
    var scroll = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var interes by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var serviciosOfrecidos by remember { mutableStateOf("") }
    var serviciosInteres by remember { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }
    var show by rememberSaveable { mutableStateOf(false) }
    var idUsuario by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))))
            .verticalScroll(scroll)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Regresar",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { navHostController.popBackStack() }
                )
                Spacer(Modifier.weight(1f))
            }

            Text(
                text = "Registro",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // TextField para el email.
            CustomTextField("Email", email) { email = it }
            CustomTextField("Contraseña", contraseña) { contraseña = it }
            CustomTextField("Experiencia", experiencia) { experiencia = it }
            CustomTextField("Interés", interes) { interes = it }
            CustomTextField("Nombre", nombre) { nombre = it }
            CustomTextField("Servicios Ofrecidos", serviciosOfrecidos) { serviciosOfrecidos = it }
            CustomTextField("Servicios Interés", serviciosInteres) { serviciosInteres = it }

            Spacer(Modifier.height(32.dp))

            // Botón para registrarse.
            Button(
                onClick = {
                    // Verificar que la contraseña tenga al menos 8 caracteres.
                    if (contraseña.length < 8) {
                        show = true
                        dialogTitle = "Alerta"
                        dialogText = "La contraseña debe tener al menos 8 caracteres"
                    } else if (email.isNotEmpty() && contraseña.isNotEmpty()) {
                        auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                idUsuario = auth.currentUser?.uid.toString()
                                if (experiencia.isNotEmpty() && serviciosInteres.isNotEmpty() && interes.isNotEmpty() && nombre.isNotEmpty() && serviciosOfrecidos.isNotEmpty()) {
                                    val db = FirebaseFirestore.getInstance().collection("UsersProfile")
                                    val dat = DatosUsuario(
                                        nombre,
                                        email,
                                        experiencia,
                                        interes,
                                        serviciosInteres,
                                        serviciosOfrecidos,
                                        idUsuario
                                    )
                                    db.document(idUsuario).set(dat)
                                    show = true
                                    dialogTitle = "Alerta"
                                    dialogText = "Registro Exitoso"
                                    // Limpiar campos después del registro exitoso.
                                    experiencia = ""
                                    interes = ""
                                    nombre = ""
                                    serviciosOfrecidos = ""
                                    serviciosInteres = ""
                                    email = ""
                                    contraseña=""
                                } else {
                                    show = true
                                    dialogTitle = "Alerta"
                                    dialogText = "Todos los campos son Obligatorios"
                                }
                            } else {
                                handleRegistrationError(task.exception, { newShow, newDialogTitle, newDialogText ->
                                    show = newShow
                                    dialogTitle = newDialogTitle
                                    dialogText = newDialogText
                                })
                            }
                        }
                    } else {
                        show = true
                        dialogTitle = "Alerta"
                        dialogText = "Todos los campos son Obligatorios"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Registrarse", color = Color.White)
            }

            // Diálogo de alerta.
            if (show) {
                AlertDialog(
                    onDismissRequest = { show = false },
                    title = { Text(dialogTitle, color = Color.Black) },
                    text = { Text(dialogText, color = Color.Black) },
                    confirmButton = {
                        Button(onClick = { show = false }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF2E2E2E),
                focusedContainerColor = Color(0xFF3B3B3B),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(text = "Introduce $label", color = Color.Gray)
            }
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos
    }
}

private fun handleRegistrationError(exception: Exception?, updateDialogState: (Boolean, String, String) -> Unit) {
    // Manejar errores durante el registro.
    when (exception) {
        is FirebaseAuthWeakPasswordException -> {
            updateDialogState(true, "Alerta", "La contraseña es demasiado débil")
        }
        is FirebaseAuthUserCollisionException -> {
            updateDialogState(true, "Alerta", "El correo ya está registrado")
        }
        else -> {
            updateDialogState(true, "Alerta", "Error al registrar: ${exception?.message}")
        }
    }
}