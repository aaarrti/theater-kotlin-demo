package com.example.theater.data

import com.example.theater.domain.Booking
import com.example.theater.domain.Performance
import com.example.theater.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface SeatRepository : JpaRepository<Seat, Long> {

    fun findByRowNumberAndSeat(row: Int, seat: Char): Seat

}

@Repository
interface PerformanceRepository: JpaRepository<Performance, Long>

@Repository
interface BookingRepository: JpaRepository<Booking, Long> {
    fun findByPerformanceAndSeat(perf: Performance, seat: Seat): Booking?
}