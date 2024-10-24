package com.example.patitasvivas.Presentacion.serviciosprestados

import android.content.ContentValues.TAG
import android.content.Context
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
import com.example.patitasvivas.enviarNotificaciones
import com.example.patitasvivas.mensaje
import com.example.patitasvivas.ui.theme.Green
import com.google.firebase.auth.FirebaseAuth  // Para la autenticación de Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore  // Para interactuar con Firestore de Firebase
import com.google.firebase.messaging.FirebaseMessaging


// Composable para mostrar las mascotas del usuario
@Composable
fun ServiciosAjenos(auth: FirebaseAuth) {
    // Inicialización de Firestore
    val db = FirebaseFirestore.getInstance()
    // Referencia a la colección "pets" de Firestore
    val serviciosCollection = db.collection("services")
    // Variable que guarda la lista de mascotas del usuario
    var Servicios by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    // Obtiene el contexto de la aplicación
    val context = LocalContext.current

    // Efecto que se ejecuta al iniciar la pantalla o cuando cambia el id del usuario
    LaunchedEffect(key1 = auth.currentUser?.uid.toString()) {
        // Consulta en Firestore para obtener las Servicios de los demas menos los del login actual
        serviciosCollection.whereNotEqualTo("idUsuario", auth.currentUser?.uid.toString()).get()
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
        Text(text = "Servicios Ofrecidos", color = Color.White, style = MaterialTheme.typography.headlineMedium)

        // Si "Servicios" es null, significa que los datos aún se están cargando
        if (Servicios == null) {
            // Mostrar un indicador de carga mientras los datos se obtienen
            CircularProgressIndicator(color = Color.White)
        } else if (Servicios!!.isEmpty()) {
            // Si "Servicios" está vacío, significa que no tiene Servicios registradas
            Text(
                text = "No se tienen Servicios Para MOSTRAR",
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
                    ServiciosCardajenos(
                        name = Servicio["name"] as? String ?: "Sin nombre",  // Mostrar nombre o "Sin nombre" si no está disponible
                        type = Servicio["type"] as? String ?: "Sin tipo",  // Mostrar raza o "Sin raza"
                        description = Servicio["description"] as? String ?: "Desconocido",  // Mostrar el año o "Desconocido"
                        cost = Servicio["cost"] as? String ?: "Gratuito",  // Mostrar estado de salud o "Desconocido"
                        idServicio=Servicio["idServicio"] as String,
                        Correo=Servicio["correo"] as String,
                        idUsuario=auth.currentUser?.uid.toString(),
                        context
                    )
                }
            }
        }
    }
}

// Composable para mostrar la tarjeta (Card) de una mascota
@Composable
fun ServiciosCardajenos(name: String, type: String, description: String, cost: String, idServicio: String, Correo: String, idUsuario: String, context: Context) {
    val db = FirebaseFirestore.getInstance()
    var dialogTitle by remember { mutableStateOf("") } // Título del diálogo de alerta.
    var dialogText by remember { mutableStateOf("") } // Texto del diálogo de alerta.
    var show by rememberSaveable { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de alerta.
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
            // Nombre del Servicio
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
            //persona que lo ofrecio
            Text(
                text = "Ofrecido Por: $Correo",
                color = Color(0xFFFFC107), // Color dorado para resaltar el nombre
                style = MaterialTheme.typography.bodyMedium.copy( // Usar bodyMedium
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp // Espaciado de letras para un toque más elegante
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            // Tipo de Servicio
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
// Realizamos una consulta para verificar si el servicio ya ha sido adquirido por el usuario
            db.collection("ServiciosAdquiridos")
                .whereEqualTo("idUsuario", idUsuario) // Filtramos por el ID del usuario
                .whereEqualTo("idServicioqueofrecieron", idServicio) // Filtramos por el ID del servicio ofrecido
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Si no existe el servicio adquirido, procedemos a crear un nuevo documento
                        val documentReference = db.collection("ServiciosAdquiridos").document() // Aquí se crea un documento nuevo

                        // Definimos qué información vamos a enviar
                        val service = hashMapOf(
                            "IdServicioAdquirido" to documentReference.id, // servicio adquirido
                            "idServicioqueofrecieron" to idServicio, // Agregamos el ID del servicio
                            "idUsuario" to idUsuario, // Agregamos el ID del usuario
                            "name" to name, // Agregamos el nombre del servicio
                            "type" to type, // Agregamos el tipo de servicio
                            "description" to description, // Agregamos la descripción
                            "cost" to cost, // Agregamos el costo
                            "timestamp" to FieldValue.serverTimestamp(), // Agregamos la hora en que se creó el servicio
                            "correo" to Correo // Agregamos el correo
                        )

                        // Guardamos el servicio en Firestore
                        documentReference.set(service)
                            .addOnSuccessListener {
                                // Mensaje de éxito
                                enviarNotificaciones(context,"Se Interesaron En $name","Realiza el servicio", "","$idServicio")
                                show = true // Mostramos el diálogo de alerta
                                dialogTitle = "Alerta" // Título del diálogo
                                dialogText = "Servicio adquirido exitosamente, Revisa la seccion de Servicios Adquiridos" // Texto del diálogo
                            }
                            .addOnFailureListener {
                                show = true // Mostramos el diálogo de alerta
                                dialogTitle = "Alerta" // Título del diálogo
                                dialogText = "Error al enviar los datos al servidor"
                            }
                    } else {
                        show = true // Mostramos el diálogo de alerta
                        dialogTitle = "Alerta" // Título del diálogo
                        dialogText = "Ya Adquiriste este servicio, Verifica si ya lo realizaron"
                    }
                }
                .addOnFailureListener {
                }
        },
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)) {
            Text("Solicitar Servicio") // Texto del botón.
        }
        // Mostramos el diálogo de alerta si show es verdadero
        // Llama a la función 'mensaje' para mostrar el diálogo de alerta.
        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }


}

