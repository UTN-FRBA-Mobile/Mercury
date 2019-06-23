package ar.edu.utn.frba.mobile.a2019c1.mercury.util.notifications

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import java.util.*
import android.app.NotificationChannel
import androidx.core.app.JobIntentService
import ar.edu.utn.frba.mobile.a2019c1.mercury.MainActivity
import ar.edu.utn.frba.mobile.a2019c1.mercury.R

class NotificationService : JobIntentService() {
    private lateinit var mNotification: Notification
    private val mNotificationId: Int = 1000

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, NotificationService::class.java, mNotificationId, work)
    }

    @SuppressLint("NewApi")
    private fun createChannel() {
        val context = this.applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.parseColor("#e8334a")
        notificationChannel.description = getString(R.string.NOTIFICATION_CHANNEL_DESCRIPTION)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        const val CHANNEL_ID = "ar.edu.utn.frba.mobile.a2019c1.mercury.Itinerarios"
        const val CHANNEL_NAME = "Itinerarios"
    }

    override fun onHandleWork(intent: Intent) {
        createChannel()

        var timestamp: Long = 0
        var title = ""
        var message = ""

        if (intent.extras != null) {
            timestamp = intent.extras!!.getLong("timestamp")
            title = intent.extras!!.getString("title",title)
            message = intent.extras!!.getString("message",message)
        }

        if (timestamp > 0) {
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val pendingIntent = createPendingIntent(title, message, timestamp)
            val res = this.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mNotification = Notification.Builder(applicationContext, CHANNEL_ID)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(Notification.BigTextStyle()
                    .bigText(message))
                .setContentText(message).build()

            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)
        }

    }

    private fun createPendingIntent(title: String, message: String,timestamp: Long): PendingIntent {
        val notifyIntent = Intent(this, MainActivity::class.java) //Se elige la activity que se va a abrir

        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("message", message)
        notifyIntent.putExtra("notification", true)

        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return PendingIntent.getActivity(applicationContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}