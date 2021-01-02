package com.example.dddeventoswebflux.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("stop")
data class Stop(
        val id: Int,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        var arrivalAt: Date? = null,
        var departureAt: Date? = null,
)