package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*

class NotificationUtils {


    /*
    Ejemplo de uso

    import java.util.*

    val mNotificationTime = Calendar.getInstance().timeInMillis + 5000
    NotificationUtils().setNotification(mNotificationTime, "Prueba noti", "aca va el mensaje", this.activity!!)

     */

    fun setNotification(timeInMilliSeconds: Long, title: String, message : String, activity: Activity) {

        //------------  alarm settings start  -----------------//

        if (timeInMilliSeconds > 0) {


            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity, AlarmReceiver::class.java) // AlarmReceiver1 = broadcast receiver

            alarmIntent.putExtra("reason", "notification")
            alarmIntent.putExtra("timestamp", timeInMilliSeconds)
            alarmIntent.putExtra("title", title)
            alarmIntent.putExtra("message", message)


            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSeconds, pendingIntent)

        }

        //------------ end of alarm settings  -----------------//


    }
}