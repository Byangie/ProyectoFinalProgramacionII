package com.example.patitasvivas.Presentacion.Menu

import ClinicasScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.navigation.NavHostController
import com.example.patitasvivas.Presentacion.GestionPet.GestionMascota
import com.example.patitasvivas.ui.theme.Black
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.patitasvivas.Presentacion.ConsejosCuidadoScreen
import com.example.patitasvivas.Presentacion.GestionPet.Mascotasenadpocionajenas
import com.example.patitasvivas.Presentacion.GraciasScreen
import com.example.patitasvivas.Presentacion.PantallaInicial.MostrarMascotas
import com.example.patitasvivas.Presentacion.PantallaInicio
import com.example.patitasvivas.Presentacion.chat.ui.ChatScreen
import com.example.patitasvivas.Presentacion.chat.viewmodel.ChatViewModel
import com.example.patitasvivas.Presentacion.quitaradopcion.QuitarAdopcion
import com.example.patitasvivas.Presentacion.serviciosprestados.MisServicios
import com.example.patitasvivas.Presentacion.serviciosprestados.OfferServiceScreen
import com.example.patitasvivas.Presentacion.serviciosprestados.ServiciosAjenos
import com.example.patitasvivas.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.patitasvivas.Modelo.DatosUsuario.DatosUsuario
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Menus(auth: FirebaseAuth, navHostController: NavHostController) {
    barra(auth, navHostController)
}

@Composable
fun barra(auth: FirebaseAuth, navHostController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    // Colores personalizados
    val primaryColor = Color(0xFF6200EE)
    val secondaryColor = Color(0xFF03DAC5)
    val backgroundColor = Color(0xFF121212)

    // Layout principal con un cajón modal
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp) // Ajusta el tamaño horizontal del drawer
            ) {
                // Habilitar el desplazamiento vertical en el menú
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()) // Añadir desplazamiento
                ) {
                    // Menú de navegación
                    MenuItem(text = "Inicio", navController, "Contenido principal", drawerState, scope)
                    MenuItem(text = "Mis mascotas", navController, "Mis mascotas", drawerState, scope)
                    MenuItem(text = "Gestión De Mascotas", navController, "Gestion Mascotas", drawerState, scope)
                    MenuItem(text = "Mis mascotas en Adopción", navController, "Mis mascotas en Adopcion", drawerState, scope)
                    MenuItem(text = "Mascotas Ajenas para Adoptar", navController, "Mascotas Ajenas para Adoptar", drawerState, scope)
                    MenuItem(text = "Agregar Servicios", navController, "Agregar Servicios", drawerState, scope)
                    MenuItem(text = "Mostrar mis Servicios", navController, "Mostrar mis Servicios", drawerState, scope)
                    MenuItem(text = "Servicios Ajenos para Adquirir", navController, "Servicios Ajenos para Adquirir", drawerState, scope)
                    MenuItem(text = "Consejos de Cuidado", navController, "ConsejosCuidado", drawerState, scope)
                    MenuItem(text = "Clinica", navController, "Clinica", drawerState, scope)
                    MenuItem(text = "Chat", navController, "Chat", drawerState, scope)
                    MenuItem(text = "Cerrar Sesión", navController, "Cerrar Sesión", drawerState, scope)
                    MenuItem(text = "Unamas", navController, "bye", drawerState, scope)
                }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            "Menu",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    onClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    },
                    containerColor = secondaryColor, // Color del botón
                    contentColor = Color.White
                )
            },
            containerColor = backgroundColor, // Color de fondo de la pantalla principal
            modifier = Modifier.fillMaxSize()
        ) { contentPadding ->
            // Contenido principal de la pantalla
            NavHost(navController = navController, startDestination = "Contenido principal") {
                composable("Contenido principal") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        PantallaInicio(navHostController = navController)
                    }
                }
                composable("Mis Mascotas") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MostrarMascotas(auth)
                    }
                }
                composable("Gestion Mascotas") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        GestionMascota(auth)
                    }
                }
                composable("Mis mascotas en Adopcion") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        QuitarAdopcion(auth)
                    }
                }
                composable("Mascotas Ajenas para Adoptar") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Mascotasenadpocionajenas(auth)
                    }
                }
                composable("Agregar Servicios") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        OfferServiceScreen(auth)
                    }
                }
                composable("Mostrar mis Servicios") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MisServicios(auth)
                    }
                }
                composable("Servicios Ajenos para Adquirir") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ServiciosAjenos(auth)
                    }
                }
                composable("ConsejosCuidado") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ConsejosCuidadoScreen()
                    }
                }
                composable("Clinica") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ClinicasScreen()
                    }
                }
                composable("Chat") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Firebase.auth.currentUser?.let { it1 -> ChatScreen("nombre", it1.uid,viewModel = viewModel()) }
                    }
                }
                composable("Cerrar Sesión") {
                    // Agrega aquí la lógica para cerrar sesión
                    auth.signOut()
                }
            }
        }
    }
}

@Composable
fun MenuItem(text: String, navController: NavHostController, route: String, drawerState: DrawerState, scope: CoroutineScope) {
    Text(
        text = text,
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(route) // Navegación al hacer clic
                scope.launch {
                    drawerState.close() // Cerrar el cajón después de la navegación
                }
            }
            .padding(10.dp)
    )
}
