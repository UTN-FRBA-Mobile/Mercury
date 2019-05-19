package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import kotlinx.android.synthetic.main.schedule_for_list_view.view.*

class ScheduleListAdapter(
    private val context: Context,
    private val schedules: List<Schedule>,
    private val deleteSchedule: (Schedule) -> Unit
) : RecyclerView.Adapter<ScheduleListAdapter.ScheduleListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_for_list_view, parent, false)
        return ScheduleListItemViewHolder(view)
    }

    override fun getItemCount() = schedules.size

    override fun onBindViewHolder(holder: ScheduleListItemViewHolder, position: Int) {
        val scheduleToUpdate = schedules[position]
        holder.bind(scheduleToUpdate, deleteSchedule)
    }

    inner class ScheduleListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: Schedule, deleteSchedule: (Schedule) -> Unit) {
            itemView.schedule_name.text = schedule.name
            val scheduleDuration = schedule.duration()
            itemView.schedule_duration.text = context.resources.getQuantityString(R.plurals.LIST_SCHEDULE__SCHEDULE_DURATION,
                scheduleDuration,
                scheduleDuration
            )

            itemView.schedule_delete_button.setOnClickListener {
                deleteSchedule(schedule)
                notifyDataSetChanged()
            }
        }
    }
}
