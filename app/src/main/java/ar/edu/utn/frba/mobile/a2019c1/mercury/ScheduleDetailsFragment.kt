package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schedule_details.*

class ScheduleDetailsFragment : Fragment() {

    private val scheduleDetailsViewModel: ScheduleDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

    }

    private fun setupAdapter() {
        val schedule = scheduleDetailsViewModel.scheduleToView
        val scheduleDetailsAdapter = schedule?.let {
            ScheduleDetailsAdapter(
                context!!,
                it
            )
        }

        if (schedule?.clientsPerDay!!.isEmpty()) {
            days_list.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }

        with(days_list as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleDetailsAdapter
        }
    }


}


