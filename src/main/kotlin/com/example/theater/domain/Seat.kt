package com.example.theater.domain


import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class Seat (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val row_n: Int,
    val seat: Char,
    val price: BigDecimal,
    val description: String,
    val booked: Boolean = false
) {

    override fun toString(): String = "Seat $row_n-$seat $$price ($description)"
}

