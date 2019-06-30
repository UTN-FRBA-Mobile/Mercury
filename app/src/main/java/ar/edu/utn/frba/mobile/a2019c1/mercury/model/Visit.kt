package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class MyLocalDate(val year: Int, val month: Int, val dayOfMonth: Int) {

    constructor(it: LocalDate) : this(it.year, it.monthValue, it.dayOfMonth)

    fun toLocalDate() : LocalDate {
        return LocalDate.of(year, month, dayOfMonth)
    }
}

@IgnoreExtraProperties
data class Visit(val client : Client, val timeToVisit: LocalTime, val visitDatesToPersist : MutableList<MyLocalDate> = mutableListOf()) {

    @Exclude
    fun getVisitsDates() : MutableList<LocalDate> {
        return visitDatesToPersist.map { it.toLocalDate() } .toMutableList()
    }

    fun addVisitOnDate(date: LocalDate) {
        visitDatesToPersist.add(MyLocalDate(date))
    }

    fun disableVisitDatesAfter(disableTime: LocalDateTime) {
        visitDatesToPersist.removeIf { it.toLocalDate().atTime(timeToVisit).isAfter(disableTime) }
    }

    fun nextVisitDate(date: LocalDate): LocalDate? {
        // There should be no more than one possible next visit date. If there are, the first one is picked
        return getVisitsDates().sortedBy { it }
            .firstOrNull { it.isEqual(date) || it.isAfter(date) }
    }

    @Exclude
    fun getVisitsOnDates(): Iterable<VisitOnDate> {
        return getVisitsDates().map { date -> VisitOnDate(this, date) }
    }

    companion object {
        fun buildFromDatabase(map: HashMap<String, Any>): Visit {
            val hashMapClient = map.get("client") as HashMap<String, Any>
            val client = Client.buildFromDatabase(hashMapClient)
            val hashMapTimeToVisit = map.get("timeToVisit") as HashMap<String, Any>
            val timeToVisit = buildTimeToVisit(hashMapTimeToVisit)
            val hashMapVisitDates = map.get("visitDatesToPersist") as MutableList<HashMap<String, Any>>? ?: mutableListOf()
            val visitDates = hashMapVisitDates.map { buildVisitDate(it) } .toMutableList()
            return Visit(client, timeToVisit, visitDates)
        }

        private fun buildVisitDate(map: HashMap<String, Any>): MyLocalDate {
            val year = map.get("year") as Long
            val month = map.get("month") as Long
            val dayOfMonth = map.get("dayOfMonth") as Long
            return MyLocalDate(year.toInt(), month.toInt(), dayOfMonth.toInt())
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