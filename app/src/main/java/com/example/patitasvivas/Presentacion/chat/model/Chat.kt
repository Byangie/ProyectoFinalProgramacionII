package com.example.patitasvivas.Presentacion.chat.model

data class Chat(val participants: List<String> = listOf(),  // Lista de IDs de los participantes en el chat
                val lastMessage: String = "",               // Último mensaje enviado
                val timestamp: Long = 0                     // Marca de tiempo del último mensaje
)
