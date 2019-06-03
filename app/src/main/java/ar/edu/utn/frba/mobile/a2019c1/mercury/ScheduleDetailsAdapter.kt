package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.DaySchedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import kotlinx.android.synthetic.main.days_for_schedule_details.view.*
import android.widget.ArrayAdapter
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit


class ScheduleDetailsAdapter(
    private val context: Context,
    private val schedule: Schedule) : RecyclerView.Adapter<ScheduleDetailsAdapter.ScheduleDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDetailsAdapter.ScheduleDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.days_for_schedule_details, parent, false)
        return ScheduleDetailViewHolder(view)
    }

    override fun getItemCount() = schedule.clientsPerDay.size


    override fun onBindViewHolder(holder: ScheduleDetailsAdapter.ScheduleDetailViewHolder, position: Int) {
        val day = schedule.clientsPerDay[position]
        holder.bind(position, day)
    }

    inner class ScheduleDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            position: Int,
            day: DaySchedule
        ) {
            itemView.day_number.text = "Dia " + day.dayNumber.toString()
            val adapter = VisitsAdapter(context, day.visits)
            itemView.visits.adapter = adapter
        }

    }

}