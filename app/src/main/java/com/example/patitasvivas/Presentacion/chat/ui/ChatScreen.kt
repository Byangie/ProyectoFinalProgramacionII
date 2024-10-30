package com.example.patitasvivas.Presentacion.chat.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patitasvivas.Modelo.DatosUsuario.DatosUsuario
import com.example.patitasvivas.Presentacion.chat.viewmodel.ChatViewModel
import com.example.patitasvivas.Presentacion.chat.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(chatId: String, userId: String, viewModel: ChatViewModel = viewModel()) {
    val messages by viewModel.messages.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState("")
    val usuario by viewModel.usuario.observeAsState(DatosUsuario())

    LaunchedEffect(userId) {
        viewModel.loadUserName(userId)
        viewModel.loadMessages(chatId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Título y Slogan
        Text(
            text = "CHAT PATITAS VIVAS",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            color = Color(0xFF6200EA),
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Text(
            text = "Conectando amigos peludos y humanos",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { message ->
                MessageItem(message = message)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Espacio para el input
        Firebase.auth.currentUser?.let {
            MessageInput(viewModel, chatId, it.uid)
        }
    }
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(viewModel: ChatViewModel, chatId: String, userId: String) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text("Escribe un mensaje...", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.Black), // Establecer el color del texto aquí
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .border(BorderStroke(1.dp, Color.Black))
                .height(60.dp) // Aumentar la altura del TextField
        )

        Column(
            modifier = Modifier
                .padding(start = 8.dp) // Espacio a la izquierda
                .align(Alignment.CenterVertically) // Alinear el botón
        ) {
            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(chatId, userId, messageText)
                        messageText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA) // Cambiar el color del botón a un color destacado (morado)
                ),
                modifier = Modifier
                    .padding(bottom = 50.dp) // Espacio adicional para elevar el botón
                    .width(90.dp) // Ancho fijo para el botón
            ) {
                Text("Enviar", color = Color.White) // Color del texto del botón
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(message.timestamp))

    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val backgroundColor = if (isCurrentUser) Color(0xFF81D4FA) else Color(0xFF6200EA)
    val textColor = if (isCurrentUser) Color.Black else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .background(backgroundColor, MaterialTheme.shapes.medium)
                .border(BorderStroke(1.dp, Color.LightGray), MaterialTheme.shapes.medium)
                .padding(7.dp)
                .widthIn(max = 200.dp) // Ancho máximo del cuadro
        ) {
            Text(
                text = message.email,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = textColor
            )
        }
    }
}
