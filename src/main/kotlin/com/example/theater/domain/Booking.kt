package com.example.theater.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val customerName: String
) {
    @ManyToOne
    lateinit var seat: Seat

    @ManyToOne
    lateinit var performance: Performance
}