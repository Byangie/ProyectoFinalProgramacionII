import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patitasvivas.Presentacion.cuidados.ClinicaVeterinaria
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicasScreen() {
    val scope = rememberCoroutineScope()
    var clinicas by remember { mutableStateOf(listOf<ClinicaVeterinaria>()) }

    // Variables para el formulario de nueva clínica
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var resena by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) } // Variable de carga
    var message by remember { mutableStateOf("") } // Mensaje de éxito/error
    var showAlert by remember { mutableStateOf(false) } // Control de la alerta

    // Cargar los datos desde Firebase
    LaunchedEffect(Unit) {
        val repo = RepositorioClinicas()
        scope.launch {
            clinicas = repo.obtenerClinicas()
            loading = false // Cambia el estado de carga
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFB3E5FC)) // Color de fondo celeste
    ) {
        // Título
        Text(
            text = "Clínicas",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF0D47A1), // Color azul oscuro para el texto
            modifier = Modifier
                .padding(bottom = 19.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Formulario para agregar una nueva clínica
        Text(
            text = "Agrega una nueva clínica",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF0D47A1)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Modificador de campo de texto con bordes redondeados y menor tamaño
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(56.dp) // Ajusta la altura de los campos de texto

        // Campo de texto para el nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la clínica") },
            modifier = textFieldModifier,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
        )

        // Campo de texto para la dirección
        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = textFieldModifier,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
        )

        // Campo de texto para el contacto
        TextField(
            value = contacto,
            onValueChange = { contacto = it },
            label = { Text("Contacto") },
            modifier = textFieldModifier,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
        )

        // Campo de texto para la reseña
        TextField(
            value = resena,
            onValueChange = { resena = it },
            label = { Text("Reseña") },
            modifier = textFieldModifier,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar clínica
        Button(
            onClick = {
                val nuevaClinica = ClinicaVeterinaria(nombre, direccion, contacto, resena)
                scope.launch {
                    try {
                        RepositorioClinicas().agregarClinica(nuevaClinica) // Guardar en Firebase
                        clinicas = RepositorioClinicas().obtenerClinicas() // Actualizar la lista
                        message = "Clínica agregada exitosamente." // Mensaje de éxito
                        showAlert = true // Mostrar alerta
                    } catch (e: Exception) {
                        message = "Error al agregar la clínica." // Mensaje de error
                        showAlert = true // Mostrar alerta
                    }

                    // Limpiar los campos después de agregar la clínica
                    nombre = ""
                    direccion = ""
                    contacto = ""
                    resena = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)), // Color azul oscuro
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp) // Ajusta la altura del botón
        ) {
            Text(text = "Agregar Clínica", color = Color.White)
        }

        // Mensaje de éxito/error como alerta
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Mensaje") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Subtítulo centrado para "Clínicas cercanas"
        Text(
            text = "Clínicas cercanas",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF0D47A1),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 8.dp)
        )

        // Mostrar un indicador de carga mientras se obtienen las clínicas
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Mostrar las clínicas en tarjetas
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(clinicas.size) { index ->
                    val clinica = clinicas[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = clinica.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0D47A1))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Dirección: ${clinica.direccion}", fontSize = 16.sp )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Contacto: ${clinica.contacto}", fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Reseña: ${clinica.resena}", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

