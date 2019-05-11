package ar.edu.utn.frba.mobile.a2019c1.mercury.model

class Schedule(val name: String) {
    private val clientsPerDay: MutableList<DaySchedule> = mutableListOf()

    fun visits(): List<Visit> {
        return clientsPerDay.map { it.visits }
            .flatten()
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

    override fun toString(): String {
        return "Schedule(name=$name, clients=${visits().map{it.client.name}})"
    }

}

data class DaySchedule(val dayNumber: Int, val visits: MutableList<Visit>) {
    fun add(visit: Visit) {
        visits.add(visit)
    }
}
