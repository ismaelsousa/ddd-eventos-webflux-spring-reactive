package com.example.dddeventoswebflux.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("route")
data class Route(
        @Id val id: Int,
        val equipment: Equipment,
        val mobileEquipment: Equipment,
        val name: String,
        val stops: MutableList<Stop>,
        val datePlan: Date
)