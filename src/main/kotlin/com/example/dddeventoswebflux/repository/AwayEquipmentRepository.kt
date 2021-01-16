package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.AwayEquipment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface AwayEquipmentRepository:ReactiveMongoRepository<AwayEquipment, String> {
    fun getTopAwayEquipmentByRouteId(id:Int): Mono<AwayEquipment>
    fun getAwayEquipmentByRouteId(id:Int): Mono<AwayEquipment>
}