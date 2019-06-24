package ar.edu.utn.frba.mobile.a2019c1.mercury

import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.mobile.a2019c1.mercury.db.Database
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule

class ScheduleViewModel : ViewModel() {
    val schedules: MutableList<Schedule> = mutableListOf()

    fun remove(scheduleToDelete: Schedule) {
        schedules.remove(scheduleToDelete)
        Database.delete(scheduleToDelete)
    }

    fun upsert(scheduleToUpsert: Schedule) {
        Database.save(scheduleToUpsert)
    }

}
