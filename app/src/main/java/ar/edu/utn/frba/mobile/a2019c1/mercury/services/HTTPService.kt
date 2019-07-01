package ar.edu.utn.frba.mobile.a2019c1.mercury.services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

open class HTTPService(val context: Context, private val baseURl: String) {
    val queue = Volley.newRequestQueue(context)

    private fun request(method: Int, resource: String, callback: (String?) -> Unit) {
        val request = StringRequest(method, "$baseURl/$resource", Response.Listener(callback), Response.ErrorListener {  } )
        queue.add(request)
    }
    fun get(resource: String, callback: (String?) -> Unit) {
        request(Request.Method.GET, resource, callback)
    }
}
