package com.example.dddeventoswebflux.controller

import com.example.dddeventoswebflux.domain.Route
import com.example.dddeventoswebflux.repository.RouteRepository


import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/routes")
class RouteController(
        private val routeRepository: RouteRepository
) {

    @PostMapping
    fun addRoute(@RequestBody route: Route): Mono<Route> {
        return routeRepository.save(route)
    }

    @PutMapping
    fun updateRoute(@RequestBody route: Route): Mono<Route> {
        return routeRepository.save(route)
    }

    @DeleteMapping
    fun deleteRoute(@RequestBody route: Route): Mono<Void> {
        return routeRepository.delete(route)
    }

    @GetMapping
    fun getRoutes(): Flux<Route> {
        return routeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getRouteById(@PathVariable id: Int): Mono<Route> {

        return routeRepository.findById(id)
                .switchIfEmpty(Mono.error(ClassNotFoundException("Route not found")))

    }

    @GetMapping("/ByEquipmentId/{id}")
    fun getRouteByEquipmentId(@PathVariable id: Int): Mono<Route> {
       return routeRepository.getRouteByEquipment_Id(id)
               .switchIfEmpty(Mono.error(ClassNotFoundException("Route not found")))
    }

}