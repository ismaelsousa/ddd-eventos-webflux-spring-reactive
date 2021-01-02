package com.example.dddeventoswebflux.repository

import com.example.dddeventoswebflux.domain.Event
import org.springframework.data.mongodb.repository.ReactiveMongoRepository


interface EventRepository: ReactiveMongoRepository<Event, String>