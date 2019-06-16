package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import kotlinx.android.synthetic.main.schedule_for_list_view.view.*
import java.time.LocalDate

class ScheduleListAdapter(
    private val context: Context,
    private val schedules: List<Schedule>,
    private val updateSchedule: (Schedule) -> Unit,
    private val deleteSchedule: (Schedule) -> Unit
) : RecyclerView.Adapter<ScheduleListAdapter.ScheduleListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_for_list_view, parent, false)
        return ScheduleListItemViewHolder(view)
    }

    override fun getItemCount() = schedules.size

    override fun onBindViewHolder(holder: ScheduleListItemViewHolder, position: Int) {
        val scheduleToUpdate = schedules[position]
        holder.bind(position, scheduleToUpdate, updateSchedule, deleteSchedule)
    }

    inner class ScheduleListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            position: Int,
            schedule: Schedule,
            updateSchedule: (Schedule) -> Unit,
            deleteSchedule: (Schedule) -> Unit
        ) {
            itemView.schedule_name.text = schedule.name
            val scheduleDuration = schedule.duration()
            itemView.schedule_duration.text = context.resources.getQuantityString(R.plurals.LIST_SCHEDULE__SCHEDULE_DURATION,
                scheduleDuration,
                scheduleDuration
            )
            itemView.schedule_notification_button.visibility = if (itemView.schedule_active.isChecked) View.VISIBLE else View.GONE

            itemView.schedule_active.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectScheduleStartDate()
                } else {
                    //TODO desactivar itinerario
                }
                notifyDataSetChanged()
            }

            itemView.schedule_edit_button.setOnClickListener {
                updateSchedule(schedule)
            }

            itemView.schedule_delete_button.setOnClickListener {
                val alertDialog: AlertDialog = AlertDialog.Builder(context)
                    .setMessage("¿Estás seguro de que querés eliminar el itinerario? No vas a poder recuperarlo")
                    .setPositiveButton("Eliminar"){_, _ ->
                        deleteSchedule(schedule)
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton("Cancelar") {_, _ ->  }
                    .create()
                alertDialog.show()
            }
        }

        private fun selectScheduleStartDate() {
            val datePicker = DatePicker(context)
            val alertDialog: AlertDialog = AlertDialog.Builder(context)
                .setTitle("¿Cuándo querés iniciar el itinerario?")
                .setView(datePicker)
                .setPositiveButton("Confirmar") { _, _ ->
                    val scheduleStartDate = LocalDate.of(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                    Toast.makeText(context, scheduleStartDate.toString(), Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar") { _, _ -> itemView.schedule_active.isChecked = false }
                .create()
            alertDialog.show()
        }
    }
}
