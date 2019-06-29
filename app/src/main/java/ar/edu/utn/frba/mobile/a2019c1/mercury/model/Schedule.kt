package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import java.time.LocalDate
import java.time.LocalDateTime

class Schedule(var name: String) {

    var objectId: String? = null

    var clientsPerDay: MutableList<DaySchedule> = mutableListOf()

    fun visits(): List<Visit> {
        return clientsPerDay.map { it.visits }
            .flatten()
    }

    fun visitsOnDates(): List<VisitOnDate> {
        return visits().flatMap { visit ->
            visit.visitDates.map { date -> VisitOnDate(visit, date) }
        }
    }

    fun nextVisitDates(dateToLookForwardFrom: LocalDate): List<VisitOnDate> {
        return visits().mapNotNull { visit ->
            val nextVisitDate = visit.nextVisitDate(dateToLookForwardFrom)
            nextVisitDate?.let { VisitOnDate(visit, it) }
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

    fun disable(rightNow: LocalDateTime) {

    }

    override fun toString(): String {
        return "Schedule(name=$name, clients=${visits().map{it.client.name}})"
    }

    companion object {
        fun buildFromDatabase(map: HashMap<String, Any>): Schedule {
            val name = map.get("name") as String
            val objectId = map.get("objectId") as String
            val hashMapClientsPerDay = map.get("clientsPerDay") as MutableList<HashMap<String, Any>>? ?: mutableListOf()
            val clientsPerDay = hashMapClientsPerDay.map { DaySchedule.buildFromDatabase(it) } .toMutableList()
            val schedule = Schedule(name)
            schedule.objectId = objectId
            schedule.clientsPerDay = clientsPerDay
            return schedule
        }
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

    companion object {
        fun buildFromDatabase(map: HashMap<String, Any>): DaySchedule {
            val dayNumber = map.get("dayNumber") as Long
            val hashMapVisits = map.get("visits") as MutableList<HashMap<String, Any>>
            val visits = hashMapVisits.map { Visit.buildFromDatabase(it) } .toMutableList()
            return DaySchedule(dayNumber.toInt(), visits)
        }
    }

}
