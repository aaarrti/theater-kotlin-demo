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
        dto.available = booking == null
        //dto.seat = seatEntity
        dto.performance = dto.selectedPerformance?.title
        dto.performances = performanceRepository.findAll()
        return dto
    }

    fun getInitDTO(): CheckBookingDTO {
        val performanse = performanceRepository.findAll()
        return CheckBookingDTO(performances = performanse)
    }

    fun fillSeatsDB() {
        logger.info("Filling the database")
        val seats = ((1..15).map { row ->
            (1..36).map { column ->
                Seat(
                    id = 0,
                    rowNumber = row,
                    seat = (column + 64).toChar(),
                    price = calcPrice(row, column),
                    description = getDescription(row, column)
                )
            }
        }).flatten()
        seatRepository.saveAll(seats)
    }

    fun calcPrice(row: Int, column: Int) = when {
        // 2 lat rows
        row >= 14 -> BigDecimal(14.5)
        column in arrayOf(1, 2, 3, 34, 35, 36) -> BigDecimal(16.5)
        row == 1 || row == 2 -> BigDecimal(21)
        else -> BigDecimal(18)
    }


    fun getDescription(row: Int, column: Int) = when {
        // 2 lat rows
        row == 15 -> "Back row"
        row == 14 -> "Cheaper seat"
        column in arrayOf(1, 2, 3, 34, 35, 36) -> "Restricted view"
        row == 1 || row == 2 -> "Best view"
        else -> "Standard SEAT"
    }

}


open class CheckBookingDTO(
    val seatRows: IntRange = 1..36,
    val seatNums: CharRange = 'A'..'O',
    var performances: List<Performance>?,
    var selectedSeatRow: Int = 1,
    var selectedSeatNum: Char = 'A',
    var selectedPerformance: Performance? = performances?.get(0),
    var available: Boolean = false,
    var booking: Booking = Booking(0, ""),
    var seat: Seat? = null,
    var performance: String? = null,
    var customerName: String = ""
)





