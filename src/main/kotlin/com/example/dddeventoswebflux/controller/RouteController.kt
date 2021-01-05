package com.example.dddeventoswebflux.controller

import com.example.dddeventoswebflux.domain.Route
import com.example.dddeventoswebflux.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/routes")
class RouteController(
        private val routeRepository: RouteRepository
) {

    @PostMapping
   suspend fun addRoute(@RequestBody route: Route): Route {
        return routeRepository.save(route)
    }

    @PutMapping
    suspend fun updateRoute(@RequestBody route: Route):Route {
        return routeRepository.save(route)
    }

    @DeleteMapping
    suspend  fun deleteRoute(@RequestBody route: Route) {
        return routeRepository.delete(route)
    }

    @GetMapping
    suspend fun getRoutes(): Flow<Route> {
        return routeRepository.findAll()
    }

    @GetMapping("/{id}")
    suspend fun getRouteById(@PathVariable id: Int):Route {
        return routeRepository.findById(id) ?: throw ClassNotFoundException("Route not found")
    }

    @GetMapping("/ByEquipmentId/{id}")
    suspend fun getRouteByEquipmentId(@PathVariable id: Int): Route {
       return routeRepository.getRouteByEquipment_Id(id) ?: throw ClassNotFoundException("Route not found")
    }

}