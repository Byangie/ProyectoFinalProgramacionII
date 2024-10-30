package com.example.patitasvivas.Presentacion.chat.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patitasvivas.Presentacion.chat.viewmodel.ChatViewModel
import com.example.patitasvivas.Presentacion.chat.model.Message
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(chatId: String, viewModel: ChatViewModel = viewModel()) {
    val messages by viewModel.loadMessages(chatId).observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState("")
    var messageText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Mostrar los mensajes
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                MessageItem(message)
            }
        }

        // Mostrar mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Campo de texto para escribir un mensaje
        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
            )

            Button(
                onClick = {
                    viewModel.sendMessage(chatId, messageText)
                    messageText = "" // Limpiar el campo de texto después de enviar
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(message.timestamp))

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = message.userName, style = MaterialTheme.typography.bodySmall)
        Text(text = message.text, style = MaterialTheme.typography.bodyLarge)
        Text(text = formattedTime, style = MaterialTheme.typography.bodySmall) // Mostrar la hora
    }
}
