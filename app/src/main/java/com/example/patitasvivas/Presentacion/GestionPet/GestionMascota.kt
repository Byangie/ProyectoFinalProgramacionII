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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign  // Para usar la propiedad textAlign en el texto
import androidx.compose.ui.unit.sp
import com.google.firebase.messaging.FirebaseMessaging
@Composable
fun GestionMascota(auth: FirebaseAuth) {
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var estadoSalud by remember { mutableStateOf("") }
    var vacunas by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf(emptyList<Uri>()) }
    var isLoading by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        imageUris = uris
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Text(text = "Registrar Mascota", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold)

        InputField(label = "Nombre", value = nombre) { nombre = it }
        InputField(label = "Raza", value = raza) { raza = it }
        InputField(label = "Edad", value = anio, keyboardType = KeyboardType.Number) { anio = it }
        InputField(label = "Estado de Salud", value = estadoSalud) { estadoSalud = it }
        InputField(label = "Historial de Vacunación", value = vacunas) { vacunas = it }

        imageUris.forEach { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier.size(150.dp).padding(top = 8.dp),
                contentScale = ContentScale.Crop
            )
        }

        Button(onClick = { launcher.launch("image/*") }, enabled = !isLoading) {
            Text("Subir Imágenes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Handle data saving logic here
        }, enabled = !isLoading, colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.White)) {
            Text(text = "Guardar Mascota")
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        if (showMessage) {
            AlertDialog(
                onDismissRequest = { showMessage = false },
                title = { Text(dialogTitle) },
                text = { Text(dialogText) },
                confirmButton = {
                    Button(onClick = { showMessage = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun InputField(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = TextStyle(color = Color.Black)) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
    )
}