package com.example.dddeventoswebflux.events

import com.example.dddeventoswebflux.domain.AwayEquipment
import com.example.dddeventoswebflux.domain.Event
import com.example.dddeventoswebflux.domain.enum.EventType
import com.example.dddeventoswebflux.repository.AwayEquipmentRepository
import com.example.dddeventoswebflux.repository.EventRepository
import com.example.dddeventoswebflux.repository.RouteRepository
import com.example.dddeventoswebflux.util.haversineDistance
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.*

const val geoFence = 0.05
@Component
class EventAwayEquipment(
        private val awayEquipmentRepository: AwayEquipmentRepository,
        private val routeRepository: RouteRepository,
        private val eventRepository: EventRepository
) {
    private val log = LoggerFactory.getLogger(EventAwayEquipment::class.java)
    fun processEvent(notificationMobileDTO: NotificationMobileDTO) {

        val distance = haversineDistance(
                notificationMobileDTO.lastCoordinate.latitude,
                notificationMobileDTO.lastCoordinate.longitude,
                notificationMobileDTO.lastCoordinateMobile.latitude,
                notificationMobileDTO.lastCoordinateMobile.longitude,
        )

        if(distance >= 0.5){
            log.info("Distancia maior q 0.5")
            /*
               Tratar caso em que o motorista está na hora de almoço
               Ou tempo livre
               Baseado na hora que a coordenada foi enviada
            */

            val hourSentCoordinate = notificationMobileDTO.lastCoordinateMobile.`when`.hours
            val scheduleWork = (hourSentCoordinate >= 7 &&  hourSentCoordinate <= 12) || ( hourSentCoordinate >= 14 && hourSentCoordinate<=18) || true
            if( !scheduleWork ){
                log.info("============================")
                log.info("Não está na hora do trabalho")
                log.info("============================\n")
                return
            }

            /*
                Trata caso que esteja longe do caminhão mas é um ponto de parada
                Ai o motorista pode sair do veículo para entregar ou chamar o cliente
            */

             routeRepository.getRouteByEquipment_Id(notificationMobileDTO.lastCoordinate.routeId).map {route ->
                // log.info("pegou a rota")
                route.stops.filter { stop ->
                 //   log.info("está no filtro ${stop.id}")
                    val distanceStop = haversineDistance(
                            stop.latitude,
                            stop.longitude,
                            notificationMobileDTO.lastCoordinate.latitude,
                            notificationMobileDTO.lastCoordinate.longitude,
                    )
                    // Se não está em uma parada
                    distanceStop >= geoFence
                }.map {stop ->
                  log.info("Não está em uma parada ${stop.id}")
                    // Buscar por um equipamento distante
                    //  A partir daqui é necessário criar um alerta

                    //awayEquipmentRepository.getAwayEquipmentByRouteId(notificationMobileDTO.lastCoordinate.routeId)
                    awayEquipmentRepository.getAwayEquipmentByRouteId(notificationMobileDTO.lastCoordinate.routeId).flatMap {away->
                        log.info("Buscou por um equipamento  ${away}")
                        // Se data que awayEquip >= 10 min
                        // NewEvent (Danger)
                        val dateAway = away.`when`

                        val currentDate = Date()


                        if(
                                currentDate.year == dateAway.year
                                &&
                                currentDate.month == dateAway.month
                                &&
                                dateAway.day == dateAway.day
                                &&
                                (currentDate.hours >= dateAway.hours || currentDate.seconds   >= dateAway.seconds )
                        ){
                            if(currentDate.seconds - dateAway.seconds >= 10){ //2:10
                                // throw event
                                log.info("===============================")
                                log.info("|    ALERTA DE PERIGO ENVIADO |")
                                log.info("===============================\n")
                                val eventAway = Event(null, eventType = EventType.AWAY, `when` = Date(), stopId =stop.id )
                                eventRepository.save(eventAway).then()
                            }else{
                                // criar awayEquipment
                                log.info("=============================")
                                log.info("|   POSSIVEL PERIGO - AWAY   |")
                                log.info("=============================\n")
                                val newAwayEquipment = AwayEquipment(null, notificationMobileDTO.lastCoordinate.routeId, `when` = Date()) //2:00
                                awayEquipmentRepository.save(newAwayEquipment).then()
                            }
                        }else{
                            log.info("=============================")
                            log.info("|        NAO CRIOU          |")
                            log.info("=============================\n")
                            mono {  }
                        }
                    }.switchIfEmpty {
                        // criar awayEquipment

                        val newAwayEquipment = AwayEquipment(null, notificationMobileDTO.lastCoordinate.routeId, `when` = Date()) //2:00

                        log.info("=============================")
                        log.info("|   POSSIVEL PERIGO - AWAY   |")
                        log.info("=============================\n")
                        awayEquipmentRepository.save(newAwayEquipment).subscribe()
                        mono {  }
                    }.subscribe()
                }
            }.subscribe()
        }else{
            // Limpar último eventoAway
            awayEquipmentRepository.getAwayEquipmentByRouteId(notificationMobileDTO.lastCoordinate.routeId).map { lastAwayEquipment->
                log.info("================================")
                log.info("| MOTORISTA VOLTOU AO VEÍCULO  |")
                log.info("===============================\n")
                lastAwayEquipment._id?.let { awayEquipmentRepository.deleteById(it).then() }

            }

        }

    }
}


