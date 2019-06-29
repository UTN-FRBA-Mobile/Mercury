package ar.edu.utn.frba.mobile.a2019c1.mercury

import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.mobile.a2019c1.mercury.db.Database
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule

class ScheduleViewModel : ViewModel() {
    val schedules: MutableList<Schedule> = mutableListOf()

    fun remove(scheduleToDelete: Schedule) {
        Database.delete(scheduleToDelete)
    }

    fun upsert(schedule : Schedule) {
        if(schedule.objectId != null)
            Database.update(schedule)
        else
            Database.save(schedule)
    }

    fun updateDatasource(newSchedules: MutableList<Schedule>) {
        schedules.clear()
        schedules.addAll(newSchedules)
    }

}
