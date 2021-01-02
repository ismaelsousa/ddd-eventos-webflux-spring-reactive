package com.example.dddeventoswebflux.events

import com.example.dddeventoswebflux.domain.Coordinate
import com.example.dddeventoswebflux.domain.LastCoordinate


data class NotificationDto(
        val coordinate: Coordinate,
        val lastCoordinate: LastCoordinate
)