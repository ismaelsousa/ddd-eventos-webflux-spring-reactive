package com.example.dddeventoswebflux.events

import com.example.dddeventoswebflux.repository.AwayEquipmentRepository
import com.example.dddeventoswebflux.repository.EventRepository
import com.example.dddeventoswebflux.repository.RouteRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EventAwayEquipment(
        private val awayEquipmentRepository: AwayEquipmentRepository,
        private val routeRepository: RouteRepository,
        private val eventRepository: EventRepository
) {
    private val log = LoggerFactory.getLogger(EventAwayEquipment::class.java)
    fun processEvent(notificationMobileDTO: NotificationMobileDTO) {
        log.info("Chegou aqui $notificationMobileDTO")
    }
}