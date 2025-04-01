package org.example.medimitr.medicines

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.example.medimitr.medicines.service.MedicineService
import org.example.medimitr.models.NewMedicine
import org.koin.ktor.ext.inject

fun Application.configureMedicineRouting() {
    val medicineService by inject<MedicineService>()

    routing {
        authenticate {
            get("/medicines") {
                // GET /medicines endpoint
                val medicines = medicineService.getAllMedicine() // Get all medicines
                call.respond(medicines) // Respond with medicine list
            }

            post("/medicines") {
                // POST /medicines endpoint
                val newMedicine = call.receive<NewMedicine>() // Receive new medicine data
                val medicine = medicineService.createMedicine(newMedicine) // Create medicine
                call.respond(medicine) // Respond with created medicine
            }
        }
    }
}
