package com.example.patitasvivas.Presentacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

data class ConsejoCuidado(
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",  // "articulo", "video", "guia"
    val url: String = "",   // URL para videos o guías
    val imagen: String = "" // Imagen relacionada
)
@Composable
fun ConsejosCuidadoScreen() {
    val context = LocalContext.current
    var mostrarDescripcion by remember { mutableStateOf<ConsejoCuidado?>(null) }

    // Datos de ejemplo para recomendaciones
    val recomendaciones = listOf(
        ConsejoCuidado(
            titulo = "Ejercicio Regular",
            descripcion = "Asegúrate de sacar a tu perro a pasear al menos dos veces al día.",
            tipo = "articulo",
            imagen = "https://www.prensalibre.com/wp-content/uploads/2019/01/nutricion1.jpg"
        ),
        ConsejoCuidado(
            titulo = "Alimentación Balanceada",
            descripcion = "Proporciona una dieta equilibrada y adecuada a la edad de tu mascota.",
            tipo = "articulo",
            imagen = "https://blog.laika.com.co/wp-content/uploads/2022/06/p-pexels-doug-brown-790616.jpg.jpeg"
        ),
        ConsejoCuidado(
            titulo = "Visitas al Veterinario",
            descripcion = "Lleva a tu mascota al veterinario al menos una vez al año para chequeos.",
            tipo = "articulo",
            imagen = "https://madagascarmascotas.com/blog/wp-content/uploads/2023/11/prepara-mascota-visita-veterinario.jpg"
        ),
        ConsejoCuidado(
            titulo = "Juguetes Interactivos",
            descripcion = "Proporciona juguetes que estimulen la mente de tu mascota.",
            tipo = "articulo",
            imagen = "https://blog.gudog.com/wp-content/uploads/2016/01/Untitled-design-1-1024x768.jpg"
        )
    )

    // Datos de ejemplo para guías
    val guias = listOf(
        ConsejoCuidado(
            titulo = "Entrenamiento de Cachorros",
            descripcion = "Sigue esta guía para enseñar a tu cachorro los comandos básicos.",
            tipo = "guia",
            url = "https://mimascotayyo.elanco.com/mx/cachorros-mininos/guia-para-entrenar-cachorros",
            imagen = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtUyIFGgXfft_VAVPK3r_gMuDNP-F7N4X23A&s"
        ),
        ConsejoCuidado(
            titulo = "Higiene en Mascotas",
            descripcion = "Aprende cómo mantener la higiene adecuada de tu mascota.",
            tipo = "guia",
            url = "https://www.prensalibre.com/c-studio/la-higiene-de-las-mascotas/",
            imagen = "https://cdn.nubika.es/wp-content/uploads/2024/03/14082002/higiene-animales.jpg"
        ),
        ConsejoCuidado(
            titulo = "Alimentación de Gatos",
            descripcion = "Guía para una alimentación saludable y balanceada de tu gato.",
            tipo = "guia",
            url = "https://www.purina.es/cuidados/gatos/alimentacion/guia",
            imagen = "https://images.ctfassets.net/denf86kkcx7r/1qLfyKAX0rQgI5HWgHFpeQ/30e93a7a7ca8ab2e11a531f571918f3d/alimentacionequilibradagato-52"
        )
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(text = "Consejos de Cuidado", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Sección combinada de Recomendaciones y Guías
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Text(text = "Recomendaciones", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(recomendaciones) { recomendacion ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { mostrarDescripcion = recomendacion },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = recomendacion.titulo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (recomendacion.imagen.isNotEmpty()) {
                            Image(
                                painter = rememberImagePainter(recomendacion.imagen),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                        Text(text = recomendacion.descripcion, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Guías", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(guias) { guia ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { mostrarDescripcion = guia },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = guia.titulo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (guia.imagen.isNotEmpty()) {
                            Image(
                                painter = rememberImagePainter(guia.imagen),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                        Text(text = guia.descripcion, fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ver Guía",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Mostrar recomendación o guía seleccionada
        mostrarDescripcion?.let { consejo ->
            AlertDialog(
                onDismissRequest = { mostrarDescripcion = null },
                title = { Text(consejo.titulo) },
                text = { Text(consejo.descripcion) },
                confirmButton = {
                    Button(onClick = { mostrarDescripcion = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}
