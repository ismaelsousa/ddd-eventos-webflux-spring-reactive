package com.example.dddeventoswebflux.stream

import com.example.dddeventoswebflux.domain.Coordinate
import com.example.dddeventoswebflux.domain.createLastCoordinate
import com.example.dddeventoswebflux.repository.LastCoordinateRepository
import com.example.dddeventoswebflux.repository.RouteRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

import com.example.dddeventoswebflux.events.EventArrival
import com.example.dddeventoswebflux.events.NotificationDto

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

@Component
class CoordinateProcessor(
        private val routeRepository: RouteRepository,
        private val lastCoordinateRepository: LastCoordinateRepository,
        private val eventArrival: EventArrival,
) {

    private val log = LoggerFactory.getLogger(CoordinateProcessor::class.java)

    fun receiveCoordinate(coordinate: Coordinate): Mono<NotificationDto> {
        log.info("Coordinate received: [{}]", coordinate)
        return  routeRepository.getRouteByEquipment_Id(coordinate.equipmentId)
                .flatMap { route->
                    lastCoordinateRepository.getLastCoordinateByEquipment_Id(coordinate.equipmentId)
                            .flatMap { lastCoordinate->
                                val updatedLastCoordinate = lastCoordinate.copy(latitude = coordinate.latitude, longitude = coordinate.longitude, `when` = coordinate.datePing)
                                lastCoordinateRepository.save(updatedLastCoordinate)
                            }.switchIfEmpty{
                                val newLasCoordinate = createLastCoordinate(coordinate.equipmentId, coordinate.latitude, coordinate.longitude, route)
                                lastCoordinateRepository.save(newLasCoordinate)
                            }
                }.map { lastCoordinate ->
                    NotificationDto(coordinate, lastCoordinate)
                }
    }

    @Scheduled(fixedDelay = 100000, initialDelay = 10000)
    fun consumeCoordinates(){
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val jsonContent = "[{\"equipmentId\":10000,\"latitude\":-3.752414,\"longitude\":-38.511576,\"datePing\":1599904800000},{\"equipmentId\":10000,\"latitude\":-3.752526,\"longitude\":-38.512504,\"datePing\":1599904920000},{\"equipmentId\":10000,\"latitude\":-3.752581,\"longitude\":-38.513015,\"datePing\":1599905040000},{\"equipmentId\":10000,\"latitude\":-3.75262,\"longitude\":-38.513433,\"datePing\":1599905160000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905220000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905280000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905340000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905400000},{\"equipmentId\":10000,\"latitude\":-3.752696,\"longitude\":-38.513927,\"datePing\":1599905520000},{\"equipmentId\":10000,\"latitude\":-3.7526674,\"longitude\":-38.5149107,\"datePing\":1599905580000},{\"equipmentId\":10000,\"latitude\":-3.7522729,\"longitude\":-38.5162625,\"datePing\":1599905640000},{\"equipmentId\":10000,\"latitude\":-3.751894,\"longitude\":-38.515098,\"datePing\":1599905700000},{\"equipmentId\":10000,\"latitude\":-3.750796,\"longitude\":-38.515302,\"datePing\":1599905760000},{\"equipmentId\":10000,\"latitude\":-3.749769,\"longitude\":-38.515491,\"datePing\":1599905820000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599905880000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599905940000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906000000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906060000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906120000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906180000},{\"equipmentId\":10000,\"latitude\":-3.748932,\"longitude\":-38.515718,\"datePing\":1599906240000},{\"equipmentId\":10000,\"latitude\":-3.748671,\"longitude\":-38.516117,\"datePing\":1599906300000},{\"equipmentId\":10000,\"latitude\":-3.748641,\"longitude\":-38.516856,\"datePing\":1599906360000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906480000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906540000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906600000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906660000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906720000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599906960000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599907020000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599907080000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599907140000}]"

        val myRoute = "[" +
                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0720683," +
                "\"longitude\":-37.9894671," +
                "\"datePing\":1599905040000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905040000}," +

                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0724181," +
                "\"longitude\":-37.9897523," +
                "\"datePing\":1599905160000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905160000}," +

                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0730062," +
                "\"longitude\":-37.9900684," +
                "\"datePing\":1599905220000" +
                "}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905220000" +
                "}," +



                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0737474," +
                "\"longitude\":-37.9903889," +
                "\"datePing\":1599905280000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905280000}," +


                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0747838," +
                "\"longitude\":-37.9908696," +
                "\"datePing\":1599905340000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905340000}," +



                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0751817," +
                "\"longitude\":-37.9900618," +
                "\"datePing\":1599905400000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905400000}," +


                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0757217," +
                "\"longitude\":-37.9889622," +
                "\"datePing\":1599905520000}," +

                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905520000}," +



                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.076194," +
                "\"longitude\":-37.9879722," +
                "\"datePing\":1599905580000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905580000}," +


                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\": -5.0765525," +
                "\"longitude\":-37.9872676," +
                "\"datePing\":1599905640000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905640000}," +


                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0769001," +
                "\"longitude\":-37.9864928," +
                "\"datePing\":1599905700000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905700000}," +



                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0773199," +
                "\"longitude\":-37.9857663," +
                "\"datePing\":1599905760000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0705261," +
                "\"longitude\":-37.9918204," +
                "\"datePing\":1599905760000}," +



                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0774861," +
                "\"longitude\":-37.9851802," +
                "\"datePing\":1599905820000}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0774861," +
                "\"longitude\":-37.9851802," +
                "\"datePing\":1599905820000}," +



                "{" +
                "\"equipmentId\":10000," +
                "\"latitude\":-5.0775801," +
                "\"longitude\":-37.9848137," +
                "\"datePing\":1599905880000" +
                "}," +
                "{" +
                "\"equipmentId\":13," +
                "\"latitude\":-5.0775801," +
                "\"longitude\":-37.9848137," +
                "\"datePing\":1599905880000" +
                "}" +
                "]"

        Flux.fromIterable( mapper.readValue(myRoute, Array<Coordinate>::class.java).asList())
                .delayElements(Duration.ofMillis(500))
                .flatMap { coordinate->receiveCoordinate(coordinate) }
                .map { notificationDto -> eventArrival.processCoordinate(notificationDto) }
                .subscribe()
    }
}