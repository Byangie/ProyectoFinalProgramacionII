package com.example.patitasvivas.Presentacion.Menu

import ClinicasScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.patitasvivas.Presentacion.GestionPet.GestionMascota
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
import com.example.patitasvivas.Presentacion.GestionPet.MascotasEnAdopcionAjenas
import com.example.patitasvivas.Presentacion.PantallaInicial.MostrarMascotas
import com.example.patitasvivas.Presentacion.PantallaInicio
import com.example.patitasvivas.Presentacion.chat.ui.ChatScreen
import com.example.patitasvivas.Presentacion.quitaradopcion.QuitarAdopcion
import com.example.patitasvivas.Presentacion.serviciosprestados.MisServicios
import com.example.patitasvivas.Presentacion.serviciosprestados.OfferServiceScreen
import com.example.patitasvivas.Presentacion.serviciosprestados.ServiciosAjenos
import com.example.patitasvivas.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.patitasvivas.Presentacion.Inicio.Inicio
import com.example.patitasvivas.Presentacion.Mascotasenadopcion.SolicitudAdopcionScreen
import com.example.patitasvivas.Presentacion.serviciosprestados.ServiciosAdquiridosScreen
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

    // Estados para controlar la visibilidad de los submenús
    val isMascotasExpanded = remember { mutableStateOf(false) }
    val isServiciosExpanded = remember { mutableStateOf(false) }
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
                    //MENU PRINCIPAL DE MASCOTAS
                    Text(
                        text = "Gestion de Mascotas",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                isMascotasExpanded.value = !isMascotasExpanded.value // Alternar visibilidad
                            }
                            .padding(10.dp)
                    )

                    // Submenú de "Mis mascotas"
                    if (isMascotasExpanded.value) {

                        Card(
                            modifier = Modifier
                                .padding(start = 24.dp, top = 4.dp)
                                .fillMaxWidth()
                                .background(Color.LightGray.copy(alpha = 0.1f)), // Fondo de la Card
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp) // Usa CardDefaults.cardElevation
                        ) {
                            Text(
                                text = "Mis mascotas",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        navController.navigate("Mis mascotas") // Navegación al hacer clic
                                        scope.launch {
                                            drawerState.apply { close() }
                                        }
                                    }
                                    .padding(10.dp)
                            )

                            Text(
                                text = "Agregar Mascota",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        navController.navigate("Gestion Mascotas") // Navegación al hacer clic
                                        scope.launch {
                                            drawerState.apply { close() }
                                        }
                                    }
                                    .padding(10.dp)
                            )

                            Text(
                                text = "Mis mascotas en Adopción",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        navController.navigate("Mis mascotas en Adopcion") // Navegación al hacer clic
                                        scope.launch {
                                            drawerState.apply { close() }
                                        }
                                    }
                                    .padding(10.dp)
                            )
                        }
                    }

                    MenuItem(text = "Solicitudes de Adopcion", navController, "Solicitudes de Adopcion", drawerState, scope)

                    MenuItem(text = "Mascotas Ajenas para Adoptar", navController, "Mascotas Ajenas para Adoptar", drawerState, scope)



                    //MENU PRINCIPAL DE Servicios
                    Text(
                        text = "Gestion de Servicios",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                isServiciosExpanded.value = !isServiciosExpanded.value // Alternar visibilidad
                            }
                            .padding(10.dp)
                    )




                    // Submenú de "Gestion de Servicio"
                    if (isServiciosExpanded.value) {

                        Card(
                            modifier = Modifier
                                .padding(start = 24.dp, top = 4.dp)
                                .fillMaxWidth()
                                .background(Color.LightGray.copy(alpha = 0.1f)), // Fondo de la Card
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp) // Usa CardDefaults.cardElevation
                        ) {
                            Text(
                                text = "Agregar Servicios",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        navController.navigate("Agregar Servicios") // Navegación al hacer clic
                                        scope.launch {
                                            drawerState.apply { close() }
                                        }
                                    }
                                    .padding(10.dp)
                            )

                            Text(
                                text = "Mostrar mis Servicios",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        navController.navigate("Mostrar mis Servicios") // Navegación al hacer clic
                                        scope.launch {
                                            drawerState.apply { close() }
                                        }
                                    }
                                    .padding(10.dp)
                            )
                        }
                    }

                    MenuItem(text = "Servicios Pendientes de realizar", navController, "Servicios Pendientes", drawerState, scope)
                    MenuItem(text = "Servicios Ajenos para Adquirir", navController, "Servicios Ajenos para Adquirir", drawerState, scope)
                    MenuItem(text = "Consejos de Cuidado", navController, "ConsejosCuidado", drawerState, scope)
                    MenuItem(text = "Clinica", navController, "Clinica", drawerState, scope)
                    MenuItem(text = "Chat", navController, "Chat", drawerState, scope)
                    MenuItem(text = "Cerrar Sesión", navController, "Cerrar Sesión", drawerState, scope)

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
                        MascotasEnAdopcionAjenas(auth)
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

                composable("Solicitudes de Adopcion") {

                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        SolicitudAdopcionScreen(auth)
                    }
                }



                        composable("Servicios Pendientes") {

                            Box(
                                modifier = Modifier
                                    .padding(contentPadding)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ServiciosAdquiridosScreen(auth)
                            }
                        }

                composable("Cerrar Sesión") {
                    // Cerrar sesión
                    auth.signOut()

                    // Navegar a la pantalla de inicio y limpiar el stack de navegación
                    navHostController.navigate("Inicio") {
                        // Limpiar el stack de navegación
                        popUpTo(navHostController.graph.startDestinationId) { inclusive = true }
                    }
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
