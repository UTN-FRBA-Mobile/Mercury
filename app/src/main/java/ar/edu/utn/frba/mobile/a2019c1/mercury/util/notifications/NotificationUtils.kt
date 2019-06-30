package ar.edu.utn.frba.mobile.a2019c1.mercury.util.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class NotificationUtils {

    fun setNotification(
        timeInMilliSeconds: Long,
        title: String,
        message: String,
        activity: Activity,
        uniqueId: String
    ) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        val newNotificationId = sharedPref.getInt("lastNotificationID", 0) + 1
        val notificationIds = HashSet(sharedPref.getStringSet(uniqueId, emptySet()))
        notificationIds.add(newNotificationId.toString())
        with(sharedPref.edit()) {
            putInt("lastNotificationID", newNotificationId)
            putStringSet(uniqueId, notificationIds)
            commit()
        }

        if (timeInMilliSeconds > 0) {
            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity, AlarmReceiver::class.java)
            alarmIntent.putExtra("reason", "notification")
            alarmIntent.putExtra("timestamp", timeInMilliSeconds)
            alarmIntent.putExtra("title", title)
            alarmIntent.putExtra("message", message)

            val pendingIntent = PendingIntent.getBroadcast(activity, newNotificationId, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInMilliSeconds, pendingIntent)
        }
    }

    fun cancelAllNotifications(uniqueId: String, activity: Activity){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        val notificationIds: MutableSet<String> = sharedPref.getStringSet(uniqueId, emptySet())!!
        with(sharedPref.edit()) {
            putStringSet(uniqueId, emptySet())
            commit()
        }

        notificationIds.forEach { id ->
            val idNumber = Integer.parseInt(id)
            cancelNotification(idNumber, activity)
        }
    }

    private fun cancelNotification(idNotification : Int, activity: Activity){
        val alarmIntent = Intent(activity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, idNotification, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}