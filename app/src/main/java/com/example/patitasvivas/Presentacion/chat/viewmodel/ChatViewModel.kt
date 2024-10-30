package com.example.patitasvivas.Presentacion.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.patitasvivas.Presentacion.chat.model.Message

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Función para enviar un mensaje
    fun sendMessage(chatId: String, messageText: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Verifica si el usuario está autenticado
        if (currentUser != null) {
            val userId = currentUser.uid
            loadUserName(userId) { nombre -> // Llama a la función para obtener el nombre
                val message = hashMapOf(
                    "senderId" to userId,
                    "text" to messageText,
                    "timestamp" to System.currentTimeMillis(),
                    "nombre" to nombre // Incluye el nombre en el mensaje
                )

                // Agrega el mensaje a la subcolección de mensajes dentro del chat
                db.collection("chats").document(chatId)
                    .collection("messages").add(message)
                    .addOnSuccessListener {
                        // Actualiza el último mensaje en la colección de chats
                        db.collection("chats").document(chatId)
                            .update(
                                mapOf(
                                    "lastMessage" to messageText,
                                    "timestamp" to System.currentTimeMillis()
                                )
                            )
                    }
                    .addOnFailureListener {
                        // Maneja errores al enviar el mensaje
                        _errorMessage.value = "Error al enviar el mensaje: ${it.message}"
                    }
            }
        } else {
            // Manejo del caso donde el usuario no está autenticado
            _errorMessage.value = "Error: Usuario no autenticado"
        }
    }

    // Función para obtener mensajes en tiempo real

    fun loadMessages(chatId: String): LiveData<List<Message>> {
        val messages = MutableLiveData<List<Message>>()

        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _errorMessage.value = "Error al cargar mensajes: ${e.message}"
                    return@addSnapshotListener
                }

                val messageList = mutableListOf<Message>()
                val docs = snapshot?.documents ?: emptyList()

                for (doc in docs) {
                    val message = doc.toObject(Message::class.java)
                    message?.let { msg ->
                        messageList.add(msg)
                    }
                }

                val userNames = mutableMapOf<String, String>()
                val totalMessages = messageList.size
                var messagesLoaded = 0

                if (totalMessages == 0) {
                    messages.value = messageList
                    return@addSnapshotListener
                }

                for (msg in messageList) {
                    loadUserName(msg.senderId) { userName ->
                        userNames[msg.senderId] = userName
                        msg.userName = userName
                        messagesLoaded++


                        if (messagesLoaded == totalMessages) {
                            messages.value = messageList
                        }
                    }
                }
            }

        return messages
    }
    // Nueva función para cargar el nombre de usuario

    fun loadUserName(userId: String, callback: (String) -> Unit) {
        Log.d("ChatViewModel", "Accediendo a documento: UsersProfile/$userId")
        db.collection("UsersProfile").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("nombre")
                    if (userName != null) {
                        callback(userName)
                    } else {
                        callback("Nombre no encontrado")
                    }
                } else {
                    callback("Documento no encontrado")
                }
            }}


                // Función para cargar el correo electrónico
    fun loadUserEmail(userId: String, callback: (String) -> Unit) {
        db.collection("UsersProfile").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val email = document.getString("serviciosOfrecidos") ?: "Correo no encontrado"
                    callback(email)
                } else {
                    callback("Documento no encontrado")
                }
            }
            .addOnFailureListener { e ->
                callback("Error al cargar el correo: ${e.message}")
            }
    }

    // Crear nuevo chat si no existe
    fun createChat(participants: List<String>, initialMessage: String) {
        val chatData = hashMapOf(
            "participants" to participants,
            "lastMessage" to initialMessage,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("chats").add(chatData)
            .addOnSuccessListener { documentReference ->
                val chatId = documentReference.id
                sendMessage(chatId, initialMessage)
            }
            .addOnFailureListener {
                _errorMessage.value = "Error al crear el chat: ${it.message}"
            }
    }
}
