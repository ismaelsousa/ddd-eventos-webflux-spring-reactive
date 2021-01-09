package com.example.dddeventoswebflux.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("awayEquipment")
data class AwayEquipment(
        @Id val _id:String?=null,
        val routeId: Int,
        val `when`: Date=Date()
)