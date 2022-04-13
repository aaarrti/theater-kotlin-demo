package com.example.theater.service


import com.example.theater.data.BookingRepository
import com.example.theater.data.PerformanceRepository
import com.example.theater.data.SeatRepository
import com.example.theater.domain.Booking
import com.example.theater.domain.Performance
import com.example.theater.domain.Seat
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class TheaterService {

    val logger = KotlinLogging.logger { }

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    @Autowired
    lateinit var bookingRepository: BookingRepository

    fun checkAvailability(dto: CheckBookingDTO): CheckBookingDTO {
        val seatEntity: Seat = seatRepository.findByRowNumberAndSeat(dto.selectedSeatRow, dto.selectedSeatNum)
        val booking: Booking? = bookingRepository.findByPerformanceAndSeat(dto.selectedPerformance!!, seatEntity)
        dto.performance = dto.selectedPerformance?.title
        dto.performances = performanceRepository.findAll()
        dto.seat = seatEntity
        if (booking == null) {
            dto.available = true
        } else {
            dto.available = false
            dto.booking = booking
        }
        return dto
    }

    fun getInitDTO(): CheckBookingDTO {
        val performanse = performanceRepository.findAll()
        return CheckBookingDTO(performances = performanse)
    }

    fun doBook(dto: CheckBookingDTO): BookingConfirmationDTO {
        val booking = Booking(0, dto.customerName)
        booking.seat = dto.seat!!
        booking.performance = performanceRepository.findByTitle(dto.performance!!)
        bookingRepository.save(booking)
        val dto = BookingConfirmationDTO(booking.seat, booking.performance, booking.customerName)
        return dto
    }

    fun fillSeatsDB() {
        logger.info("Filling the database")
        val seats = ((1..15).map { row ->
            ('A'..'Z').map { column ->
                Seat(
                    id = 0,
                    rowNumber = row,
                    seat = column,
                    price = calcPrice(row, column),
                    description = getDescription(row, column)
                )
            }
        }).flatten()
        seatRepository.saveAll(seats)
    }

    private fun calcPrice(row: Int, column: Char) = calcPrice(row, column - 'B')

    private fun calcPrice(row: Int, column: Int) = when {
        // 2 last rows
        row >= 14 -> BigDecimal(14.5)
        column in arrayOf(1, 2, 3, 34, 35, 36) -> BigDecimal(16.5)
        row == 1 || row == 2 -> BigDecimal(21)
        else -> BigDecimal(18)
    }

    private fun getDescription(row: Int, column: Char) = getDescription(row, column - 'B')

    private fun getDescription(row: Int, column: Int) = when {
        // 2 lat rows
        row == 15 -> "Back row"
        row == 14 -> "Cheaper seat"
        column in arrayOf(1, 2, 3, 34, 35, 36) -> "Restricted view"
        row == 1 || row == 2 -> "Best view"
        else -> "Standard SEAT"
    }

}


class CheckBookingDTO(
    val seatRows: IntRange = 1..15,
    val seatNums: CharRange = 'A'..'Z',
    var performances: List<Performance>?,
    var selectedSeatRow: Int = 1,
    var selectedSeatNum: Char = 'A',
    var selectedPerformance: Performance? = performances?.get(0),
    var available: Boolean? = null,
    var booking: Booking = Booking(0, ""),
    var seat: Seat? = null,
    var performance: String? = null,
    var customerName: String = ""
)

class BookingConfirmationDTO(val seat: Seat, val performance: Performance, val customerName: String)







