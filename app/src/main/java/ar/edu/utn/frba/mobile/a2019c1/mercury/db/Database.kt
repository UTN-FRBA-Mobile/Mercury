package ar.edu.utn.frba.mobile.a2019c1.mercury.db

import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import com.google.firebase.database.FirebaseDatabase

class Database {

    companion object {
        val db = FirebaseDatabase.getInstance().reference

        fun save(scheduleToSave: Schedule) {
            val newObject = db.child(Statics.SCHEDULE).push()
            scheduleToSave.objectId = newObject.key
            newObject.setValue(scheduleToSave)
        }
        
    }
}