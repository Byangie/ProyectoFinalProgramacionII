package com.example.patitasvivas.Presentacion.serviciosprestados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

data class ServicioAdquirido(
    val IdServicioAdquirido: String = "",
    val idServicioqueofrecieron: String = "",
    val idUsuario: String = "",
    val name: String = "",
    val type: String = "",
    val description: String = "",
    val cost: String = "",
    val timestamp: Timestamp? = null,
    val correo: String = ""
)

data class UserProfile(
    val idUsuario: String = "",
    val nombre: String = "",
    val email: String = ""
)

@Composable
fun ServiciosAdquiridosScreen(auth: FirebaseAuth) {
    var serviciosAdquiridos by remember { mutableStateOf(emptyList<ServicioAdquirido>()) }
    var isLoading by remember { mutableStateOf(true) }
    val currentUserId = auth.currentUser?.uid

    LaunchedEffect(Unit) {
        if (currentUserId != null) {
            serviciosAdquiridos = fetchServiciosAdquiridos(currentUserId)
        }
        isLoading = false
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            serviciosAdquiridos.isEmpty() -> NoServicesMessage()
            else -> ServicesList(serviciosAdquiridos, currentUserId, onServiciosUpdated = { nuevosServicios ->
                serviciosAdquiridos = nuevosServicios // Actualizar la lista aquí
            })
        }
    }
}

@Composable
fun NoServicesMessage() {
    Text(
        text = "No tienes servicios adquiridos ofrecidos por ti.",
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ServicesList(serviciosAdquiridos: List<ServicioAdquirido>, currentUserId: String?, onServiciosUpdated: (List<ServicioAdquirido>) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(serviciosAdquiridos) { servicio ->
            ServicioAdquiridoCard(servicio, currentUserId, onServiciosUpdated) // Pasar la función de actualización
        }
    }
}

@Composable
fun ServicioAdquiridoCard(
    servicioAdquirido: ServicioAdquirido,
    currentUserId: String?,
    onServiciosUpdated: (List<ServicioAdquirido>) -> Unit // Función para actualizar la lista
) {
    val solicitanteInfo = remember { mutableStateOf<UserProfile?>(null) }
    val proveedorInfo = remember { mutableStateOf<UserProfile?>(null) }
    val formattedDate = formatTimestamp(servicioAdquirido.timestamp)

    LaunchedEffect(servicioAdquirido.idUsuario) {
        solicitanteInfo.value = fetchUserProfile(servicioAdquirido.idUsuario)
    }

    LaunchedEffect(servicioAdquirido.idServicioqueofrecieron) {
        proveedorInfo.value = fetchServiceProvider(servicioAdquirido.idServicioqueofrecieron)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ServiceDetails(servicioAdquirido, formattedDate)
            UserProfileSection("Solicitante", solicitanteInfo.value)
            UserProfileSection("Proveedor", proveedorInfo.value)

            // Usar rememberCoroutineScope aquí dentro del Composable
            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    coroutineScope.launch {
                        eliminarYGuardarEnHistorial(
                            servicioAdquirido.IdServicioAdquirido,
                            solicitanteInfo.value,
                            proveedorInfo.value,
                            currentUserId ?: "",
                            { nuevosServicios ->
                                // Actualiza la lista de servicios adquiridos a través del callback
                                onServiciosUpdated(nuevosServicios)
                            }
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Finalizar Proceso de Servicio",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ServiceDetails(servicioAdquirido: ServicioAdquirido, formattedDate: String) {
    Text(
        text = "Servicio: ${servicioAdquirido.name}",
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = "Tipo: ${servicioAdquirido.type}", color = MaterialTheme.colorScheme.onSurface)
    Text(text = "Descripción: ${servicioAdquirido.description}", color = MaterialTheme.colorScheme.onSurface)
    Text(text = "Costo: ${servicioAdquirido.cost}", color = MaterialTheme.colorScheme.onSurface)
    Text(text = "Fecha: $formattedDate", color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun UserProfileSection(role: String, userProfile: UserProfile?) {
    userProfile?.let {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "$role: ${it.nombre}",
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        Text(text = "Correo: ${it.email}", color = MaterialTheme.colorScheme.onSurface)
    }
}

private fun formatTimestamp(timestamp: Timestamp?): String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return timestamp?.toDate()?.let { dateFormatter.format(it) } ?: "Sin fecha"
}

private suspend fun fetchServiciosAdquiridos(currentUserId: String): List<ServicioAdquirido> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val servicesSnapshot = db.collection("services")
            .whereEqualTo("idUsuario", currentUserId)
            .get()
            .await()

        val serviceIds = servicesSnapshot.documents.mapNotNull { it.id }

        if (serviceIds.isNotEmpty()) {
            val acquiredServicesSnapshot = db.collection("ServiciosAdquiridos")
                .whereIn("idServicioqueofrecieron", serviceIds)
                .get()
                .await()

            acquiredServicesSnapshot.documents.mapNotNull { doc ->
                doc.toObject(ServicioAdquirido::class.java)?.copy(IdServicioAdquirido = doc.id)
            }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}

private suspend fun fetchUserProfile(userId: String): UserProfile? {
    val db = FirebaseFirestore.getInstance()
    val userProfileDoc = db.collection("UsersProfile")
        .whereEqualTo("userid", userId)
        .get()
        .await()
    return if (userProfileDoc.documents.isNotEmpty()) {
        userProfileDoc.documents[0].toObject(UserProfile::class.java)
    } else {
        null
    }
}

private suspend fun fetchServiceProvider(serviceId: String): UserProfile? {
    val db = FirebaseFirestore.getInstance()
    val serviceDoc = db.collection("services").document(serviceId).get().await()
    val providerUserId = serviceDoc.getString("idUsuario")

    return providerUserId?.let {
        fetchUserProfile(it)
    }
}

private suspend fun eliminarYGuardarEnHistorial(servicioId: String, solicitante: UserProfile?, proveedor: UserProfile?, currentUserId: String, onServiciosUpdated: (List<ServicioAdquirido>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    // Guardar en historial
    solicitante?.let {
        val historialData = hashMapOf(
            "idServicio" to servicioId,
            "idSolicitante" to it.idUsuario,
            "nombreSolicitante" to it.nombre,
            "correoSolicitante" to it.email,
            "idProveedor" to proveedor?.idUsuario,
            "nombreProveedor" to proveedor?.nombre,
            "correoProveedor" to proveedor?.email,
            "fecha" to Timestamp.now()
        )

        // Guardar el historial
        db.collection("historialServiciosRealizados")
            .add(historialData)
            .await()
    }

    // Eliminar el servicio adquirido
    db.collection("ServiciosAdquiridos")
        .document(servicioId)
        .delete()
        .await()

    // Actualizar la lista de servicios adquiridos
    val nuevosServicios = fetchServiciosAdquiridos(currentUserId)
    onServiciosUpdated(nuevosServicios) // Pasar la lista actualizada a la función de callback
}
