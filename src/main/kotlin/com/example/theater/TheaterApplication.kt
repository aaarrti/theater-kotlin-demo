package com.example.theater

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class TheaterApplication

fun main(args: Array<String>) {
    runApplication<TheaterApplication>(*args)
    val log = KotlinLogging.logger { }
    log.info(
        """
---------------------------------------------------------------------------------------------------------------
Application running!
Visit localhost:8080
---------------------------------------------------------------------------------------------------------------
		"""
    )

}


