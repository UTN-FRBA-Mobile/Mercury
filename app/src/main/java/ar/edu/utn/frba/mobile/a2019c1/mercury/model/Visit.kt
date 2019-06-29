package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Visit(val client : Client, val timeToVisit: LocalTime, val visitDates: MutableList<LocalDate> = mutableListOf()) {
    fun addVisitOnDate(date: LocalDate) {
        visitDates.add(date)
    }

    fun disableVisitDatesAfter(disableTime: LocalDateTime) {
        visitDates.removeIf { it.atTime(timeToVisit).isAfter(disableTime) }
    }

    fun nextVisitDate(date: LocalDate): LocalDate? {
        // There should be no more than one possible next visit date. If there are, the first one is picked
        return visitDates.sortedBy { it }
            .firstOrNull { it.isEqual(date) || it.isAfter(date) }
    }

    companion object {
        fun buildFromDatabase(map: HashMap<String, Any>): Visit {
            val hashMapClient = map.get("client") as HashMap<String, Any>
            val client = Client.buildFromDatabase(hashMapClient)
            val hashMapTimeToVisit = map.get("timeToVisit") as HashMap<String, Any>
            val timeToVisit = buildTimeToVisit(hashMapTimeToVisit)
            val hashMapVisitDates = map.get("hashMapVisitDates") as MutableList<HashMap<String, Any>>? ?: mutableListOf()
            val visitDates = hashMapVisitDates.map { buildVisitDate(it) } .toMutableList()
            return Visit(client, timeToVisit, visitDates)
        }

        private fun buildVisitDate(map: HashMap<String, Any>): LocalDate {
            val year = map.get("year") as Long
            val month = map.get("month") as Long
            val dayOfMonth = map.get("dayOfMonth") as Long
            return LocalDate.of(year.toInt(), month.toInt(), dayOfMonth.toInt())
        }

        private fun buildTimeToVisit(map: HashMap<String, Any>): LocalTime {
            val hour = map.get("hour") as Long
            val minute = map.get("minute") as Long
            val nano = map.get("nano") as Long
            val second = map.get("second") as Long
            return LocalTime.of(hour.toInt(), minute.toInt(), second.toInt(), nano.toInt())
        }
    }
}