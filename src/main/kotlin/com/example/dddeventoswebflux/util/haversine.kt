package com.example.dddeventoswebflux.util

import kotlin.math.*

const val earthRadiusKm: Double = 6372.8

fun haversineDistance(latOrigin: Double,
                      lonOrigin: Double,
                      latDestination: Double,
                      lonDestination: Double): Double {
    val dLat = Math.toRadians(latDestination - latOrigin)
    val dLon = Math.toRadians(lonDestination - lonOrigin)
    val originLat = Math.toRadians(latOrigin)
    val destinationLat = Math.toRadians(latDestination)

    val a = sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat)
    val c = 2 * asin(sqrt(a))
    return earthRadiusKm * c
}