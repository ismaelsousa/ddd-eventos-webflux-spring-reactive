package com.example.dddeventoswebflux.events

import com.example.dddeventoswebflux.domain.LastCoordinate
import com.example.dddeventoswebflux.domain.LastCoordinateMobile

data class NotificationMobileDTO(
        val lastCoordinateMobile: LastCoordinateMobile,
        val lastCoordinate: LastCoordinate
)