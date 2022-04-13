package com.example.theater.data


import com.example.theater.domain.Performance
import com.example.theater.service.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener


@Component
class InitDataMigration {

    @Autowired
    lateinit var service: TheaterService
    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    @EventListener(classes = [ApplicationReadyEvent::class])
    fun initDB(){
        service.fillSeatsDB()
        performanceRepository.saveAll(
            listOf(
                Performance(0, "Shrek 3"),
                Performance(0, "Your Mom")
            )
        )
    }

}