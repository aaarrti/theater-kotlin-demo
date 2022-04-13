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

    private val BEST_VIEW_ROWS = listOf(1, 2)
    private val LAST_ROW = 15
    private val PRE_LAST_ROW = 14
    private val RESTRICTED_VIEW_SEATS = arrayOf('A', 'B', 'C', 'X', 'Y', 'Z')

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

    private fun calcPrice(row: Int, column: Char) = when {
        // 2 last rows
        row == LAST_ROW || row == PRE_LAST_ROW -> BigDecimal(14.5)
        column in RESTRICTED_VIEW_SEATS -> BigDecimal(16.5)
        row in BEST_VIEW_ROWS -> BigDecimal(21)
        else -> BigDecimal(18)
    }


    private fun getDescription(row: Int, column: Char) = when {
        // 2 lat rows
        row == LAST_ROW -> "Back row"
        row == PRE_LAST_ROW -> "Cheaper seat"
        column in RESTRICTED_VIEW_SEATS  -> "Restricted view"
        row in BEST_VIEW_ROWS -> "Best view"
        else -> "Standard seat"
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







