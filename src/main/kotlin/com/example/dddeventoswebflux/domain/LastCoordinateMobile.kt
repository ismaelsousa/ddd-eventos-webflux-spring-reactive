package com.example.dddeventoswebflux.domain
import org.springframework.data.annotation.Id
import java.util.*
import org.springframework.data.mongodb.core.mapping.Document

@Document("lastCoordinateMobile")
data class LastCoordinateMobile(
        @Id val _id: String? = null,
        val mobileEquipment: Equipment,
        val latitude: Double,
        val longitude: Double,
        val `when`: Date = Date(),
        val routeId: Int
)