package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.Event
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository: CoroutineCrudRepository<Event, String>