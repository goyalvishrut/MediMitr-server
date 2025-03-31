package org.example.medimitr.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.example.medimitr.models.NewMedicine
import kotlin.random.Random

fun main() =
    runBlocking {
        val client =
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                        },
                    )
                }
            }

        val baseUrl = "http://localhost:8081/medicines" // Replace with your API endpoint

        try {
            val randomMedicine = generateRandomMedicine()

            val response: NewMedicine =
                client
                    .post(baseUrl) {
                        contentType(ContentType.Application.Json)
                        setBody(randomMedicine)
                    }.body()
            println("Medicine added successfully: $response")
        } catch (e: Exception) {
            println("Error adding medicine: ${e.message}")
            e.printStackTrace()
        } finally {
            client.close()
        }
    }

fun generateRandomMedicine(): NewMedicine {
    val names = listOf("Aspirin", "Ibuprofen", "Paracetamol", "Amoxicillin", "Lisinopril")
    val descriptions =
        listOf("Pain reliever", "Anti-inflammatory", "Fever reducer", "Antibiotic", "Blood pressure medication")
    val imageUrls =
        listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg",
            "https://example.com/image5.jpg",
        )

    return NewMedicine(
        name = names.random(),
        description = descriptions.random(),
        price = Random.nextDouble(5.0, 50.0),
        imageUrl = imageUrls.random(),
    )
}
