package com.example.dddeventoswebflux.events


const val geofence = 0.2 // 200 meters

interface IEventsProcessor {

    fun processCoordinate(notificationDto: NotificationDto)
}