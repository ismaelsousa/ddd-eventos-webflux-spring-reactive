package com.example.dddeventoswebflux.events

import com.example.dddeventoswebflux.domain.Coordinate
import com.example.dddeventoswebflux.domain.Event
import com.example.dddeventoswebflux.domain.Route
import com.example.dddeventoswebflux.domain.Stop
import com.example.dddeventoswebflux.domain.enum.EventType
import com.example.dddeventoswebflux.repository.EventRepository
import com.example.dddeventoswebflux.repository.RouteRepository
import com.example.dddeventoswebflux.util.haversineDistance

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.*

@Component
class EventArrival(
        private val eventRepository: EventRepository,
        private val routeRepository: RouteRepository
) : IEventsProcessor {

    private val log = LoggerFactory.getLogger(EventArrival::class.java)

    /**
     * para detectar se o motorista chegou em algum cliente precisamos saber se
     * 1. esta dentro do raio de atendimento e
     * 2. se essa e a coordenada anterior são iguais
     */
    override fun processCoordinate(notificationDto: NotificationDto) {
        val lastCoordinate = notificationDto.lastCoordinate
        val coordinate = notificationDto.coordinate
        if (lastCoordinate.latitude == coordinate.latitude && lastCoordinate.longitude == coordinate.longitude) {

            routeRepository.getRouteByEquipment_Id(notificationDto.coordinate.equipmentId)
                    .map {route-> Pair(filterListStops(coordinate = coordinate, stops = route.stops).toFlux(), route)}
                    .flatMap { pair->
                        pair.first.flatMap { stop ->
                            arrivedStopRoute(pair.second,stop)
                                    .flatMap{
                                        registerEvent(EventType.ARRIVE, stop.id)
                                    }

                        }.then()
                    }.subscribe()

        }
    }
    
    private fun arrivedStopRoute(route:Route, oldStop:Stop): Mono<Route> {
        val updatedStop = oldStop.copy(arrivalAt = Date())
        val indexOf = route.stops.indexOf(oldStop)
        route.stops.removeAt(indexOf)
        route.stops.add(updatedStop)
        return routeRepository.save(route)
    }

    private fun registerEvent(eventType: EventType, stopId: Int):Mono<Event>{
        log.info("Driver Arrival on Stop [{}]", stopId)
        val newEvent = Event(eventType = eventType, `when` = Date(), stopId = stopId)
        return  eventRepository.save(newEvent)
    }

    private fun filterListStops(coordinate: Coordinate, stops: List<Stop>) = stops.filter {stop->
        val distance = haversineDistance(stop.latitude, stop.longitude, coordinate.latitude, coordinate.longitude)
        stop.arrivalAt == null && distance <= geofence
    }

}

