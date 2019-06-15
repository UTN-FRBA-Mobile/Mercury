package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Client
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.DaySchedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit
import ar.edu.utn.frba.mobile.a2019c1.mercury.util.Permissions
import kotlinx.android.synthetic.main.fragment_schedule_edition.*
import kotlinx.android.synthetic.main.fragment_schedule_edition.view.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

class ScheduleEditionFragment : Fragment(), ScheduleEditionAdapter.OnItemClickListener {

    private val scheduleListViewModel: ScheduleViewModel by activityViewModels()
    private val scheduleEditionViewModel: ScheduleEditionViewModel by activityViewModels()
    private val clientsPerDay: MutableList<Pair<Int,Visit>> = mutableListOf()
    private lateinit var onEditionCompleted: () -> Unit

    private fun newClientInputFields() = listOf(client_name, client_phone_number, client_location)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_edition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleEditionViewModel.scheduleOnEdition?.let {
            loadScheduleToEditFromViewModel(it)
            scheduleEditionViewModel.scheduleOnEdition = null
        }

        client_visit_time.text = "00:00"
        client_visit_time.setOnClickListener{
            val timeSetListener =  TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                val newTime = LocalTime.of(hour, minute)
                client_visit_time.text = newTime.toString()
            }
            val time = LocalDateTime.now()
            TimePickerDialog(context,timeSetListener,time.hour,time.minute,true).show()
        }

        addClientToScheduleButton.setOnClickListener { addClientToSchedule() }

        val visits = clientsPerDay

        val scheduleEditionAdapter = ScheduleEditionAdapter(visits, context!!,this)

        with(view.visit_list as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleEditionAdapter
        }
        pickAContact.setOnClickListener{
            Permissions.checkPermissionsAndDo(this.activity!!, android.Manifest.permission.READ_CONTACTS) {
                launchContactPicker()
            }
        }
        fab.setOnClickListener { saveSchedule() }
    }
    private val PICK_CONTACT_REQUEST = 1  // The request code
    fun launchContactPicker(){
        Intent(Intent.ACTION_PICK, Uri.parse("content://contacts")).also { pickContactIntent ->
            pickContactIntent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE // Show user only contacts w/ phone numbers
            activity?.startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST)
        }
    }

    private fun updateAdapter(){
        view?.visit_list?.adapter?.notifyDataSetChanged()
    }
    override fun onDeleteItem(position: Int){
        clientsPerDay.removeAt(position)
        updateAdapter()
    }

    private fun addClientToSchedule() {
        val isClientDataValid = validateClientDataIsNotEmpty()
        if(!isClientDataValid) return

        val clientToAdd = parseNewClient()
        val visitTime: LocalTime =
            try {
                LocalTime.parse(client_visit_time.text.toString())
            } catch (e: DateTimeParseException) {
                client_visit_time.error = "Formato Invalido de fecha"
                return
            }
        val dayNumber = 1 // TODO get actual day
        val visit = Visit(clientToAdd,visitTime)
        clientsPerDay.add(Pair(dayNumber,visit))

        clearNewClientFields()
        updateAdapter()
    }

    private fun validateClientDataIsNotEmpty(): Boolean {
        val blankInputFields: List<EditText> = newClientInputFields().filter { it.text.isBlank() }
        blankInputFields.forEach { it.error = "No puede ser vacío" }
        return blankInputFields.isEmpty()
    }

    private fun parseNewClient(): Client {
        val name = client_name.text.toString()
        val phoneNumber = client_phone_number.text.toString()
        val location = client_location.text.toString()
        return Client(name, phoneNumber, location)
    }

    private fun clearNewClientFields() {
        val newClientInputFields = newClientInputFields()
        newClientInputFields.forEach {
            it.text.clear()
        }
        client_visit_time.text = "00:00"
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

        scheduleListViewModel.schedules.add(scheduleToCreate)

        onEditionCompleted()
    }

    fun setOnEditionCompletedCallback(onEditionCompleted: () -> Unit) {
        this.onEditionCompleted = onEditionCompleted
    }

    private fun loadScheduleToEditFromViewModel(scheduleToEdit: Schedule) {
        schedule_name.setText(scheduleToEdit.name)

        val scheduleClientsPerDay = scheduleToEdit.clientsPerDay
            .flatMap { dayScheduleToClientsPerDay(it) }
        clientsPerDay.addAll(scheduleClientsPerDay)
    }

    private fun dayScheduleToClientsPerDay(clientsPerDay: DaySchedule) =
        clientsPerDay.visits.map { visit -> Pair(clientsPerDay.dayNumber, visit) }

    fun processContactPick(contactName: String?, phoneNumber: String?, location: String?){
        client_name.setText(contactName)
        client_phone_number.setText(phoneNumber)
        client_location.setText(location)
    }
}