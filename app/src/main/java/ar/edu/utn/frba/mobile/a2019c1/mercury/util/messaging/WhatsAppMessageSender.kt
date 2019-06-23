package ar.edu.utn.frba.mobile.a2019c1.mercury.util.messaging

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog

object WhatsAppMessageSender : MessageSender {

    override fun sendMessage(message: String, phoneNumber: String, activity: Activity) {
        if (isAppAvailable(activity, "com.whatsapp")) {
            launchWhatsappIntent(phoneNumber, message, activity)
        } else {
            showAlertDialog(activity)
        }
    }

    private fun launchWhatsappIntent(phoneNumber: String, textMessage: String, activity: Activity) {
        val number = formatPhoneNumber(phoneNumber)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.whatsapp")
        sendIntent.putExtra("jid", "$number@s.whatsapp.net")
        activity.startActivity(sendIntent)
    }

    private fun showAlertDialog(activity: Context) {
        AlertDialog.Builder(activity)
            .setTitle("WhatsApp no se encuentra instalada")
            .setCancelable(true)
            .setMessage("Para enviar mensajes a través de WhatsApp, debe tener la aplicación instalada")
            .setPositiveButton("Aceptar") { _, _ -> }
            .show()
    }

    private fun isAppAvailable(context: Context, appName: String): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun formatPhoneNumber(phoneNumber: String) = phoneNumber.filter { it.isDigit() }

}
