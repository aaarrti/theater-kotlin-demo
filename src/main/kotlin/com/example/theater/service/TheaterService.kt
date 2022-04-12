package com.example.theater.service


import com.example.theater.data.SeatRepository
import com.example.theater.domain.Seat
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class TheaterService {

    val logger = KotlinLogging.logger {  }
    @Autowired
    lateinit var seatRepository: SeatRepository

    fun isSeatFree(row: Int, seat: Char): Boolean{
        return true
    }

    fun initMigration() {
        logger.info("Filling the database")
        val seats = ((1..15).map { row ->
            (1..36).map { column ->
                Seat(
                    id= 0,
                    row_n = row,
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

