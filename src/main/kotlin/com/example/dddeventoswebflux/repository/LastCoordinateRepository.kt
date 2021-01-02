package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.LastCoordinate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface LastCoordinateRepository : ReactiveMongoRepository<LastCoordinate, String>{
    fun getLastCoordinateByEquipment_Id(id:Int): Mono<LastCoordinate>
}