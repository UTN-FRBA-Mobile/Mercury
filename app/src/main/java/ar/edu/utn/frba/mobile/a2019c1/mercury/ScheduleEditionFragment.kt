package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Client
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit
import kotlinx.android.synthetic.main.fragment_schedule_edition.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

class ScheduleEditionFragment : Fragment() {

    private val viewModel: ScheduleViewModel by activityViewModels()
    private val clientsPerDay: MutableList<Pair<Int,Visit>> = mutableListOf()
    private lateinit var onEditionCompleted: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_edition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        client_visit_time.text = "00:00"
        client_visit_time.setOnClickListener{
            val timeSetListener=  TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                client_visit_time.text = "$hour:$minute"
            }
            val time = LocalDateTime.now()
            TimePickerDialog(context,timeSetListener,time.hour,time.minute,true).show()
        }

        addClientToScheduleButton.setOnClickListener { addClientToSchedule() }

        fab.setOnClickListener { saveSchedule() }
    }

    private fun addClientToSchedule() {
        val name = client_name.text.toString()
        val phoneNumber = client_phone_number.text.toString()
        val location = client_location.text.toString()
        val visitTime: LocalTime =
        try {
            LocalTime.parse(client_visit_time.text.toString())
        } catch (e: DateTimeParseException) {
            client_visit_time.error = "Formato Invalido de fecha"
            return
        }
        val clientToAdd = Client(name, phoneNumber, location)
        val dayNumber = 1 // TODO get actual day
        val visit = Visit(clientToAdd,visitTime)
        clientsPerDay.add(Pair(dayNumber,visit))
    }

    private fun saveSchedule() {
        val scheduleName = schedule_name.text.toString()

        if (scheduleName.isBlank()) {
            schedule_name.error = "Agregar nombre"
            return
        }

        val scheduleToCreate = Schedule(scheduleName)

        clientsPerDay.forEach { (dayNumber, client) ->
            scheduleToCreate.addClientOnDay(dayNumber, client)
        }

        viewModel.schedules.add(scheduleToCreate)

        onEditionCompleted()
    }

    fun setOnEditionCompletedCallback(onEditionCompleted: () -> Unit) {
        this.onEditionCompleted = onEditionCompleted
    }

}