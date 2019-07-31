package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import java.time.LocalDate
import java.time.LocalDateTime

data class VisitOnDate(val visit: Visit, val date: LocalDate) {
    fun isAfter(dateTime: LocalDateTime): Boolean {
        return date.atTime(visit.timeToVisit).isAfter(dateTime)
    }

    fun isFromDay(date : LocalDate) : Boolean {
        return (date.dayOfMonth == this.date.dayOfMonth) and (date.monthValue == this.date.monthValue) and (date.year == this.date.year)
    }
}
