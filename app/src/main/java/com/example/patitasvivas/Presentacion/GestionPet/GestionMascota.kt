package com.example.patitasvivas.Presentacion.GestionPet

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.patitasvivas.mensaje
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.patitasvivas.ui.theme.Black
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import androidx.compose.ui.text.TextStyle        // Para definir el estilo del texto
import androidx.compose.ui.text.style.TextAlign  // Para usar la propiedad textAlign en el texto
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun GestionMascota(auth: FirebaseAuth) {
// Obtiene una instancia de Firestore para realizar operaciones de base de datos
    val db = FirebaseFirestore.getInstance()
// Obtiene una instancia de Firebase Storage para cargar y almacenar imágenes
    val storage = FirebaseStorage.getInstance()
// Obtiene el contexto actual de la aplicación, útil para mostrar mensajes y obtener recursos
    val context = LocalContext.current
// Crea un alcance de coroutine para ejecutar operaciones asíncronas
    val scope = rememberCoroutineScope()
// Crea un estado para el scroll de la columna
    var scroll = rememberScrollState()

// Variables de estado para almacenar datos de la mascota y el ID del usuario
    var Nombre by remember { mutableStateOf("") } // Nombre de la mascota
    var Raza by remember { mutableStateOf("") } // Raza de la mascota
    var Año by remember { mutableStateOf("") } // Edad de la mascota
    var EstadoSalud by remember { mutableStateOf("") } // Estado de salud de la mascota
    var Vacunas by remember { mutableStateOf("") } // Historial de vacunación de la mascota
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) } // Lista para almacenar múltiples URIs de imágenes
    var IdUsuario by remember { mutableStateOf("") } // ID del usuario actual
    var Estado by remember { mutableStateOf("") } // Estado de salud de la mascota
    var Idmascota by remember { mutableStateOf("") } // Estado de salud de la mascota
    var isLoading by remember { mutableStateOf(false) }
    var Correo by remember { mutableStateOf("")}//obtener el correo
    var dialogTitle by remember { mutableStateOf("") } // Título del diálogo de alerta.
    var dialogText by remember { mutableStateOf("") } // Texto del diálogo de alerta.
    var show by rememberSaveable { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de alerta.

// Lanzador para abrir el selector de imágenes y obtener múltiples URIs
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        imageUris = uris // Actualiza la lista de URIs con las imágenes seleccionadas
    }

// Composición de la interfaz de usuario en una columna
    Column(
        modifier = Modifier
            .fillMaxSize() // Toma todo el tamaño disponible
            .background(Color.Black) // Establece un fondo negro
            .verticalScroll(scroll), // Habilita el desplazamiento vertical
        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
    ) {
        Spacer(Modifier.weight(1f)) // Espacio flexible para centrar los elementos

        // Título de la pantalla
        Text(text = "Registrar Mascota", color = Color.White)

        // Campo de texto para el nombre de la mascota
        OutlinedTextField(
            value = Nombre,
            onValueChange = { Nombre = it }, // Actualiza el estado al cambiar el texto
            label = { Text(text = "Nombre", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        // Campo de texto para la raza
        OutlinedTextField(
            value = Raza,
            onValueChange = { Raza = it }, // Actualiza el estado al cambiar el texto
            label = { Text(text = "Raza", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        // Campo de texto para la edad
        OutlinedTextField(
            value = Año,
            onValueChange = { Año = it }, // Actualiza el estado al cambiar el texto
            label = { Text(text = "Edad", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), // Permite solo números
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        // Campo de texto para el estado de salud
        OutlinedTextField(
            value = EstadoSalud,
            onValueChange = { EstadoSalud = it }, // Actualiza el estado al cambiar el texto
            label = { Text(text = "Estado de Salud", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        // Campo de texto para el historial de vacunación
        OutlinedTextField(
            value = Vacunas,
            onValueChange = { Vacunas = it }, // Actualiza el estado al cambiar el texto
            label = { Text(text = "Historial de Vacunación", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        // Muestra las imágenes seleccionadas en una lista
        imageUris.forEach { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri), // Carga la imagen de forma asíncrona
                contentDescription = null, // Descripción opcional para accesibilidad
                modifier = Modifier
                    .size(150.dp) // Establece un tamaño fijo para la imagen
                    .padding(top = 8.dp), // Agrega un margen superior
                contentScale = ContentScale.Crop // Recorta la imagen para que se ajuste al tamaño
            )
        }

        // Botón para abrir el selector de imágenes
        Button(onClick = {
            launcher.launch("image/*") // Abre el selector de imágenes para seleccionar múltiples
        }, enabled = !isLoading) {
            Text("Subir Imágenes") // Texto del botón
        }

        IdUsuario = auth.currentUser?.uid.toString() // Obtener el ID de usuario actual desde la autenticación
        Estado=""
        Correo=auth.currentUser?.email.toString()
        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el botón y los demás elementos
        // Botón para guardar los datos y subir las imágenes
        Button(onClick = {
            // Inicia una coroutine para realizar operaciones asíncronas
            scope.launch {
                if (imageUris.isNotEmpty()) { // Verifica si hay imágenes seleccionadas
                    // Crear una lista para almacenar las URLs de las imágenes
                    val imageUrls = mutableListOf<String>()
                    isLoading = true

                    // Subir cada imagen a Firebase Storage
                    for (uri in imageUris) {
                        // Crea una referencia en el almacenamiento para la imagen
                        val storageRef = storage.reference.child("pet_images/${uri.lastPathSegment}")
                        storageRef.putFile(uri).addOnSuccessListener {
                            // Obtiene la URL de la imagen una vez subida
                            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                // Agrega la URL a la lista
                                imageUrls.add(downloadUrl.toString())

                                // Si todas las imágenes han sido subidas, guardar en Firestore
                                if (imageUrls.size == imageUris.size) {
                                    // Guardar los detalles de la mascota junto con las URLs de las imágenes en Firestore
                                    val petData = hashMapOf(
                                        "name" to Nombre, // Nombre de la mascota
                                        "Raza" to Raza, // Raza de la mascota
                                        "Año" to Año, // Edad de la mascota
                                        "EstadoSalud" to EstadoSalud, // Estado de salud de la mascota
                                        "Vacunas" to Vacunas, // Historial de vacunación
                                        "ImagenUrls" to imageUrls, // Agregar la lista de URLs de las imágenes
                                        "IdUsuario" to IdUsuario, // ID del usuario
                                        "Estado" to Estado, // estado si se da en adpcion o no
                                        "Correo" to Correo //Correo electronico del quien es la mascota
                                    )

                                    // Agrega el documento a la colección "pets"
                                    db.collection("pets").add(petData)
                                        .addOnSuccessListener {documentReference ->
                                            // Obtener el ID del documento recién creado
                                            Idmascota = documentReference.id

                                            //se suscribe a un tema esto se utiliza para realizar el envio cuando el usuario presione agregar un servicio.
                                            //se sucribe para que le llegue notificación que adquirieron un servicio que el ofrecio
                                            FirebaseMessaging.getInstance().subscribeToTopic("$Idmascota")
                                                .addOnCompleteListener { task ->
                                                    var msg = if (task.isSuccessful) "Suscrito al $Idmascota!" else "Falló la suscripción."
                                                    Log.d(TAG, msg)
                                                }



                                            // Crear un nuevo mapa que incluya el ID del documento
                                            val updatedPetData = petData.toMutableMap() // Convertir a MutableMap para poder modificarlo
                                            updatedPetData["Idmascota"] = Idmascota // Agregar el ID del documento al mapa

                                            // Actualizar el documento con el ID incluido
                                            documentReference.set(updatedPetData) // Esto reemplazará los datos anteriores con el ID incluido
                                            // Muestra un mensaje de éxito
                                            isLoading = false
                                            show = true
                                            dialogTitle = "Alerta"
                                            dialogText = "Mascota guardada exitosamente"

                                            // Limpia los campos después de guardar
                                            Nombre = ""
                                            Raza = ""
                                            Año = ""
                                            EstadoSalud = ""
                                            Vacunas = ""
                                            imageUris = emptyList() // Limpiar la lista de imágenes

                                        }
                                        .addOnFailureListener {
                                            // Muestra un mensaje de error si no se puede guardar
                                            Toast.makeText(context, "Error al guardar la mascota", Toast.LENGTH_SHORT).show()
                                            isLoading = false
                                        }
                                }
                            }.addOnFailureListener {
                                // Muestra un mensaje de error si no se puede obtener la URL
                                Toast.makeText(context, "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show()
                                isLoading = false
                            }
                        }.addOnFailureListener {
                            // Muestra un mensaje de error si no se puede subir la imagen
                            Toast.makeText(context, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                    }
                } else {
                    // Mensaje de error si no se seleccionó ninguna imagen
                    Toast.makeText(context, "Por favor selecciona al menos una imagen", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            }
        }, enabled = !isLoading){
            Text(text = "Guardar Mascota") // Texto del botón para guardar
        }
        // Si está cargando, mostrar el CircularProgressIndicator centrado
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)), // Fondo semitransparente
                contentAlignment = Alignment.Center // Centra el indicador de progreso
            ) {
                CircularProgressIndicator(color = Color.White) // Indicador de carga centrado
            }
        }
        Spacer(Modifier.weight(1f)) // Espacio flexible al final de la columna
        // Llama a la función 'mensaje' para mostrar el diálogo de alerta.
        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }

}