package ar.edu.utn.frba.mobile.a2019c1.mercury

import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule

class ScheduleViewModel : ViewModel() {
    val schedules: MutableList<Schedule> = mutableListOf()

    fun upsert(scheduleToUpsert: Schedule): List<Schedule> {
        val scheduleExists = schedules.any { it.name == scheduleToUpsert.name }
        if(scheduleExists) {
            updateSchedule(scheduleToUpsert)
        } else {
            schedules.add(scheduleToUpsert)
        }
        return schedules
    }

    private fun updateSchedule(scheduleToUpsert: Schedule) {
        schedules.replaceAll { existingSchedule ->
            if (existingSchedule.name == scheduleToUpsert.name) {
                scheduleToUpsert
            } else {
                existingSchedule
            }
        }
    }
}
