package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.Route
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RouteRepository: CoroutineCrudRepository<Route,Int>{
  suspend fun getRouteByEquipment_Id(id:Int):Route?
}