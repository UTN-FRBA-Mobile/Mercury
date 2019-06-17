package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("title", intent.getStringExtra("title"))
        service.putExtra("message", intent.getStringExtra("message"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))

        context.startService(service)
    }

}