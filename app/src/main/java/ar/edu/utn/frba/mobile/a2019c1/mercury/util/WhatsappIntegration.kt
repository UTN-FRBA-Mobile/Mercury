package ar.edu.utn.frba.mobile.a2019c1.mercury.util

import android.app.Activity
import android.content.Intent


object WhatsappIntegration {

    fun sendMessageToAContactInWhatsapp(
        textMessage: String?,
        phoneNumber: String?,
        activity: Activity
    ) {
        val number = formatPhoneNumber(phoneNumber)
        val sendIntent = Intent();
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,textMessage)
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.whatsapp")
        sendIntent.putExtra("jid", "$number@s.whatsapp.net");
        activity.startActivity(sendIntent)
    }

    private fun formatPhoneNumber(phoneNumber: String?) = phoneNumber?.replace("+", "")?.replace(" ", "")


}
