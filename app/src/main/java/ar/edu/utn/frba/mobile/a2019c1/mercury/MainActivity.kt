package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.util.Permissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val scheduleEditionViewModel: ScheduleEditionViewModel by viewModels()
    private val PICK_CONTACT_REQUEST = 1  // The request code
    private lateinit var onPickedContact: (String?,String?,String?) -> Unit
    private lateinit var launchContactPicker: () -> Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            // Solo la primera vez que corre el activity
            // Las demás el propio manager restaura todo como estaba
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ScheduleEditionFragment())
                .commit()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is ScheduleEditionFragment) {
            fragment.setOnEditionCompletedCallback(this::onEditionCompleted)
            onPickedContact = fragment::processContactPick
            launchContactPicker = fragment::launchContactPicker
        } else if (fragment is ScheduleListFragment) {
            fragment.setOnAddScheduleButtonClicked(this::onAddScheduleButtonClicked)
            fragment.setOnEditScheduleButtonClicked(this::onScheduleEditionRequest)
        }
    }

    private fun onAddScheduleButtonClicked() {
        showFragment(ScheduleEditionFragment())
    }

    private fun onScheduleEditionRequest(scheduleToEdit: Schedule) {
        val scheduleEditionFragment = ScheduleEditionFragment()
        scheduleEditionViewModel.scheduleOnEdition = scheduleToEdit
        showFragment(scheduleEditionFragment)
    }

    private fun onEditionCompleted() {
        showFragment(ScheduleListFragment())
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val activity = this
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.also { contactUri ->
                    contentResolver.query(contactUri, null, null, null, null)?.apply {
                        moveToFirst()

                        // Retrieve the phone number from the NUMBER column
                        val displayNameIndex: Int = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        val displayname: String? = getString(displayNameIndex)
                        val phoneNumberIndex: Int = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val number: String? = getString(phoneNumberIndex)

                        val contactIndex: Int = getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                        val contactID: String? = getString(contactIndex)
                        var address = ""

                        Permissions.checkPermissionsAndDo(activity,Manifest.permission.READ_CONTACTS) {
                            address = getContactAddressByContactId(contactID)
                        }

                        address = getContactAddressByContactId(contactID)


                        onPickedContact(displayname,number,address);


                    }
                }
            }
        }
    }

    private fun getContactAddressByContactId(contactID: String?): String {
        val cursorAddress = contentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + "=?", arrayOf(contactID), null
        )

        if (cursorAddress.count > 0) {
            while (cursorAddress.moveToNext()) {
                return cursorAddress.getString(
                    cursorAddress.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
                )
            }
        }
        cursorAddress.close()
        return "";
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Permissions.REQUEST_READ_CONTACT_PERMISSIONS_CODE -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchContactPicker();
                }
                return
            }
        }
    }
}
