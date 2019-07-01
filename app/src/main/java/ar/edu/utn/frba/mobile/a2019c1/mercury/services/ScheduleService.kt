package ar.edu.utn.frba.mobile.a2019c1.mercury.services

import android.content.Context
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import com.google.gson.Gson

class ScheduleService(context: Context) : HTTPService(context, "https://mercury-mobile.herokuapp.com") {

    fun getSchedules() {
        get("schedules", {
            var scheduleMap = Gson().fromJson(it, HashMap::class.java)
            scheduleMap.map {  }
            scheduleMap.size
        })
    }
}