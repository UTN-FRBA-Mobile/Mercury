package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import kotlin.reflect.KProperty

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

        scheduleDetailsViewModel.scheduleToView?.let {
            loadScheduleToViewFromViewModel(it)
            scheduleDetailsViewModel.scheduleToView = null
        }
    }

    private fun loadScheduleToViewFromViewModel(it: Schedule) {

    }

}


