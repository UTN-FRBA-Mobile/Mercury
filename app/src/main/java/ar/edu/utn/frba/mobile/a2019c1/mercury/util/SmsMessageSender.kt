package ar.edu.utn.frba.mobile.a2019c1.mercury.util

import android.app.Activity
import android.content.Intent
import android.net.Uri

object SmsMessageSender : MessageSender {
    override fun sendMessage(message: String, phoneNumber: String, activity: Activity) {
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", message)
        activity.startActivity(intent)
    }
}
