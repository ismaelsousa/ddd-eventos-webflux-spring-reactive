package com.example.dddeventoswebflux.domain

import java.util.*

class Coordinate(
        val equipmentId: Int,
        val latitude: Double,
        val longitude: Double,
        val datePing: Date
){

    override fun toString(): String {
        return "{ equipmentId: $equipmentId, latitude: $latitude, longitude: $longitude, dateping: $datePing }"
    }

}