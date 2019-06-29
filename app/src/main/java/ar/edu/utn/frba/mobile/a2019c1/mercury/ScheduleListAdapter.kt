package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.VisitOnDate
import ar.edu.utn.frba.mobile.a2019c1.mercury.util.notifications.NotificationScheduler
import kotlinx.android.synthetic.main.schedule_for_list_view.view.*
import java.time.LocalDate
import java.time.LocalDateTime

class ScheduleListAdapter(
    private val context: Context,
    private val activity: Activity,
    private val schedules: List<Schedule>,
    private val viewSchedule: (Schedule) -> Unit,
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

    private fun startScheduleOn(schedule: Schedule, scheduleStartDate: LocalDate) {
        schedule.startOn(scheduleStartDate)
        scheduleNotificationsForNewVisits(schedule)
    }

    private fun scheduleNotificationsForNewVisits(schedule: Schedule) {
        val notificationAnticipationInMinutes = 15L

        val today: LocalDateTime = LocalDateTime.now()
        schedule.nextVisitDates(today.toLocalDate()).forEach { visitOnDate ->
            scheduleNotificationForVisit(visitOnDate, today, notificationAnticipationInMinutes)
        }
    }

    private fun scheduleNotificationForVisit(visitOnDate: VisitOnDate, today: LocalDateTime, notificationAnticipation: Long) {
        val visit = visitOnDate.visit
        val visitTime = visit.timeToVisit
        val visitDateTime: LocalDateTime = visitOnDate.date.atTime(visitTime)
        val title = "Reunión con ${visit.client.name}"
        val message = "Tenés una reunión a las ${visitTime}hs"

        NotificationScheduler().scheduleNotificationWithAnticipation(visitDateTime, today, notificationAnticipation, title, message, activity)
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

            itemView.schedule_active.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectScheduleStartDate(schedule)
                } else {
                    disableSchedule(schedule)
                }
                notifyDataSetChanged()
            }

            itemView.schedule_details_button.setOnClickListener {
                viewSchedule(schedule)
            }

            itemView.schedule_edit_button.setOnClickListener {
                updateSchedule(schedule)
            }

            itemView.schedule_delete_button.setOnClickListener {
                val alertDialog: AlertDialog = AlertDialog.Builder(context)
                    .setMessage(context.getText(R.string.LIST_SCHEDULE__SCHEDULE_DELETION_MODAL__MESSAGE))
                    .setPositiveButton(context.getText(R.string.LIST_SCHEDULE__SCHEDULE_DELETION_MODAL__CONFIRMATION_BUTTON)){_, _ ->
                        deleteSchedule(schedule)
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton(context.getText(R.string.LIST_SCHEDULE__SCHEDULE_MODAL__CANCEL_BUTTON)) { _, _ ->  }
                    .create()
                alertDialog.show()
            }
        }

        private fun selectScheduleStartDate(schedule: Schedule) {
            val datePicker = DatePicker(context)
            val title = context.getText(R.string.LIST_SCHEDULE__SCHEDULE_START_DATE_PICKER_MODAL__TITLE)
            val confirmButtonText = context.getText(R.string.LIST_SCHEDULE__SCHEDULE_START_DATE_PICKER_MODAL__CONFIRMATION_BUTTON)
            val cancelButtonText = context.getText(R.string.LIST_SCHEDULE__SCHEDULE_START_DATE_PICKER_MODAL__CANCEL_BUTTON)
            val alertDialog: AlertDialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setView(datePicker)
                .setPositiveButton(confirmButtonText) { _, _ ->
                    val scheduleStartDate: LocalDate = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
                    startScheduleOn(schedule, scheduleStartDate)
                }
                .setNegativeButton(cancelButtonText) { _, _ -> itemView.schedule_active.isChecked = false }
                .create()
            alertDialog.show()
        }

        private fun disableSchedule(schedule: Schedule) {
            val alertDialog: AlertDialog = AlertDialog.Builder(context)
                .setMessage(context.getText(R.string.LIST_SCHEDULE__SCHEDULE_DISABLE_MODAL__MESSAGE))
                .setPositiveButton(context.getText(R.string.LIST_SCHEDULE__SCHEDULE_DISABLE_MODAL__CONFIRMATION_BUTTON)){_, _ ->
                    val rightNow = LocalDateTime.now()
                    schedule.disable(rightNow)
                    notifyDataSetChanged()
                }
                .setNegativeButton(context.getText(R.string.LIST_SCHEDULE__SCHEDULE_MODAL__CANCEL_BUTTON)) { _, _ ->  }
                .create()
            alertDialog.show()
        }

    }
}
