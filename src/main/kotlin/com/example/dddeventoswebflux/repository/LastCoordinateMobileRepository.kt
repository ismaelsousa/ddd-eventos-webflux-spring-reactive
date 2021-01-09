package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.LastCoordinateMobile
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface LastCoordinateMobileRepository: ReactiveMongoRepository<LastCoordinateMobile, String> {
    fun getLastCoordinateMobileByMobileEquipment_Id(id:Int): Mono<LastCoordinateMobile>
}
