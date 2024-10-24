package com.example.patitasvivas.Presentacion.Menu

import ClinicasScreen

//import PantallaInicio
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.patitasvivas.PantallaInicio
import com.example.patitasvivas.Presentacion.ConsejosCuidadoScreen
import com.example.patitasvivas.Presentacion.GestionPet.Mascotasenadpocionajenas
import com.example.patitasvivas.Presentacion.GraciasScreen
import com.example.patitasvivas.Presentacion.PantallaInicial.MostrarMascotas
import com.example.patitasvivas.Presentacion.PantallaInicio
//import com.example.patitasvivas.Presentacion.PantallaInicio.
//import com.example.patitasvivas.Presentacion.PantallaInicio.InicioPantalla
import com.example.patitasvivas.Presentacion.quitaradopcion.QuitarAdopcion
import com.example.patitasvivas.Presentacion.serviciosprestados.MisServicios
import com.example.patitasvivas.Presentacion.serviciosprestados.OfferServiceScreen
import com.example.patitasvivas.Presentacion.serviciosprestados.ServiciosAjenos
import com.example.patitasvivas.R
import kotlinx.coroutines.launch

@Composable
fun Menus(auth: FirebaseAuth, navHostController: NavHostController){
    barra(auth)
}

@Composable
fun barra(auth: FirebaseAuth) {
// Estado del cajón de navegación
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
// Ámbito de corrutina para manejar el estado del cajón
    val scope = rememberCoroutineScope()

    // Controlador de navegación
    val navController = rememberNavController()

// Colores personalizados
    val primaryColor = Color(0xFF6200EE)
    val secondaryColor = Color(0xFF03DAC5)
    val backgroundColor = Color(0xFF121212)
    val textColor = Color.White

// Layout principal con un cajón modal
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp) // Ajusta el tamaño horizontal del drawer
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight() // Ocupará toda la altura disponible
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Inicio",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Contenido principal") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )

                    Text(
                        text = "Mis mascotas",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Mis mascotas") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )

                    Text(
                        text = "Gestión De Mascotas",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
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
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Mis mascotas en Adopcion") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )

                    Text(
                        text = "Mascotas Ajenas para Adoptar",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Mascotas Ajenas para Adoptar") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )

                    Text(
                        text = "Agregar Servicios",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
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
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Mostrar mis Servicios") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )
                    Text(
                        text = "Servicios Ajenos para Adquirir",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Servicios Ajenos para Adquirir") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )

                    Text(
                        text = "Consejos de Cuidado",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("ConsejosCuidado") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )

                    Text(
                        text = "Clinica",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Clinica") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )
                    Text(
                        text = "Cerrar Sesión",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth() // Ocupa el ancho disponible (300.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Cerrar Sesión") // Navegación al hacer clic
                                scope.launch {
                                    drawerState.apply { close() }
                                }
                            }
                            .padding(10.dp)
                    )
                    // Agregar más opciones de menú aquí
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
                            drawerState.apply {
                                if (isClosed) {
                                    open()
                                } else {
                                    close()
                                }
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
                    // Contenido principal de la pantalla
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
                    // Contenido principal de la pantalla
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
                    // Nueva pantalla "Inicio"
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
                    // Nueva pantalla "Inicio"
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        QuitarAdopcion(auth)
                    }
                }

                composable("Agregar Servicios") {
                    // Nueva pantalla "Inicio"
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
                    // Nueva pantalla "Inicio"
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
                    // Nueva pantalla "Inicio"
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ServiciosAjenos(auth)
                    }
                }

                composable("Mascotas Ajenas para Adoptar") {
                    // Nueva pantalla "Inicio"
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Mascotasenadpocionajenas(auth)
                    }
                }

                composable(route = "ConsejosCuidado") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ConsejosCuidadoScreen()
                    }

                }

                composable(route = "Clinica") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ClinicasScreen()
                    }

                }
                composable("Cerrar Sesión") {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        GraciasScreen(onCerrarSesion = {
                            // Aquí puedes agregar la lógica para cerrar sesión
                            auth.signOut() // Cerrar sesión usando FirebaseAuth
                            navController.navigate("INICIO") {
                                // Limpiar el back stack para que no se pueda volver a la pantalla anterior
                                popUpTo("INICIO") { inclusive = true }
                            }
                        })
                    }
                }




            }
        }
    }
}



