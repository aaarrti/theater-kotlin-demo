package com.example.theater.web


import com.example.theater.service.CheckBookingDTO
import com.example.theater.service.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/bookings")
class BookingSystemController {

    @Autowired
    lateinit var service: TheaterService

    @GetMapping
    fun root(): ModelAndView {
        val dto = service.getInitDTO()
        return ModelAndView("seatBooking", "bean", dto)
    }


    @PostMapping("/check-availability")
    fun checkAvailability(dto: CheckBookingDTO): ModelAndView {
        val res = service.checkAvailability(dto)
        return ModelAndView("seatBooking", "bean", res)
    }

    @PostMapping("/book")
    fun book(dto: CheckBookingDTO) : ModelAndView{
        val res = service.doBook(dto)
        return ModelAndView("bookingConfirmed", "booking", res)
    }


}




