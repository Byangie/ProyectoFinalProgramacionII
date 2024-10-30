package com.example.patitasvivas.Presentacion.Signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var scroll = rememberScrollState() // Estado para el desplazamiento vertical.
    var email by remember { mutableStateOf("") } // Estado para almacenar el email.
    var contraseña by remember { mutableStateOf("") } // Estado para almacenar la contraseña.
    var experiencia by remember { mutableStateOf("") } // Estado para almacenar la experiencia del usuario.
    var interes by remember { mutableStateOf("") } // Estado para almacenar los intereses del usuario.
    var nombre by remember { mutableStateOf("") } // Estado para almacenar el nombre del usuario.
    var serviciosOfrecidos by remember { mutableStateOf("") } // Estado para almacenar los servicios ofrecidos.
    var serviciosInteres by remember { mutableStateOf("") } // Estado para almacenar los servicios de interés.
    var dialogTitle by remember { mutableStateOf("") } // Título del diálogo de alerta.
    var dialogText by remember { mutableStateOf("") } // Texto del diálogo de alerta.
    var show by rememberSaveable { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de alerta.
    var idUsuario by remember { mutableStateOf("") } // Estado para almacenar el ID del usuario.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row() {
            // Botón para volver a la pantalla anterior.
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "",
                tint = White,
                modifier = Modifier.padding(24.dp).clickable { navHostController.popBackStack() }
            )
            Spacer(Modifier.weight(1f)) // Espaciador para separar el icono del borde.
        }

        // TextField para el email.
        Text("Email", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        Spacer(Modifier.height(40.dp)) // Espaciador para separar campos.

        // TextField para la contraseña.
        Text("Contraseña", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        Spacer(Modifier.height(40.dp))

        // TextField para la experiencia.
        Text("Experiencia", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = experiencia,
            onValueChange = { experiencia = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        Spacer(Modifier.height(40.dp))

        // TextField para el interés.
        Text("Interés", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = interes,
            onValueChange = { interes = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        Spacer(Modifier.height(40.dp))

        // TextField para el nombre.
        Text("Nombre", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        Spacer(Modifier.height(40.dp))

        // TextField para los servicios ofrecidos.
        Text("Servicios Ofrecidos", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = serviciosOfrecidos,
            onValueChange = { serviciosOfrecidos = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        Spacer(Modifier.height(40.dp))

        // TextField para los servicios de interés.
        Text("Servicios Interés", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = serviciosInteres,
            onValueChange = { serviciosInteres = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)
        )

        // Lógica del botón para registrarse.
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = {
                // Verificar que la contraseña tenga al menos 8 caracteres.
                if (contraseña.length < 8) {
                    show = true
                    dialogTitle = "Alerta"
                    dialogText = "La contraseña debe tener al menos 8 caracteres"
                } else {
                    if (email.isNotEmpty() && contraseña.isNotEmpty()) { // Verificar que email y contraseña no estén vacíos.
                        auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener { task ->
                            if (task.isSuccessful) { // Si el registro es exitoso.
                                idUsuario = auth.currentUser?.uid.toString() // Obtener el ID del usuario.
                                if (experiencia.isNotEmpty() && serviciosInteres.isNotEmpty() && interes.isNotEmpty() && nombre.isNotEmpty() && serviciosOfrecidos.isNotEmpty()) {
                                    // Conectar a Firestore y crear el registro de usuario.
                                    val db = FirebaseFirestore.getInstance().collection("UsersProfile")
                                    val dat = DatosUsuario( // Crear la instancia de los datos del usuario.
                                        experiencia,
                                        interes,
                                        nombre, // Guardar el nombre en vez de "usuario desconocido".
                                        serviciosOfrecidos,
                                        serviciosInteres,
                                        email,
                                        idUsuario
                                    )
                                    db.document(idUsuario).set(dat) // Guardar el nuevo registro en Firestore utilizando el ID del usuario.
                                    // Mostrar mensaje de éxito.
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
                                } else {
                                    // Alertar si hay campos vacíos.
                                    show = true
                                    dialogTitle = "Alerta"
                                    dialogText = "Todos los campos son Obligatorios"
                                }
                            } else {
                                // Manejar errores durante el registro.
                                val exception = task.exception
                                when (exception) {
                                    is FirebaseAuthWeakPasswordException -> {
                                        show = true
                                        dialogTitle = "Alerta"
                                        dialogText = "La contraseña es demasiado débil"
                                    }
                                    is FirebaseAuthUserCollisionException -> {
                                        show = true
                                        dialogTitle = "Alerta"
                                        dialogText = "El correo ya está registrado"
                                    }
                                    else -> {
                                        show = true
                                        dialogTitle = "Alerta"
                                        dialogText = "Error al registrar: ${exception?.message}"
                                    }
                                }
                            }
                        }
                    } else {
                        // Alertar si hay campos vacíos.
                        show = true
                        dialogTitle = "Alerta"
                        dialogText = "Todos los campos son Obligatorios"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            Text("Sign Up") // Texto del botón.
        }

        Spacer(Modifier.height(40.dp)) // Espaciador final.

        // Diálogo de alerta.
        if (show) {
            AlertDialog(
                onDismissRequest = { show = false }, // Cerrar el diálogo al tocar fuera.
                title = { Text(dialogTitle, color = Black) },
                text = { Text(dialogText, color = Black) },
                confirmButton = {
                    Button(onClick = { show = false }) {
                        Text("Aceptar") // Texto del botón de aceptar.
                    }
                }
            )
        }
    }
}
