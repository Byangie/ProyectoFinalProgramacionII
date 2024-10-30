package com.example.patitasvivas.Presentacion.chat.repository


import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(userId: String, nombre: String) {
        val userProfile = hashMapOf(
            "nombre" to nombre // Asegúrate de que 'nombre' contenga el valor correcto.
        )

        db.collection("UsersProfile").document(userId).set(userProfile)
            .addOnSuccessListener {
                // Registro exitoso
            }
            .addOnFailureListener { e ->
                // Manejo de errores
            }
    }
}
