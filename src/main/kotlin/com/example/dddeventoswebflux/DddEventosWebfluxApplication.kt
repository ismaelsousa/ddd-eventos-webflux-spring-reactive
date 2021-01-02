package com.example.dddeventoswebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class DddEventosWebfluxApplication

fun main(args: Array<String>) {
    runApplication<DddEventosWebfluxApplication>(*args)
}
