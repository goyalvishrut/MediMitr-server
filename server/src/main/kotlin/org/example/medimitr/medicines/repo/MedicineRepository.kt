package org.example.medimitr.medicines.repo

import org.example.medimitr.models.Medicine
import org.example.medimitr.models.NewMedicine

interface MedicineRepository {
    suspend fun getAllMedicines(): List<Medicine> // Get all medicines

    suspend fun createMedicine(medicine: NewMedicine): Medicine // Create a new medicine
}
