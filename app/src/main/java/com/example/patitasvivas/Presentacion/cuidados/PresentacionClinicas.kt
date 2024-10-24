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

@Composable
fun ClinicasScreen() {
    val scope = rememberCoroutineScope()
    var clinicas by remember { mutableStateOf(listOf<ClinicaVeterinaria>()) }

    // Variables para el formulario de nueva clínica
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var resena by remember { mutableStateOf("") }

    // Cargar los datos desde Firebase
    LaunchedEffect(Unit) {
        val repo = RepositorioClinicas()
        scope.launch {
            clinicas = repo.obtenerClinicas()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Título
        Text(
            text = "Clínicas",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 19.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Formulario para agregar una nueva clínica
        Text(text = "Agrega una nueva clínica", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para el nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la clínica") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de texto para la dirección
        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de texto para el contacto
        TextField(
            value = contacto,
            onValueChange = { contacto = it },
            label = { Text("Contacto") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de texto para la reseña
        TextField(
            value = resena,
            onValueChange = { resena = it },
            label = { Text("Reseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar clínica
        Button(onClick = {
            val nuevaClinica = ClinicaVeterinaria(nombre, direccion, contacto, resena)
            scope.launch {
                RepositorioClinicas().agregarClinica(nuevaClinica) // Guardar en Firebase
                clinicas = RepositorioClinicas().obtenerClinicas() // Actualizar la lista

                // Limpiar los campos después de agregar la clínica
                nombre = ""
                direccion = ""
                contacto = ""
                resena = ""
            }
        }) {
            Text(text = "Agregar Clínica")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Subtítulo centrado para "Clínicas cercanas"
        Text(
            text = "Clínicas cercanas",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 8.dp)
        )

        // Mostrar las clínicas en una tabla
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(clinicas.size) { index ->
                val clinica = clinicas[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Gray)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = clinica.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Dirección: ${clinica.direccion}", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Contacto: ${clinica.contacto}", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Reseña: ${clinica.resena}", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
