package ar.edu.utn.frba.mobile.a2019c1.mercury.model

class Schedule(val name: String) {
    private val clientsPerDay: MutableList<DaySchedule> = mutableListOf()

    fun clients(): List<Client> {
        return clientsPerDay.map { it.clients }
            .flatten()
    }

    fun duration(): Int {
        return clientsPerDay.map { it.dayNumber }
            .max()
            ?: 0
    }

    fun addClientOnDay(day: Int, client: Client) {
        daySchedule(day).add(client)
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
        return "Schedule(name=$name, clients=${clients().map{it.name}})"
    }

}

data class DaySchedule(val dayNumber: Int, val clients: MutableList<Client>) {
    fun add(client: Client) {
        clients.add(client)
    }
}
