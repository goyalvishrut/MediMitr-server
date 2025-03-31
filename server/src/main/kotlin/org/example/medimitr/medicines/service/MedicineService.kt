package org.example.medimitr.medicines.service

import org.example.medimitr.common.roundToTwoDecimalPlaces
import org.example.medimitr.medicines.repo.MedicineRepository
import org.example.medimitr.models.NewMedicine

class MedicineService(
    private val medicineRepository: MedicineRepository,
) {
    suspend fun getAllMedicine() = medicineRepository.getAllMedicines()

    suspend fun createMedicine(newMedicine: NewMedicine) {
        medicineRepository.createMedicine(newMedicine.copy(price = newMedicine.price.roundToTwoDecimalPlaces()))
    }
}
