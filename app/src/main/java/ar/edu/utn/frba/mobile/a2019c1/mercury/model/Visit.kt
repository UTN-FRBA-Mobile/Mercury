package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import java.time.LocalDate
import java.time.LocalTime

data class Visit(val client : Client, val timeToVisit: LocalTime, val visitDates: MutableList<LocalDate> = mutableListOf()) {
    fun addVisitOnDate(date: LocalDate) {
        visitDates.add(date)
    }
}