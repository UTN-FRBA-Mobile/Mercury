package ar.edu.utn.frba.mobile.a2019c1.mercury.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat


object Permissions {
    val REQUEST_READ_CONTACT_PERMISSIONS_CODE = 300

    private fun hasPermissions(activity: Context, permissionCode: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permissionCode) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionsAndDo(activity: Activity, permissionCode: String, callback: () -> Unit) {
        if (!hasPermissions(activity, permissionCode)) {
            requestPermissions(activity,arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACT_PERMISSIONS_CODE)

        } else {
            callback()
        }
    }


}
