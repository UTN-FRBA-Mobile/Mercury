package ar.edu.utn.frba.mobile.a2019c1.mercury.util.messaging

import android.app.Activity

interface MessageSender {
    fun sendMessage(message: String, phoneNumber: String, activity: Activity)
}
