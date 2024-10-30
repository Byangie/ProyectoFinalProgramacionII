package com.example.patitasvivas.Presentacion.chat.model

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    var email: String = "" // Asegúrate de que sea var
)
