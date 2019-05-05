package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Client
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {

    private val clientsPerDay: MutableList<Pair<Int, Client>> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectHourButton.setOnClickListener{

            val timeSetListener=  TimePickerDialog.OnTimeSetListener{timePicker,hour,minute ->
                client_visit_time.text = hour.toString()+":"+minute.toString()
            }
            val time = LocalDateTime.now()
            TimePickerDialog(context,timeSetListener,time.hour,time.minute,true).show()
        }
        fab.setOnClickListener {
            val scheduleName = schedule_name.text.toString()

            if (scheduleName.isBlank()) {
                TODO("No name validation")
            }

            val scheduleToCreate = Schedule(scheduleName)

            clientsPerDay.forEach { (dayNumber, client) ->
                scheduleToCreate.addClientOnDay(dayNumber, client)
            }

            Snackbar.make(it, scheduleToCreate.toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        addClientToScheduleButton.setOnClickListener {
            addClientToSchedule(it)
        }
    }

    private fun addClientToSchedule(it: View) {
        val name = client_name.text.toString()
        val phoneNumber = client_phone_number.text.toString()
        val location = client_location.text.toString()
        val visitTime: LocalTime = try {
            LocalTime.parse(client_visit_time.text.toString())
        } catch (e: DateTimeParseException) {
            TODO("Date parsing validation")
        }

        val clientToAdd = Client(name, phoneNumber, location, visitTime)
        val dayNumber = 1 // TODO get actual day
        clientsPerDay.add(Pair(dayNumber, clientToAdd))
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Title.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}