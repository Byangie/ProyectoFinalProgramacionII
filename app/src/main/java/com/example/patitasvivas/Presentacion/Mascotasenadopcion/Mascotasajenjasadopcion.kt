package com.example.patitasvivas.Presentacion.GestionPet

// Importaciones necesarias para la funcionalidad de la aplicación
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast  // Para mostrar mensajes de toast
import androidx.compose.foundation.background  // Para establecer un fondo en componentes
import androidx.compose.foundation.gestures.detectHorizontalDragGestures  // Para detectar gestos de arrastre horizontal
import androidx.compose.foundation.layout.*  // Para utilizar el sistema de diseño
import androidx.compose.foundation.lazy.LazyColumn  // Para mostrar listas perezosas
import androidx.compose.foundation.lazy.items  // Para iterar sobre listas en LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape  // Para esquinas redondeadas en tarjetas
import androidx.compose.material3.*  // Para componentes de material design
import androidx.compose.runtime.*  // Para el manejo del estado y composición
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment  // Para alinear elementos en un contenedor
import androidx.compose.ui.Modifier  // Para modificar el comportamiento y apariencia de un componente
import androidx.compose.ui.draw.clip  // Para recortar los bordes de un componente
import androidx.compose.ui.graphics.Color  // Para utilizar colores
import androidx.compose.ui.input.pointer.pointerInput  // Para gestionar entradas de puntero
import androidx.compose.ui.layout.ContentScale  // Para escalar contenido de imágenes
import androidx.compose.ui.platform.LocalContext  // Para obtener el contexto local de la aplicación
import androidx.compose.ui.text.style.TextAlign  // Para alinear texto
import androidx.compose.ui.unit.dp  // Para definir unidades de medida en dp
import coil.compose.AsyncImage  // Para cargar imágenes de forma asíncrona
import com.example.patitasvivas.enviarNotificaciones
import com.example.patitasvivas.mensaje
import com.example.patitasvivas.ui.theme.Green  // Para importar un color de tema personalizado
import com.google.firebase.auth.FirebaseAuth  // Para la autenticación de Firebase
import com.google.firebase.firestore.FirebaseFirestore  // Para interactuar con Firestore de Firebase
import kotlinx.coroutines.launch  // Para lanzar corutinas

// Composable para mostrar las mascotas del usuario
@Composable
fun Mascotasenadpocionajenas(auth: FirebaseAuth) {
    // Inicialización de Firestore
    val db = FirebaseFirestore.getInstance()
    // Referencia a la colección "pets" de Firestore
    val petsCollection = db.collection("pets")
    // Variable que guarda la lista de mascotas del usuario
    var mascotas by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    // Obtiene el contexto de la aplicación
    val context = LocalContext.current

    // Efecto que se ejecuta al iniciar la pantalla o cuando cambia el id del usuario
    LaunchedEffect(key1 = auth.currentUser?.uid.toString()) {
        // Consulta en Firestore para obtener las mascotas en adopcion
        petsCollection.whereNotEqualTo("IdUsuario", auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Si se encuentran mascotas, mapeamos los documentos a una lista de mapas y los guardamos en la variable "mascotas"
                    // Filtramos las mascotas cuyo campo "Estado" no sea "Adopción"
                    mascotas = documents.map { it.data }.filter { mascota -> mascota["Estado"] as? String != ""}
                } else {
                    // Si no se encuentran mascotas, asignamos una lista vacía a "mascotas"
                    mascotas = emptyList()
                }
            }
            .addOnFailureListener {
                // Si ocurre un error en la consulta, mostramos un mensaje de error
                Toast.makeText(context, "Error al cargar las mascotas", Toast.LENGTH_SHORT).show()
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
        Text(text = "Mis Mascotas", color = Color.White, style = MaterialTheme.typography.headlineMedium)

        // Si "mascotas" es null, significa que los datos aún se están cargando
        if (mascotas == null) {
            // Mostrar un indicador de carga mientras los datos se obtienen
            CircularProgressIndicator(color = Color.White)
        } else if (mascotas!!.isEmpty()) {
            // Si "mascotas" está vacío, significa que no tiene mascotas registradas
            Text(
                text = "No hay mascotas en adopcion",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Si la lista de mascotas tiene elementos, las mostramos en un LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Iterar sobre la lista de mascotas y mostrar una tarjeta (Card) por cada mascota
                items(mascotas!!) { mascota ->
                    // Llama a la función MascotaCard para mostrar la tarjeta de cada mascota
                    MascotaCard(
                        nombre = mascota["name"] as? String ?: "Sin nombre",  // Mostrar nombre o "Sin nombre" si no está disponible
                        raza = mascota["Raza"] as? String ?: "Sin raza",  // Mostrar raza o "Sin raza"
                        año = mascota["Año"] as? String ?: "Desconocido",  // Mostrar el año o "Desconocido"
                        estadoSalud = mascota["EstadoSalud"] as? String ?: "Desconocido",  // Mostrar estado de salud o "Desconocido"
                        imagenUrls = mascota["ImagenUrls"] as? List<String>,  // Extraer la lista de URLs de imágenes, si existe
                        Estado = mascota["Estado"] as? String ?: "",
                        Idmascota = mascota["Idmascota"] as? String ?: "",
                        CorreoUsuario=mascota["Correo"] as? String ?: "Sin Correo",
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
fun MascotaCard(nombre: String, raza: String, año: String, estadoSalud: String, imagenUrls: List<String>?, Estado: String, Idmascota: String, CorreoUsuario:String,idUsuario:String, context: Context) {
    // Variable para el índice de la imagen actual
    var imagenActual by remember { mutableStateOf(0) }
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
        // Column para organizar el contenido de la tarjeta
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x55000000)) // Fondo semitransparente (con 55% de opacidad)
                .padding(16.dp) // Padding dentro de la tarjeta
        ) {
            // Si hay URLs de imagen, mostramos las imágenes
            imagenUrls?.let { urls ->
                // Usamos un GestureDetector para manejar los gestos de deslizamiento
                GestureDetector2(
                    onSwipeLeft = { imagenActual = (imagenActual + 1) % urls.size },  // Cambiar a la siguiente imagen
                    onSwipeRight = { imagenActual = (imagenActual - 1 + urls.size) % urls.size }  // Cambiar a la imagen anterior
                ) {
                    AsyncImage(
                        model = urls[imagenActual],  // URL de la imagen actual
                        contentDescription = "Imagen de la mascota",  // Descripción de la imagen
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)  // Tamaño de la imagen
                            .clip(RoundedCornerShape(8.dp)),  // Borde redondeado
                        contentScale = ContentScale.Crop // Escalar la imagen para que llene el espacio sin deformarse
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))  // Espacio entre la imagen y el texto
            }

            // Mostrar el nombre de la mascota
            Text(
                text = "Nombre: $nombre",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier=Modifier.height(48.dp)
            )
            // Mostrar la raza de la mascota
            Text(
                text = "Raza: $raza",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier=Modifier.height(48.dp)
            )
            // Mostrar la edad de la mascota
            Text(
                text = "Edad: $año",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier=Modifier.height(48.dp)
            )
            // Mostrar el estado de salud de la mascota
            Text(
                text = "Estado de Salud: $estadoSalud",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier=Modifier.height(48.dp)
            )
            // Mostrar el correo
            Text(
                text = "Correo: $CorreoUsuario",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier=Modifier.height(48.dp)
            )

            // Botón para colocar en estado adoptado
            Button(onClick = {
                Log.d(TAG, "Preciono el boton")
               //aca colocamos el envio de la notificacion y solicitud.
                db.collection("SolicitudAdopción")
                    .whereEqualTo("idUsuario", idUsuario) // Filtramos por el ID del usuario
                    .whereEqualTo("idMascotaenadopcion", Idmascota) // Filtramos por el ID del servicio ofrecido
                    .get()
                    .addOnSuccessListener { documents ->
                        Log.d(TAG, "entro")
                        if (documents.isEmpty) {
                            // Si no existe el servicio adquirido, procedemos a crear un nuevo documento
                            val documentReference = db.collection("SolicitudAdopción").document() // Aquí se crea un documento nuevo
                            Log.d(TAG, "Se crea la solicitud")
                            // Definimos qué información vamos a enviar
                            val Mascota = hashMapOf(
                                "IdSolicitud" to documentReference.id, // servicio adquirido
                                "idMascotaenadopcion" to Idmascota, // Agregamos el ID del servicio
                                "idUsuario" to idUsuario, // Agregamos el ID del usuario
                                "nombre" to nombre, // Agregamos el nombre del servicio
                                "raza" to raza, // Agregamos el tipo de servicio
                                "año" to año, // Agregamos la descripción
                                "estadoSalud" to estadoSalud, // Agregamos el costo
                                "Estado" to Estado, // Agregamos la hora en que se creó el servicio
                                "ImagenUrls" to imagenUrls // Agregar la lista de URLs de las imágenes
                            )

                            // Guardamos el servicio en Firestore
                            documentReference.set(Mascota)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Envia Notificacion y muestra mensaje")
                                    enviarNotificaciones(context,"Se Interesaron En $nombre","Autoriza o Deniega la adopcion", "","$Idmascota")
                                    // Mensaje de éxito
                                    show = true // Mostramos el diálogo de alerta
                                    dialogTitle = "Alerta" // Título del diálogo
                                    dialogText = "Solicitud Enviada, Pendiente de Confirmar o deneagar" // Texto del diálogo
                                }
                                .addOnFailureListener {
                                    show = true // Mostramos el diálogo de alerta
                                    dialogTitle = "Alerta" // Título del diálogo
                                    dialogText = "Error al enviar los datos al servidor"
                                }
                        } else {
                            show = true // Mostramos el diálogo de alerta
                            dialogTitle = "Alerta" // Título del diálogo
                            dialogText = "La Solicitud esta pendiente de confirmar o rechazar"
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "error")
                    }


            },
                modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Text("Envíar Solicitud de Adopcion") // Texto del botón.
            }
        }
        // Mostramos el diálogo de alerta si show es verdadero
        // Llama a la función 'mensaje' para mostrar el diálogo de alerta.
        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }
}

// Composable para detectar gestos de deslizamiento
@Composable
fun GestureDetector2(
    onSwipeLeft: () -> Unit,  // Acción al deslizar a la izquierda
    onSwipeRight: () -> Unit,  // Acción al deslizar a la derecha
    content: @Composable () -> Unit  // Contenido a mostrar
) {
    // Variable para guardar el valor del desplazamiento
    val offsetX = remember { mutableStateOf(0f) }

    // Utilizar pointerInput para detectar gestos
    Box(modifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            offsetX.value += dragAmount // Actualizar el desplazamiento

            change.consume() // Consumir el evento

            if (offsetX.value > 300) {  // Desplazamiento a la derecha
                onSwipeRight() // Ejecutar acción de deslizar a la derecha
                offsetX.value = 0f  // Reiniciar el desplazamiento
            } else if (offsetX.value < -300) {  // Desplazamiento a la izquierda
                onSwipeLeft() // Ejecutar acción de deslizar a la izquierda
                offsetX.value = 0f  // Reiniciar el desplazamiento
            }
        }
    }) {
        // Llamar al contenido
        content()
    }
}
