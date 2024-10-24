package com.example.patitasvivas

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.patitasvivas.ui.theme.PatitasVivasTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    //variable tipo navHostController para la funcion creada en el archivo Navegacionrutas
    private lateinit var navHostController: NavHostController
    //para usar el firebase para la autenticacion se declara una variable
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //se inicializa la variable
        auth= Firebase.auth



        setContent {
            var permissionGranted by remember { mutableStateOf(false) } // Verifica si tenemos permiso para enviar notificaciones

            // Obtenemos el contexto de la aplicación
            val context = LocalContext.current // Contexto que se usará para la creación de notificaciones

            // Verificamos si ya tenemos permiso para enviar notificaciones
            LaunchedEffect(Unit) { // Ejecuta el bloque de código una vez cuando se inicia
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS // Verificamos el permiso de notificaciones
                    ) == PackageManager.PERMISSION_GRANTED // Comprobamos si el permiso está concedido
                ) {
                    permissionGranted = true // Si tenemos permiso, lo decimos
                } else {
                    requestNotificationPermission(context) // Si no, pedimos permiso
                }
            }

            //se suscribe a un tema esto se utiliza para realizar el envio cuando el usuario presione agregar un servicio.
            FirebaseMessaging.getInstance().subscribeToTopic("servicios")
                .addOnCompleteListener { task ->
                    var msg = if (task.isSuccessful) "Suscrito al servicios!" else "Falló la suscripción."
                    Log.d(TAG, msg)
                }

            //se inicializa la variable
            navHostController = rememberNavController()

            PatitasVivasTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //se llama la funcion que definimos en Navegacionrutas y le mandamos como parametro la varible creada como navHostController
                    Navegacion(navHostController, auth)
                }
            }
        }
    }

    //metodo que se ejecuta luego de logearse

    override fun onStart() {
        super.onStart()
        //variable para saber si estamos logeado o no
        val currentUser = auth.currentUser
        if(currentUser != null){
            //navegar siguiente pantalla
        }
    }

    // Función para solicitar permiso para enviar notificaciones
    private fun requestNotificationPermission(context: android.content.Context) {
        ActivityCompat.requestPermissions(
            context as Activity, // Aseguramos que el contexto es una actividad
            arrayOf(Manifest.permission.POST_NOTIFICATIONS), // Pedimos permiso para enviar notificaciones
            1001 // ID para la solicitud
        )
    }
}


