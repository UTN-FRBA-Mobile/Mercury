package ar.edu.utn.frba.mobile.a2019c1.mercury.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog


object WhatsappIntegration {

    fun sendMessageToAContactInWhatsapp(
        textMessage: String?,
        phoneNumber: String?,
        activity: Activity
    ) {
        if(isAppAvailable(activity,"com.whatsapp")){
            launchWhatsappIntent(phoneNumber, textMessage, activity)
        }
        else{
            showAlertDialog(activity)
        }

    }

    private fun launchWhatsappIntent(
        phoneNumber: String?,
        textMessage: String?,
        activity: Activity
    ) {
        val number = formatPhoneNumber(phoneNumber)
        val sendIntent = Intent();
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.whatsapp")
        sendIntent.putExtra("jid", "$number@s.whatsapp.net");
        activity.startActivity(sendIntent)
    }

    private fun showAlertDialog(activity: Context) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("No se puede enviar mensajes a whatsapp.")
        builder.setCancelable(true)
        builder.setMessage("No tiene instalado Whatsapp")
        builder.setPositiveButton("Aceptar") { _, _ ->  }
        builder.show()

    }


    fun isAppAvailable(context: Context, appName: String): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    private fun formatPhoneNumber(phoneNumber: String?) = phoneNumber?.replace("+", "")?.replace(" ", "")


}
