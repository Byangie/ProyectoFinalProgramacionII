package com.example.patitasvivas.Presentacion.Mascotasenadopcion

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.patitasvivas.mensaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class SolicitudAdopcion(
    val IdSolicitud: String = "",
    val idMascotaenadopcion: String = "",
    val idUsuario: String = "",
    val nombre: String = "",
    val raza: String = "",
    val año: String = "",
    val estadoSalud: String = "",
    val Estado: String = "",
    val ImagenUrls: List<String> = emptyList()
)

data class UserProfile(
    val idUsuario: String = "",
    val nombre: String = "",
    val email: String = ""
)

@Composable
fun SolicitudAdopcionScreen(auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    var solicitudes by remember { mutableStateOf(emptyList<SolicitudAdopcion>()) }
    var isLoading by remember { mutableStateOf(true) }
    var isUpdated by remember { mutableStateOf(false) } // Estado para forzar la actualización

    LaunchedEffect(Unit, isUpdated) {  // Agregar isUpdated para actualizar al cambiar
        val db = FirebaseFirestore.getInstance()

        // Filtrar solicitudes donde el usuario autenticado es el dueño de la mascota
        val snapshot = db.collection("SolicitudAdopción").get().await()
        solicitudes = snapshot.documents.mapNotNull { doc ->
            val solicitud = SolicitudAdopcion(
                IdSolicitud = doc.id,
                idMascotaenadopcion = doc.getString("idMascotaenadopcion") ?: "",
                idUsuario = doc.getString("idUsuario") ?: "",
                nombre = doc.getString("nombre") ?: "",
                raza = doc.getString("raza") ?: "",
                año = doc.getString("año") ?: "",
                estadoSalud = doc.getString("estadoSalud") ?: "",
                Estado = doc.getString("Estado") ?: "",
                ImagenUrls = doc.get("ImagenUrls") as? List<String> ?: emptyList()
            )

            // Obtener el documento de la mascota y verificar si el usuario actual es el dueño
            val petDoc = db.collection("pets").document(solicitud.idMascotaenadopcion).get().await()
            val idUsuarioDueno = petDoc.getString("IdUsuario")

            // Filtrar las solicitudes que corresponden al usuario actual como dueño
            if (idUsuarioDueno == currentUser?.uid) solicitud else null
        }
        isLoading = false
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (solicitudes.isEmpty()) {
                Text(
                    text = "No tienes solicitudes pendientes",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(solicitudes) { solicitud ->
                        SolicitudAdopcionCard(solicitud) {
                            isUpdated = !isUpdated // Cambia el estado para refrescar la lista
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitudAdopcionCard(solicitudAdopcion: SolicitudAdopcion, onSolicitudProcessed: () -> Unit) {
    val userSolicitante = remember { mutableStateOf<UserProfile?>(null) }
    val userDuenoMascota = remember { mutableStateOf<UserProfile?>(null) }
    var dialogTitle by remember { mutableStateOf("") } // Título del diálogo de alerta.
    var dialogText by remember { mutableStateOf("") } // Texto del diálogo de alerta.
    var show by rememberSaveable { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de alerta.
    // Obtener datos del usuario que solicitó la adopción
    LaunchedEffect(solicitudAdopcion.idUsuario) {
        val userProfileDoc = FirebaseFirestore.getInstance().collection("UsersProfile")
            .whereEqualTo("userid", solicitudAdopcion.idUsuario)
            .get()
            .await()
        if (userProfileDoc.documents.isNotEmpty()) {
            userSolicitante.value = userProfileDoc.documents[0].toObject(UserProfile::class.java)
        }
    }

    // Obtener datos del dueño de la mascota
    LaunchedEffect(solicitudAdopcion.idMascotaenadopcion) {
        val petDoc = FirebaseFirestore.getInstance().collection("pets")
            .document(solicitudAdopcion.idMascotaenadopcion)
            .get()
            .await()
        val idUsuarioDueno = petDoc.getString("IdUsuario")

        idUsuarioDueno?.let {
            val userProfileDoc = FirebaseFirestore.getInstance().collection("UsersProfile")
                .whereEqualTo("userid", it)
                .get()
                .await()
            if (userProfileDoc.documents.isNotEmpty()) {
                userDuenoMascota.value = userProfileDoc.documents[0].toObject(UserProfile::class.java)
                Log.d("UserProfile", "Datos del usuario: $userDuenoMascota")
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            solicitudAdopcion.ImagenUrls.firstOrNull()?.let { imageUrl ->
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = "Imagen de la Mascota",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = "Mascota: ${solicitudAdopcion.nombre}",
                color = Color(0xFF00796B),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Raza: ${solicitudAdopcion.raza}", color = Color(0xFF004D40))
            Text(text = "Edad: ${solicitudAdopcion.año}", color = Color(0xFF004D40))
            Text(text = "Estado de Salud: ${solicitudAdopcion.estadoSalud}", color = Color(0xFF004D40))
            Text(text = "Estado de Adopción: ${solicitudAdopcion.Estado}", color = Color(0xFF004D40))

            userSolicitante.value?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Solicitante: ${it.nombre}", color = Color(0xFF01579B), fontWeight = FontWeight.Bold)
                Text(text = "Correo: ${it.email}", color = Color(0xFF01579B))
            }

            userDuenoMascota.value?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Dueño de la mascota: ${it.nombre}", color = Color(0xFF01579B), fontWeight = FontWeight.Bold)
                Text(text = "Correo: ${it.email}", color = Color(0xFF01579B))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        aceptarSolicitud(solicitudAdopcion, userSolicitante)
                        onSolicitudProcessed() // Llamar a la actualización
                        show = true
                        dialogTitle = "Alerta"
                        dialogText = "Tu mascota fue Adoptada por " + userSolicitante.value?.nombre
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "Aceptar Solicitud", color = Color.White)
                }

                Button(
                    onClick = {
                        denegarSolicitud(solicitudAdopcion.IdSolicitud)
                        onSolicitudProcessed() // Llamar a la actualización
                        show = true
                        dialogTitle = "Alerta"
                        dialogText = "Solicitud denegada Exitosamente"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text(text = "Denegar Solicitud", color = Color.White)
                }
            }
        }

        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }
}


fun aceptarSolicitud(
    solicitudAdopcion: SolicitudAdopcion,
    userSolicitante: MutableState<UserProfile?>
) {
    val db = FirebaseFirestore.getInstance()
    val mascotaId = solicitudAdopcion.idMascotaenadopcion
    val nuevoIdUsuario = solicitudAdopcion.idUsuario


    if (mascotaId.isBlank() || nuevoIdUsuario.isBlank() || userSolicitante.value?.email.isNullOrBlank()) {
        println("Datos insuficientes para procesar la solicitud.")
        return
    }

    // Consultar y eliminar solicitudes relacionadas
    db.collection("SolicitudAdopción")
        .whereEqualTo("idMascotaenadopcion", mascotaId)
        .get()
        .addOnSuccessListener { snapshot ->
            db.runBatch { batch ->
                // Eliminar todas las solicitudes de adopción relacionadas
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }

                // Actualizar el documento de la mascota
                val petRef = db.collection("pets").document(mascotaId)
                batch.update(petRef, mapOf(
                    "IdUsuario" to nuevoIdUsuario,
                    "Estado" to "",
                    "Correo" to userSolicitante.value?.email
                ))
            }.addOnSuccessListener {
                // La transacción se completó exitosamente
                println("Mascota transferida exitosamente y solicitudes eliminadas.")
            }.addOnFailureListener { e ->
                println("Error al aceptar la solicitud: ${e.message}")
            }
        }
        .addOnFailureListener { e ->
            println("Error al obtener las solicitudes de adopción: ${e.message}")
        }
}

// Función para denegar la solicitud
fun denegarSolicitud(idSolicitud: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("SolicitudAdopción").document(idSolicitud)
        .delete()
        .addOnSuccessListener {
            // La solicitud se eliminó exitosamente
            println("Solicitud denegada y eliminada.")
        }
        .addOnFailureListener { e ->
            println("Error al denegar la solicitud: ${e.message}")
        }
}
