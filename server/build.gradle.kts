plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    alias(libs.plugins.kotlinSerialization)
}

group = "org.example.medimitr"
version = "1.0.0"
application {
    mainClass.set("org.example.medimitr.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {

    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
//    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    // Ktor server dependencies for core functionality and Netty engine

    // JSON serialization support
    implementation(libs.ktor.ktor.server.content.negotiation) // Enables content negotiation
    implementation(libs.ktor.ktor.serialization.kotlinx.json) // JSON serialization with Kotlinx

    // Authentication support with JWT
    implementation(libs.ktor.server.auth) // Base authentication module
    implementation(libs.ktor.server.auth.jwt) // JWT-specific authentication module

    // Exposed library for SQL operations
    implementation(libs.exposed.core) // Core Exposed library
    implementation(libs.exposed.jdbc) // JDBC support for Exposed
    implementation(libs.exposed.java.time) // Java time support for Exposed

    // SQL Server JDBC driver
//    implementation(libs.mssql.jdbc) // Driver to connect to SQL Server
    implementation(libs.mysql.connector.java) // MySQL JDBC driver

    // Koin for dependency injection
    implementation(libs.koin.ktor) // Koin integration with Ktor
    implementation(libs.koin.core) // Core Koin library

    // JWT library for token generation
    implementation(libs.java.jwt) // Library for creating and verifying JWTs

    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
    implementation(libs.jbcrypt)

    implementation(libs.ktor.ktor.client.cio) // CIO engine
    implementation(libs.ktor.client.content.negotiation)
}
