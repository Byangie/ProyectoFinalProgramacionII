package com.example.patitasvivas.Presentacion.serviciosprestados

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.* // Importamos herramientas para hacer la interfaz
import androidx.compose.foundation.text.KeyboardOptions // Para opciones del teclado
import androidx.compose.material3.* // Traemos herramientas de Material Design para la app
import androidx.compose.runtime.* // Para usar variables y estados en Compose
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment // Para alinear cosas en la pantalla
import androidx.compose.ui.Modifier // Para modificar cómo se ve cada parte
import androidx.compose.ui.graphics.Color // Para usar colores
import androidx.compose.ui.platform.LocalContext // Para obtener el contexto local
import androidx.compose.ui.text.TextStyle // Para el estilo del texto
import androidx.compose.ui.text.input.KeyboardType // Para tipos de teclado (números, texto, etc.)
import androidx.compose.ui.text.style.TextAlign // Para alinear el texto
import androidx.compose.ui.unit.dp // Para manejar medidas (días, altura, etc.)
import com.example.patitasvivas.enviarNotificaciones
import com.example.patitasvivas.mensaje
import com.google.firebase.auth.FirebaseAuth // Importamos Firebase para autenticación de usuarios
import com.google.firebase.firestore.FieldValue // Para manejar valores en Firestore
import com.google.firebase.firestore.FirebaseFirestore // Para interactuar con Firestore
import com.google.firebase.messaging.FirebaseMessaging


// Esta función muestra la pantalla para ofrecer un servicio
@Composable
fun OfferServiceScreen(auth: FirebaseAuth) {
    // Aquí definimos varias cosas que vamos a usar
    var serviceName by remember { mutableStateOf("") } // Nombre del servicio
    var serviceType by remember { mutableStateOf("") } // Tipo de servicio
    var serviceDescription by remember { mutableStateOf("") } // Descripción del servicio
    var serviceCost by remember { mutableStateOf("") } // Costo del servicio
    var isFree by remember { mutableStateOf(false) } // ¿Es gratuito?
    val context = LocalContext.current // Contexto que se usará para la creación de notificaciones
    var dialogTitle by remember { mutableStateOf("") } // Título del diálogo de alerta.
    var dialogText by remember { mutableStateOf("") } // Texto del diálogo de alerta.
    var show by rememberSaveable { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de alerta.

    // Inicializamos Firebase para que podamos usarlo
    val db = FirebaseFirestore.getInstance() // Obtenemos la instancia de Firestore

    // Obtenemos el ID del usuario que ha iniciado sesión
    val userId = auth.currentUser?.uid // Verificamos si el usuario ha iniciado sesión y obtenemos su ID
    val Correo=auth.currentUser?.email //obtener el Correo

    // Aquí comenzamos a construir nuestra interfaz
    Column(
        modifier = Modifier // Modificamos cómo se ve la columna
            .fillMaxSize() // La columna ocupa todo el espacio disponible
            .padding(16.dp), // Agregamos un poco de espacio alrededor
        verticalArrangement = Arrangement.Center, // Alineamos verticalmente al centro
        horizontalAlignment = Alignment.CenterHorizontally // Alineamos horizontalmente al centro
    ) {
        // Mostramos un texto que dice "Ofrecer un servicio para mascotas"
        Text(text = "Ofrecer un servicio para mascotas", color = Color.White)

        Spacer(modifier = Modifier.height(8.dp)) // Un espacio entre elementos

        // Campo para ingresar el nombre del servicio
        OutlinedTextField(
            value = serviceName, // El valor actual del campo
            onValueChange = { serviceName = it }, // Lo que pasa cuando cambia el texto
            label = { Text("Nombre del Servicio", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        Spacer(modifier = Modifier.height(8.dp)) // Otro espacio

        // Campo para ingresar el tipo de servicio
        OutlinedTextField(
            value = serviceType, // El valor actual del campo
            onValueChange = { serviceType = it }, // Lo que pasa cuando cambia el texto
            label = { Text("Tipo de Servicio (ej.: paseo, cuidado temporal)", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        Spacer(modifier = Modifier.height(8.dp)) // Otro espacio

        // Campo para ingresar la descripción del servicio
        OutlinedTextField(
            value = serviceDescription, // El valor actual del campo
            onValueChange = { serviceDescription = it }, // Lo que pasa cuando cambia el texto
            label = { Text(text = "Descripción del Servicio", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth().height(120.dp), // El campo ocupa todo el ancho y tiene una altura definida
            textStyle = TextStyle(color = Color.White) // Estilo del texto
        )

        Spacer(modifier = Modifier.height(8.dp)) // Otro espacio

        // Checkbox para seleccionar si el servicio es gratuito
        Row(verticalAlignment = Alignment.CenterVertically) { // Fila para alinear el checkbox y el texto
            Checkbox(checked = isFree, onCheckedChange = { isFree = it }, colors = CheckboxDefaults.colors(Color.White)) // La caja de verificación
            Text("Servicio Gratuito", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) // Texto al lado
        }

        // Si el servicio no es gratuito, pedimos el costo
        if (!isFree) {
            OutlinedTextField(
                value = serviceCost, // El valor actual del campo
                onValueChange = { serviceCost = it }, // Lo que pasa cuando cambia el texto
                label = { Text("Costo del Servicio", style = TextStyle(color = Color.White, textAlign = TextAlign.Center)) }, // Etiqueta del campo
                modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho
                textStyle = TextStyle(color = Color.White), // Estilo del texto
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Solo permite entrada numérica
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Otro espacio

        // Botón para ofrecer el servicio
        Button(onClick = { // Acción al hacer clic en el botón
            userId?.let { // Si tenemos el ID del usuario
                    // Llamamos a la función para ofrecer el servicio
                    offerService(
                        db, // Base de datos Firestore
                        it, // ID del usuario
                        serviceName, // Nombre del servicio
                        serviceType, // Tipo de servicio
                        serviceDescription, // Descripción del servicio
                        if (isFree) "Gratis" else serviceCost, // Costo del servicio
                        Correo.toString(),
                    ) {
                        enviarNotificaciones(context,"Servicio: $serviceName","Tipo: $serviceType", "$serviceDescription","servicios")

                        show = true // Mostramos el diálogo de alerta
                        dialogTitle = "Alerta" // Título del diálogo
                        dialogText = "Servicio Agregado, por favor revise su notificaciones" // Texto del diálogo
                        // Aquí limpiamos los campos después de enviar el servicio
                        serviceName = "" // Reiniciamos el campo del nombre
                        serviceType = "" // Reiniciamos el campo del tipo de servicio
                        serviceDescription = "" // Reiniciamos el campo de descripción
                        serviceCost = "" // Reiniciamos el campo de costo
                        isFree = false // Reiniciamos el estado del checkbox
                    }
            }
        }) {
            Text(text = "Ofrecer Servicio") // Texto del botón
        }

        // Mostramos el diálogo de alerta si show es verdadero
        // Llama a la función 'mensaje' para mostrar el diálogo de alerta.
        mensaje(show, cerrar = { show = false }, dialogTitle, dialogText)
    }

}

// Esta función se encarga de enviar el servicio a Firebase
fun offerService(
    db: FirebaseFirestore,
    userId: String,
    name: String,
    type: String,
    description: String,
    cost: String,
    Correo:String,
    onSuccess: () -> Unit
) {
    // Creamos un nuevo documento en la colección de servicios
    val documentReference = db.collection("services").document() // Aquí se crea un documento nuevo
    val IdServicio=documentReference.id
    // Definimos qué información vamos a enviar
    val service = hashMapOf(
        "idServicio" to documentReference.id, // Agregamos el ID del servicio
        "idUsuario" to userId, // Agregamos el ID del usuario
        "name" to name, // Agregamos el nombre del servicio
        "type" to type, // Agregamos el tipo de servicio
        "description" to description, // Agregamos la descripción
        "cost" to cost, // Agregamos el costo
        "timestamp" to FieldValue.serverTimestamp(),// Agregamos la hora en que se creó el servicio
        "correo" to Correo
    )
    //se suscribe a un tema esto se utiliza para realizar el envio cuando el usuario presione agregar un servicio.
    //se sucribe para que le llegue notificación que adquirieron un servicio que el ofrecio
    FirebaseMessaging.getInstance().subscribeToTopic("$IdServicio")
        .addOnCompleteListener { task ->
            var msg = if (task.isSuccessful) "Suscrito al $IdServicio!" else "Falló la suscripción."
            Log.d(TAG, msg)
        }
    // Subimos los datos a Firestore
    documentReference.set(service)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            // Si hay un error, no hacemos nada especial (podrías mostrar un mensaje)
        }
}
