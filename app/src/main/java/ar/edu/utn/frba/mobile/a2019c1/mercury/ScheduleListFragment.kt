package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schedule_edition.*
import kotlinx.android.synthetic.main.fragment_schedule_list.view.*

class ScheduleListFragment : Fragment() {

    private val viewModel: ScheduleViewModel by activityViewModels()
    private lateinit var onAddScheduleButtonClicked: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val schedules = viewModel.schedules
        val scheduleListAdapter = ScheduleListAdapter(schedules, context!!)

        with(view.schedule_list as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleListAdapter
        }

        fab.setOnClickListener { onAddScheduleButtonClicked() }
    }

    fun setOnAddScheduleButtonClicked(onAddScheduleButtonClicked: () -> Unit) {
        this.onAddScheduleButtonClicked = onAddScheduleButtonClicked
    }

}