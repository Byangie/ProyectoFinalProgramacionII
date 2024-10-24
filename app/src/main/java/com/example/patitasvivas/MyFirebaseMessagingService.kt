package com.example.patitasvivas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "PatitasVivasChannel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Maneja el mensaje aquí
        remoteMessage.notification?.let {
            Log.d("FCM", "Título: ${it.title}, Cuerpo: ${it.body}")
            // Mostrar la notificación en la barra de estado
            mostrarNotificacion(it.title, it.body)

        }
    }

    private fun mostrarNotificacion(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java) // Cambia esto a la actividad que desees abrir
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Agrega FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Cambia el icono según tu recurso
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal de notificación (solo para Android O y superiores)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Patitas Vivas Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

}