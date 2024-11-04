package com.example.patitasvivas.Presentacion.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.patitasvivas.Modelo.DatosUsuario.DatosUsuario
import com.example.patitasvivas.Presentacion.chat.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class ChatViewModel : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // LiveData para el usuario y mensajes
    private val _usuario = MutableLiveData<DatosUsuario?>()
    val usuario: LiveData<DatosUsuario?> get() = _usuario

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    val errorMessage = MutableLiveData<String>()

    // Cargar el nombre del usuario
    fun loadUserName(userId: String) {
        database.child("UsersProfile").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(DatosUsuario::class.java)
                if (usuario != null) {
                    _usuario.value = usuario
                    Log.d("ChatViewModel", "Usuario cargado: ${usuario.email}")
                } else {


                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage.value = "Error al cargar datos de usuario: ${error.message}"
                Log.e("ChatViewModel", "Error: ${error.message}")
            }
        })
    }

    // Cargar mensajes de un chat
    fun loadMessages(chatId: String) {
        database.child("chats").child(chatId).child("messages")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val newMessage = snapshot.getValue(Message::class.java)
                    val updatedList = _messages.value?.toMutableList() ?: mutableListOf()
                    newMessage?.let { updatedList.add(it) }
                    _messages.value = updatedList
                }

                override fun onCancelled(error: DatabaseError) {
                    errorMessage.value = "Error al cargar los mensajes: ${error.message}"
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            })
    }



    // Enviar un mensaje
    fun sendMessage(chatId: String, userId: String, messageText: String) {

        val emailUsuario= Firebase.auth.currentUser?.email ?: "Correo desconocido"
        if (messageText.isNotBlank()) {
            val messageId = database.child("chats").child(chatId).child("messages").push().key
            if (messageId != null) {
                val message = Message(
                    senderId = userId,
                    text = messageText,
                    timestamp = System.currentTimeMillis(),
                    email = emailUsuario
                )

                database.child("chats").child(chatId).child("messages").child(messageId).setValue(message)
                    .addOnSuccessListener {
                        Log.d("ChatViewModel", "Mensaje enviado: $messageText")
                    }
                    .addOnFailureListener { error ->
                        errorMessage.value = "Error al enviar el mensaje: ${error.message}"
                    }
            } else {
                errorMessage.value = "Error al generar el ID del mensaje."
            }
        } else {
            errorMessage.value = "El mensaje no puede estar vacío."
        }
    }

    }
