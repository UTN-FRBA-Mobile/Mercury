package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import java.time.LocalDate

class Schedule(val name: String) {
    val clientsPerDay: MutableList<DaySchedule> = mutableListOf()

    fun visits(): List<Visit> {
        return clientsPerDay.map { it.visits }
            .flatten()
    }

    fun visitsOnDates(): List<VisitOnDate> {
        return visits().flatMap { visit ->
            visit.visitDates.map { date -> VisitOnDate(visit, date) }
        }
    }

    fun duration(): Int {
        return clientsPerDay.map { it.dayNumber }
            .max()
            ?: 0
    }

    fun addClientOnDay(day: Int, visit: Visit) {
        daySchedule(day).add(visit)
    }

    private fun daySchedule(day: Int): DaySchedule {
        return clientsPerDay.firstOrNull { it.dayNumber == day }
            ?: addDaySchedule(day)
    }

    private fun addDaySchedule(day: Int): DaySchedule {
        val emptyDaySchedule = DaySchedule(day, mutableListOf())
        clientsPerDay.add(emptyDaySchedule)
        return emptyDaySchedule
    }

    fun startOn(startDate: LocalDate) {
        clientsPerDay.forEach { it.registerDayEventsStartingOn(startDate) }
    }

    override fun toString(): String {
        return "Schedule(name=$name, clients=${visits().map{it.client.name}})"
    }

}

data class DaySchedule(val dayNumber: Int, val visits: MutableList<Visit>) {
    fun add(visit: Visit) {
        visits.add(visit)
    }

    fun registerDayEventsStartingOn(startDate: LocalDate) {
        visits.forEach { visit ->
            val daysAfterScheduleStart = dayNumber.toLong() - 1
            val visitDate: LocalDate = startDate.plusDays(daysAfterScheduleStart)
            visit.addVisitOnDate(visitDate)
        }
    }
}
