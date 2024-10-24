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
// fun es una funcion
//funcion que se encarga de crear Signup de la aplicacion
fun Signup(auth: FirebaseAuth,navHostController: NavHostController) {
    var scroll = rememberScrollState() // Crea un estado para el desplazamiento vertical.
    var email by remember { mutableStateOf("") } // Estado para almacenar el email ingresado.
    var contraseña by remember { mutableStateOf("") } // Estado para almacenar la contraseña ingresada.
    var Experiencia by remember { mutableStateOf("") } // Estado para almacenar la experiencia del usuario.
    var Interes by remember { mutableStateOf("") } // Estado para almacenar los intereses del usuario.
    var Nombre by remember { mutableStateOf("") } // Estado para almacenar el nombre del usuario.
    var ServiciosOfrecidos by remember { mutableStateOf("") } // Estado para almacenar los servicios ofrecidos.
    var ServiciosInteres by remember { mutableStateOf("") } // Estado para almacenar los servicios de interés.
    var dialogTitle by remember { mutableStateOf("") } // Título del diálogo de alerta.
    var dialogText by remember { mutableStateOf("") } // Texto del diálogo de alerta.
    var show by rememberSaveable { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de alerta.
    var IdUsuario by remember { mutableStateOf("") } // Estado para almacenar el ID del usuario.

    Column(modifier = Modifier.fillMaxSize().background(Black).verticalScroll(scroll), horizontalAlignment = Alignment.CenterHorizontally) {
        Row() {
            // Crea un botón para volver, se debe importar antes en la carpeta drawable.
            Icon(painter = painterResource(id = R.drawable.back), contentDescription = "", tint = White, modifier = Modifier.padding(24.dp).clickable { navHostController.popBackStack() })
            Spacer(Modifier.weight(1f)) // Espaciador para separar el icono del borde.
        }

        // Crea el texto del Email para identificar.
        Text("Email", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        // Crea el TextField del Email para ingresar el dato.
        TextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField)) // Cambiar colores del campo.

        Spacer(Modifier.height(40.dp)) // Espaciador para separar campos.

        // Repite el proceso para la contraseña, experiencia, interés, nombre, servicios ofrecidos, y servicios de interés.
        Text("Contraseña", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(value = contraseña, onValueChange = { contraseña = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField))

        Spacer(Modifier.height(40.dp))

        Text("Experiencia", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(value = Experiencia, onValueChange = { Experiencia = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField))

        Spacer(Modifier.height(40.dp))

        Text("Interes", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(value = Interes, onValueChange = { Interes = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField))

        Spacer(Modifier.height(40.dp))

        Text("Nombre", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(value = Nombre, onValueChange = { Nombre = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField))

        Spacer(Modifier.height(40.dp))

        Text("Servicios Ofrecidos", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(value = ServiciosOfrecidos, onValueChange = { ServiciosOfrecidos = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField))

        Spacer(Modifier.height(40.dp))

        Text("Servicios Interes", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(value = ServiciosInteres, onValueChange = { ServiciosInteres = it }, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = UnselectedField, focusedContainerColor = SelectedField))

        // Lógica del botón para registrarse.
        Spacer(Modifier.height(40.dp))
        Button(onClick = {
            // Verificar que la contraseña tenga al menos 8 caracteres.
            if (contraseña.length < 8) {
                show = true
                dialogTitle = "Alerta"
                dialogText = "La contraseña debe tener al menos 8 caracteres"
            } else {
                if (email.isNotEmpty() && contraseña.isNotEmpty()) { // Verificar que email y contraseña no estén vacíos.
                    auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener { task ->
                        if (task.isSuccessful) { // Si el registro es exitoso.
                            IdUsuario = auth.currentUser?.uid.toString() // Obtener el ID del usuario.
                            if (Experiencia.isNotEmpty() && ServiciosInteres.isNotEmpty() && Interes.isNotEmpty() && Nombre.isNotEmpty() && ServiciosOfrecidos.isNotEmpty()) {
                                // Conectar a Firestore y crear el registro de usuario.
                                val db = FirebaseFirestore.getInstance().collection("UsersProfile")
                                val dat = DatosUsuario( // Crear la instancia de los datos del usuario.
                                    Experiencia,
                                    Interes,
                                    Nombre,
                                    ServiciosOfrecidos,
                                    ServiciosInteres,
                                    email,
                                    IdUsuario
                                )
                                db.document().set(dat) // Guardar el nuevo registro en Firestore.
                                // Mostrar mensaje de éxito.
                                show = true
                                dialogTitle = "Alerta"
                                dialogText = "Registro Exitoso"
                                // Limpiar campos después del registro exitoso.
                                Experiencia = ""
                                Interes = ""
                                Nombre = ""
                                ServiciosOfrecidos = ""
                                ServiciosInteres = ""
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
                            if (exception is FirebaseAuthWeakPasswordException) {
                                show = true
                                dialogTitle = "Alerta"
                                dialogText = "La contraseña es demasiado débil"
                            } else if (exception is FirebaseAuthUserCollisionException) {
                                show = true
                                dialogTitle = "Alerta"
                                dialogText = "El correo ya está registrado"
                            } else {
                                show = true
                                dialogTitle = "Alerta"
                                dialogText = "Error al registrar: ${exception?.message}"
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
        }, // fillMaxWidth hace que el botón se expanda al ancho de la pantalla.
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)) {
            Text("Sign Up") // Texto del botón.
        }

        Spacer(Modifier.height(40.dp)) // Espaciador para separar el botón del siguiente elemento.

        // Llama a la función 'mensaje' para mostrar el diálogo de alerta.
        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }
}


