package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import kotlinx.android.synthetic.main.fragment_schedule_edition.fab
import kotlinx.android.synthetic.main.fragment_schedule_list.*

class ScheduleListFragment : Fragment() {

    private val viewModel: ScheduleViewModel by activityViewModels()
    private lateinit var onAddScheduleButtonClicked: () -> Unit
    private lateinit var onEditScheduleButtonClicked: (Schedule) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        fab.setOnClickListener { onAddScheduleButtonClicked() }
    }

    private fun setupAdapter() {
        val schedules = viewModel.schedules
        val scheduleListAdapter = ScheduleListAdapter(context!!, schedules, this::updateSchedule, this::deleteSchedule)

        if (schedules.isEmpty()) {
            schedule_list.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }

        with(schedule_list as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleListAdapter
        }
    }

    private fun updateSchedule(scheduleToUpdate: Schedule) {
        onEditScheduleButtonClicked(scheduleToUpdate)
    }

    private fun deleteSchedule(scheduleToDelete: Schedule) {
        viewModel.remove(scheduleToDelete)
        setupAdapter()
    }

    fun setOnAddScheduleButtonClicked(onAddScheduleButtonClicked: () -> Unit) {
        this.onAddScheduleButtonClicked = onAddScheduleButtonClicked
    }

    fun setOnEditScheduleButtonClicked(onEditScheduleButtonClicked: (Schedule) -> Unit) {
        this.onEditScheduleButtonClicked = onEditScheduleButtonClicked
    }

}