package org.example.medimitr.medicines.repo

import org.example.medimitr.models.Medicine
import org.example.medimitr.models.Medicines
import org.example.medimitr.models.NewMedicine
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MedicineRepositoryImpl : MedicineRepository {
    override suspend fun getAllMedicines(): List<Medicine> =
        transaction {
            // Run in a transaction
            Medicines.selectAll().map { row ->
                // Select all medicines
                Medicine(
                    row[Medicines.id],
                    row[Medicines.name],
                    row[Medicines.description],
                    row[Medicines.price],
                    row[Medicines.imageUrl],
                ) // Map to Medicine objects
            }
        }

    override suspend fun createMedicine(medicine: NewMedicine): Medicine =
        transaction {
            // Run in a transaction
            Medicines
                .insert {
                    // Insert into Medicines table
                    it[name] = medicine.name // Set name
                    it[description] = medicine.description // Set description
                    it[price] = medicine.price // Set price
                    it[imageUrl] = medicine.imageUrl // Set image URL
                }.resultedValues!!
                .first()
                .let { row ->
                    // Get the inserted row
                    Medicine(
                        row[Medicines.id],
                        row[Medicines.name],
                        row[Medicines.description],
                        row[Medicines.price],
                        row[Medicines.imageUrl],
                    ) // Map to Medicine object
                }
        }
}
