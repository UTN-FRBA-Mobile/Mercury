package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        NotificationService().enqueueWork(context, intent)

    }

}