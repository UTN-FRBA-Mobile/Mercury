package ar.edu.utn.frba.mobile.a2019c1.mercury.util.notifications

import android.app.Activity
import java.time.Duration
import java.time.LocalDateTime

class NotificationScheduler {
    fun scheduleNotificationWithAnticipation(
        eventDateTime: LocalDateTime,
        today: LocalDateTime,
        notificationAnticipation: Long,
        title: String,
        message: String,
        activity: Activity
    ) {
        val timeUntilVisit = Duration.between(today, eventDateTime)
        val timeUntilNotification = timeUntilVisit.minusMinutes(notificationAnticipation)

        NotificationUtils().setNotification(timeUntilNotification.toMillis(), title, message, activity)
    }
}
