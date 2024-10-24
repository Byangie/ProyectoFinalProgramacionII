import com.example.patitasvivas.Presentacion.cuidados.ConsejoCuidado
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

internal class RepositorioConsejos {
    private val db = FirebaseFirestore.getInstance()
    private val consejosCollection = db.collection("consejosCuidado")

    // Función suspendida que obtiene una lista de consejos
    suspend fun obtenerConsejos(): List<ConsejoCuidado> {
        return try {
            consejosCollection.get().await().toObjects(ConsejoCuidado::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
