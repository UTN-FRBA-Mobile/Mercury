package ar.edu.utn.frba.mobile.a2019c1.mercury.services

import android.content.Context
import ar.edu.utn.frba.mobile.a2019c1.mercury.db.Database
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScheduleService(context: Context) : HTTPService(context, "https://mercury-mobile.herokuapp.com") {

    fun getSchedules() {
        get("schedules", {
            val listType = object : TypeToken<List<Schedule>>() { }.type
            val schedules: ArrayList<Schedule> = Gson().fromJson(it, listType)
            schedules.forEach { Database.save(it) }
        })
    }
}