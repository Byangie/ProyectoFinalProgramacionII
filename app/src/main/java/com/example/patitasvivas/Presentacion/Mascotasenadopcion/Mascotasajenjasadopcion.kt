package com.example.patitasvivas.Presentacion.GestionPet

// Importaciones necesarias para la funcionalidad de la aplicación
// Importaciones necesarias para la funcionalidad de la aplicación
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.patitasvivas.enviarNotificaciones
import com.example.patitasvivas.mensaje
import com.example.patitasvivas.ui.theme.Green
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun MascotasEnAdopcionAjenas(auth: FirebaseAuth) {
    val db = FirebaseFirestore.getInstance()
    val petsCollection = db.collection("pets")
    var mascotas by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    val context = LocalContext.current

    LaunchedEffect(key1 = auth.currentUser?.uid.toString()) {
        petsCollection.whereNotEqualTo("IdUsuario", auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    mascotas = documents.map { it.data }.filter { mascota -> mascota["Estado"] as? String != "" }
                } else {
                    mascotas = emptyList()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al cargar las mascotas", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F1F1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mascotas en Adopción",
            color = Green,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (mascotas == null) {
            CircularProgressIndicator(color = Green)
        } else if (mascotas!!.isEmpty()) {
            Text(
                text = "No hay mascotas en adopción",
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(mascotas!!) { mascota ->
                    MascotaCard(
                        nombre = mascota["name"] as? String ?: "Sin nombre",
                        raza = mascota["Raza"] as? String ?: "Sin raza",
                        año = mascota["Año"] as? String ?: "Desconocido",
                        estadoSalud = mascota["EstadoSalud"] as? String ?: "Desconocido",
                        imagenUrls = mascota["ImagenUrls"] as? List<String>,
                        Estado = mascota["Estado"] as? String ?: "",
                        Idmascota = mascota["Idmascota"] as? String ?: "",
                        CorreoUsuario = mascota["Correo"] as? String ?: "Sin Correo",
                        idUsuario = auth.currentUser?.uid.toString(),
                        context
                    )
                }
            }
        }
    }
}

@Composable
fun MascotaCard(
    nombre: String,
    raza: String,
    año: String,
    estadoSalud: String,
    imagenUrls: List<String>?,
    Estado: String,
    Idmascota: String,
    CorreoUsuario: String,
    idUsuario: String,
    context: Context
) {
    var imagenActual by remember { mutableStateOf(0) }
    val db = FirebaseFirestore.getInstance()
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }
    var show by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEFEF))
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            imagenUrls?.let { urls ->
                GestureDetector2(
                    onSwipeLeft = { imagenActual = (imagenActual + 1) % urls.size },
                    onSwipeRight = { imagenActual = (imagenActual - 1 + urls.size) % urls.size }
                ) {
                    AsyncImage(
                        model = urls[imagenActual],
                        contentDescription = "Imagen de la mascota",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = "Nombre: $nombre", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
            Text(text = "Raza: $raza", color = Color(0xFF555555))
            Text(text = "Edad: $año", color = Color(0xFF777777))
            Text(text = "Estado de Salud: $estadoSalud", color = Color(0xFF888888))
            Text(text = "Correo: $CorreoUsuario", color = Color(0xFF999999))

            Button(
                onClick = {
                    Log.d(TAG, "Preciono el boton")
                    db.collection("SolicitudAdopción")
                        .whereEqualTo("idUsuario", idUsuario)
                        .whereEqualTo("idMascotaenadopcion", Idmascota)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                val documentReference = db.collection("SolicitudAdopción").document()
                                val Mascota = hashMapOf(
                                    "IdSolicitud" to documentReference.id,
                                    "idMascotaenadopcion" to Idmascota,
                                    "idUsuario" to idUsuario,
                                    "nombre" to nombre,
                                    "raza" to raza,
                                    "año" to año,
                                    "estadoSalud" to estadoSalud,
                                    "Estado" to Estado,
                                    "ImagenUrls" to imagenUrls
                                )

                                documentReference.set(Mascota)
                                    .addOnSuccessListener {
                                        enviarNotificaciones(
                                            context, "Se Interesaron En $nombre",
                                            "Autoriza o Deniega la adopcion", "", "$Idmascota"
                                        )
                                        show = true
                                        dialogTitle = "¡Solicitud Enviada!"
                                        dialogText = "Pendiente de confirmación."
                                    }
                                    .addOnFailureListener {
                                        show = true
                                        dialogTitle = "Error"
                                        dialogText = "Error al enviar los datos."
                                    }
                            } else {
                                show = true
                                dialogTitle = "Alerta"
                                dialogText = "Solicitud pendiente de confirmar o rechazar."
                            }
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "error")
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Enviar Solicitud de Adopción", color = Color.White)
            }
        }
        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }
}

@Composable
fun GestureDetector2(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    content: @Composable () -> Unit
) {
    val offsetX = remember { mutableStateOf(0f) }

    Box(modifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            offsetX.value += dragAmount
            change.consume()
            if (offsetX.value > 300) {
                onSwipeRight()
                offsetX.value = 0f
            } else if (offsetX.value < -300) {
                onSwipeLeft()
                offsetX.value = 0f
            }
        }
    }) {
        content()
    }
}
