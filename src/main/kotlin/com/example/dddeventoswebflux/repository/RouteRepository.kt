package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.Route
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface RouteRepository: ReactiveMongoRepository<Route,Int>{
    fun getRouteByEquipment_Id(id:Int):Mono<Route>
}