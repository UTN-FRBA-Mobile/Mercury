package ar.edu.utn.frba.mobile.a2019c1.mercury

import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule

class ScheduleViewModel : ViewModel() {
    val schedules: MutableList<Schedule> = mutableListOf()
}
