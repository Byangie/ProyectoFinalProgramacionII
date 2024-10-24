package com.example.patitasvivas

import android.content.Context
import android.os.AsyncTask
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.*
import java.io.InputStream
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class NotificationTask(private val context: Context, private val Nombre:String,private val Tipo:String, private val Descripcion:String, private val Tema: String) : AsyncTask<Void, Void, String?>() {

    override fun doInBackground(vararg params: Void?): String? {
        return try {
            val inputStream: InputStream = context.assets.open("Credenciales.json") // Archivo en assets
            val googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

            googleCredentials.refreshIfExpired()  // Refresca el token si ha expirado
            googleCredentials.accessToken.tokenValue
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(accessToken: String?) {
        if (accessToken != null) {
            enviarNotificacion(accessToken)
        } else {
            println("Error al obtener el token de acceso")
        }
    }

    private fun enviarNotificacion(accessToken: String) {
        val topic = "$Tema"
        val json = """
        {
            "message": {
                "topic": "$topic",
                "notification": {
                    "title": "$Nombre",
                    "body": "$Tipo"
                }
            }
        }
        """

        val client = OkHttpClient()
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json)
        val request = Request.Builder()
            .url("https://fcm.googleapis.com/v1/projects/patitasvivas-1309b/messages:send")
            .post(body)
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body
                if (responseBody != null) {
                    println("Notificación enviada: ${responseBody.string()}")
                } else {
                    println("No hay cuerpo en la respuesta.")
                }
            }
        })
    }
}

// En tu actividad o fragmento
fun enviarNotificaciones(context: Context, Nombre:String, Tipo:String, Descripcion:String,Tema:String) {
    NotificationTask(context, Nombre,Tipo, Descripcion,Tema).execute()
    println("cuantas veces envia la notificacion")
}
