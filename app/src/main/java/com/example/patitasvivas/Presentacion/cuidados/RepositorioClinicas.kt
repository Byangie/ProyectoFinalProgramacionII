import com.example.patitasvivas.Presentacion.cuidados.ClinicaVeterinaria
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RepositorioClinicas {

    private val db = FirebaseFirestore.getInstance()
    private val clinicasCollection = db.collection("clinicasVeterinarias")

    suspend fun obtenerClinicas(): List<ClinicaVeterinaria> {
        return try {
            clinicasCollection.get().await().toObjects(ClinicaVeterinaria::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun agregarClinica(clinica: ClinicaVeterinaria) {
        try {
            clinicasCollection.add(clinica).await() // Aquí se guarda la clínica en Firebase
        } catch (e: Exception) {
            // Manejo de error
            println("Error al agregar la clínica: ${e.message}")
        }
    }
}
