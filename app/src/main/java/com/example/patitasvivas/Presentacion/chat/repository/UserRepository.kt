package com.example.patitasvivas.Presentacion.chat.repository


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun registerUser(userId: String, nombre: String) {
        val userProfile = mapOf(
            "nombre" to nombre
        )

        db.child("UsersProfile").child(userId).setValue(userProfile)

                .addOnSuccessListener {
                    // Registro exitoso
                }
                    .addOnFailureListener { e ->
                        // Manejo de errores
                    }
            }
        }

