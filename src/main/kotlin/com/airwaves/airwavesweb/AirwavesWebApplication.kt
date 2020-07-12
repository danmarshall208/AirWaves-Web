package com.airwaves.airwavesweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AirwavesWebApplication

fun main(args: Array<String>) {
    runApplication<AirwavesWebApplication>(*args)
}