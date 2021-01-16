package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.Route
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*

interface RouteRepository: ReactiveMongoRepository<Route,Int>{
    fun getRouteByEquipment_Id(id:Int):Mono<Route>
    fun getRouteByMobileEquipment_Id(equipmentId:Int): Mono<Route>
}