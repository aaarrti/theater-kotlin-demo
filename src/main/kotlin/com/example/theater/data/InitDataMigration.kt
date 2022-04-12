package com.example.theater.data


import com.example.theater.service.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener


@Component
class InitDataMigration {

    @Autowired
    lateinit var service: TheaterService

    @EventListener(classes = [ApplicationReadyEvent::class])
    fun initDB() = service.initMigration()

}