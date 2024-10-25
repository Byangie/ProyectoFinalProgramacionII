package com.example.patitasvivas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.patitasvivas.Presentacion.GestionPet.GestionMascota
import com.example.patitasvivas.Presentacion.GraciasScreen
import com.example.patitasvivas.Presentacion.Inicio.Inicio
import com.example.patitasvivas.Presentacion.Login.Login
import com.example.patitasvivas.Presentacion.Menu.Menus
import com.example.patitasvivas.Presentacion.Signup.Signup
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navegacion(navHostController: NavHostController, auth: FirebaseAuth) {
    NavHost(navController = navHostController, startDestination = "INICIO") {
        composable("INICIO") {
            Inicio(
                navigateToLogin = { navHostController.navigate("LOGIN") },
                navigateToSignUp = { navHostController.navigate("Signup") }
            )
        }

        composable("LOGIN") {
            Login(auth, navHostController) {
                navHostController.navigate("Menu")
            }
        }

        composable("Signup") {
            Signup(auth, navHostController)
        }

        composable("Menu") {
            Menus(auth, navHostController)
        }
//Cerrando_Sesion
        composable("CerrarSesion") {
            GraciasScreen(onCerrarSesion = {
                auth.signOut() // Cerrar sesión usando FirebaseAuth
                navHostController.navigate("INICIO") {
                    popUpTo("INICIO") { inclusive = true } // Limpiar el back stack
                }
            })
        }
    }
}
