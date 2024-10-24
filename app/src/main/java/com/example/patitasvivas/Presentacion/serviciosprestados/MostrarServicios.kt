package com.example.patitasvivas.Presentacion.serviciosprestados

// Importaciones necesarias para la funcionalidad de la aplicación
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast  // Para mostrar mensajes de toast
import androidx.compose.foundation.background  // Para establecer un fondo en componentesl
import androidx.compose.foundation.layout.*  // Para utilizar el sistema de diseño
import androidx.compose.foundation.lazy.LazyColumn  // Para mostrar listas perezosas
import androidx.compose.foundation.lazy.items  // Para iterar sobre listas en LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*  // Para componentes de material design
import androidx.compose.runtime.*  // Para el manejo del estado y composición
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment  // Para alinear elementos en un contenedor
import androidx.compose.ui.Modifier  // Para modificar el comportamiento y apariencia de un componente
import androidx.compose.ui.graphics.Color  // Para utilizar colores
import androidx.compose.ui.platform.LocalContext  // Para obtener el contexto local de la aplicación
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign  // Para alinear texto
import androidx.compose.ui.unit.dp  // Para definir unidades de medida en dp
import androidx.compose.ui.unit.sp
import com.example.patitasvivas.mensaje
import com.example.patitasvivas.ui.theme.Green
import com.google.firebase.auth.FirebaseAuth  // Para la autenticación de Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore  // Para interactuar con Firestore de Firebase
import com.google.firebase.messaging.FirebaseMessaging


// Composable para mostrar las mascotas del usuario
@Composable
fun MisServicios(auth: FirebaseAuth) {
    // Inicialización de Firestore
    val db = FirebaseFirestore.getInstance()
    // Referencia a la colección "pets" de Firestore
    val serviciosCollection = db.collection("services")
    // Variable que guarda la lista de mascotas del usuario
    var Servicios by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    // Obtiene el contexto de la aplicación
    val context = LocalContext.current

    // Variable que controlará el refresco de la pantalla
    var refreshTrigger by remember { mutableStateOf(0) }

    // Efecto que se ejecuta al iniciar la pantalla o cuando cambia el id del usuario
    LaunchedEffect(key1 = auth.currentUser?.uid.toString(), key2 = refreshTrigger) {
        // Consulta en Firestore para obtener las mascotas del usuario actual
        serviciosCollection.whereEqualTo("idUsuario", auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Si se encuentran Servicios, mapeamos los documentos a una lista de mapas y los guardamos en la variable "Servicios"
                    Servicios = documents.map { it.data }
                } else {
                    // Si no se encuentran Servicios, asignamos una lista vacía a "Servicios"
                    Servicios = emptyList()
                }
            }
            .addOnFailureListener {
                // Si ocurre un error en la consulta, mostramos un mensaje de error
                Toast.makeText(context, "Error al cargar las Servicios", Toast.LENGTH_SHORT).show()
            }
    }

    // Layout principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),  // Fondo de pantalla negro
        horizontalAlignment = Alignment.CenterHorizontally,  // Alinear los elementos horizontalmente al centro
        verticalArrangement = Arrangement.Center  // Alinear los elementos verticalmente al centro
    ) {
        // Título de la pantalla
        Text(text = "Mis Servicios Ofrecidos", color = Color.White, style = MaterialTheme.typography.headlineMedium)

        // Si "Servicios" es null, significa que los datos aún se están cargando
        if (Servicios == null) {
            // Mostrar un indicador de carga mientras los datos se obtienen
            CircularProgressIndicator(color = Color.White)
        } else if (Servicios!!.isEmpty()) {
            // Si "Servicios" está vacío, significa que no tiene Servicios registradas
            Text(
                text = "No tienes Servicios Ofrecidos registradas. ¡Registra un servicio que desees Prestar!",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Si la lista de Servicios tiene elementos, las mostramos en un LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Iterar sobre la lista de Servicios y mostrar una tarjeta (Card) por cada mascota
                items(Servicios!!) { Servicio ->
                    // Llama a la función MascotaCard para mostrar la tarjeta de cada mascota
                    ServiciosCard(
                        name = Servicio["name"] as? String ?: "Sin nombre",  // Mostrar nombre o "Sin nombre" si no está disponible
                        type = Servicio["type"] as? String ?: "Sin tipo",  // Mostrar raza o "Sin raza"
                        description = Servicio["description"] as? String ?: "Desconocido",  // Mostrar el año o "Desconocido"
                        cost = Servicio["cost"] as? String ?: "Gratuito",  // Mostrar estado de salud o "Desconocido"
                        idServicio=Servicio["idServicio"] as String,
                        onRefrescar = {
                            // Incrementar el valor de refreshTrigger para forzar la recomposición
                            refreshTrigger++
                        }
                    )
                }
            }
        }
    }
}

// Composable para mostrar la tarjeta (Card) de una mascota
@Composable
fun ServiciosCard(name: String, type: String, description: String, cost: String, idServicio: String,onRefrescar: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    // Crear una tarjeta transparente con los datos de la mascota
    Card(
        modifier = Modifier
            .fillMaxWidth()  // La tarjeta ocupa todo el ancho disponible
            .padding(8.dp),  // Margen alrededor de la tarjeta
        colors = CardDefaults.cardColors( // Fondo transparente
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(4.dp)  // Elevación de la tarjeta
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(8.dp)) // Fondo oscuro con bordes redondeados
                .padding(16.dp)
        ) {
            // Nombre de la mascota
            Text(
                text = "Nombre: $name",
                color = Color(0xFFFFC107), // Color dorado para resaltar el nombre
                style = MaterialTheme.typography.bodyMedium.copy( // Usar bodyMedium
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp // Espaciado de letras para un toque más elegante
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Tipo de mascota
            Text(
                text = "Tipo: $type",
                color = Color(0xFFB0BEC5), // Color gris claro para un toque elegante
                style = MaterialTheme.typography.bodyMedium.copy( // Usar bodyMedium
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic // Cursiva para diferenciación
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )

            // Descripción de la mascota
            Text(
                text = "Descripción: $description",
                color = Color(0xFFB0BEC5),
                style = MaterialTheme.typography.bodyMedium.copy( // Usar bodyMedium
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold // Resaltar el costo
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )

            // Costo de la mascota
            Text(
                text = "Costo: $cost", // Añadir el símbolo de moneda
                color = Color(0xFFB0BEC5),
                style = MaterialTheme.typography.bodyMedium.copy( // Usar bodyMedium
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold // Resaltar el costo
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )
        }
        // Botón para Suscribirse a un servicio
        Button(onClick = {
            // Referencia a la colección y el documento
            val documentRef = db.collection("services").document(idServicio)
            // Desuscribirse del tema
            FirebaseMessaging.getInstance().unsubscribeFromTopic(idServicio)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "Desuscrito exitosamente del tema: $idServicio")
                    } else {
                        Log.w("TAG", "Error al desuscribirse del tema", task.exception)
                    }
                }
            // Elimina el documento
            documentRef.delete()
                .addOnSuccessListener {
                    Log.d("TAG", "Documento eliminado con éxito")
                    onRefrescar()
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error al eliminar el documento", e)
                }
        },
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)) {
            Text("Eliminar Servicio Ofrecido") // Texto del botón.
        }
    }
}

