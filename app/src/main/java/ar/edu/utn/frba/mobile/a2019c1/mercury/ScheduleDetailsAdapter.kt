package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.DaySchedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit
import ar.edu.utn.frba.mobile.a2019c1.mercury.util.MessageSender
import ar.edu.utn.frba.mobile.a2019c1.mercury.util.SmsMessageSender
import ar.edu.utn.frba.mobile.a2019c1.mercury.util.WhatsAppMessageSender
import kotlinx.android.synthetic.main.days_for_schedule_details.view.*
import kotlinx.android.synthetic.main.visit_for_schedule_detail.view.*
import java.time.LocalDate

class ScheduleDetailsAdapter(
    private val context: Context,
    private val schedule: Schedule,
    private val parentActivity: Activity
) : RecyclerView.Adapter<ScheduleDetailsAdapter.ScheduleDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.days_for_schedule_details, parent, false)
        return ScheduleDetailViewHolder(view)
    }

    override fun getItemCount() = schedule.clientsPerDay.size

    override fun onBindViewHolder(holder: ScheduleDetailViewHolder, position: Int) {
        val day = schedule.clientsPerDay[position]
        holder.bind(day)
    }

    inner class ScheduleDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(day: DaySchedule) {
            itemView.day_number.text = context.getString(R.string.SCHEDULE_DETAILS__SCHEDULE_DAY_NUMBER, day.dayNumber.toString())
            day.visits.sortedBy { it.timeToVisit }
                .forEach { visit ->
                    val visitView = createViewForVisit(visit)
                    itemView.schedule_details_visits.addView(visitView)
                }
        }

        private fun createViewForVisit(visit: Visit): View? {
            val visitView = LayoutInflater.from(context)
                .inflate(R.layout.visit_for_schedule_detail, itemView.schedule_details_visits, false)
            val clientNameView = visitView.findViewById(R.id.client_name) as TextView
            val visitTimeView = visitView.findViewById(R.id.client_visit_time) as TextView
            val client = visit.client
            val clientName = client.name
            clientNameView.text = clientName
            val visitTime = visit.timeToVisit.toString()
            visitTimeView.text = visitTime

            val today = LocalDate.now()
            val nextVisitDate: LocalDate? = visit.nextVisitDate(today)
            if (nextVisitDate != null) {
                val message = "Hola $clientName! Te quería avisar que el día ${nextVisitDate.dayOfMonth}/${nextVisitDate.monthValue} a las ${visitTime}hs voy a estar en tu comercio. Saludos!"
                val clientPhoneNumber = client.phoneNumber
                val sendMessageWith = { messageSender: MessageSender -> messageSender.sendMessage(message, clientPhoneNumber, parentActivity) }
                visitView.send_wpp_message.setOnClickListener { sendMessageWith(WhatsAppMessageSender) }
                visitView.send_sms_message.setOnClickListener { sendMessageWith(SmsMessageSender) }
            } else {
                listOf(visitView.send_wpp_message, visitView.send_sms_message).forEach { it.visibility = View.GONE }
            }

            return visitView
        }

    }

}