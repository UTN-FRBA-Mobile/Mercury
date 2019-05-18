package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import kotlinx.android.synthetic.main.schedule_for_list_view.view.*

class ScheduleListAdapter(
    private val schedules: List<Schedule>,
    private val context: Context
) : RecyclerView.Adapter<ScheduleListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_for_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = schedules.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val schedule = schedules[position]
        itemView.schedule_name.text = schedule.name
        val scheduleDuration = schedule.duration()
        itemView.schedule_duration.text = context.resources.getQuantityString(R.plurals.LIST_SCHEDULE__SCHEDULE_DURATION,
            scheduleDuration,
            scheduleDuration
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
