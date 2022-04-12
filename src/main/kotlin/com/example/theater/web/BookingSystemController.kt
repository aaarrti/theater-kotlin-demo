package com.example.theater.web

import com.example.theater.data.InitDataMigration
import com.example.theater.service.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class BookingSystemController {

    @Autowired
    lateinit var service: TheaterService


    @GetMapping
    fun homePage(): ModelAndView {
        return ModelAndView("seatBooking", "bean", CheckAvailabilityBackingBean())
    }

    @PostMapping("/checkAvailability")
    fun checkAvailability(bean: CheckAvailabilityBackingBean): ModelAndView {
        val res = service.isSeatFree(bean.selectedSeatNum, bean.selectedSeatRow)
        bean.result = "Seat ${bean.selectedSeatNum}-${bean.selectedSeatRow} is " + if (res) "available" else "booked"
        return ModelAndView("seatBooking", "bean", bean)
    }


}

class CheckAvailabilityBackingBean {
    val seatNums = 1..36
    val seatRows = 'A'..'O'
    var selectedSeatNum = 1
    var selectedSeatRow = ' '
    var result = ""
}


