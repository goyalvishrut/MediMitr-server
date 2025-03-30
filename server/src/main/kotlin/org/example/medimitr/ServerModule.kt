package org.example.medimitr

import org.example.medimitr.medicines.repo.MedicineRepository
import org.example.medimitr.medicines.repo.MedicineRepositoryImpl
import org.example.medimitr.medicines.service.MedicineService
import org.example.medimitr.orders.repo.OrderRepository
import org.example.medimitr.orders.repo.OrderRepositoryImpl
import org.example.medimitr.orders.service.OrderService
import org.example.medimitr.user.repo.UserRepository
import org.example.medimitr.user.repo.UserRepositoryImpl
import org.example.medimitr.user.services.UserService
import org.jetbrains.exposed.sql.Database
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun serverModule(database: Database) =
    module {
        single { database } // Provide the pre-connected database instance
        singleOf(::MedicineService)
        singleOf(::UserService)
        singleOf(::OrderService)
        singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
        singleOf(::MedicineRepositoryImpl) { bind<MedicineRepository>() }
        singleOf(::OrderRepositoryImpl) { bind<OrderRepository>() }
    }
