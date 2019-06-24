package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.db.Database
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_schedule_edition.fab
import kotlinx.android.synthetic.main.fragment_schedule_list.*

class ScheduleListFragment : Fragment() {

    private val viewModel: ScheduleViewModel by activityViewModels()
    private lateinit var onAddScheduleButtonClicked: () -> Unit
    private lateinit var onViewScheduleButtonClicked: (Schedule) -> Unit
    private lateinit var onEditScheduleButtonClicked: (Schedule) -> Unit
    lateinit var scheduleListAdapter : ScheduleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Database.db.orderByKey().addValueEventListener(scheduleListener)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setActionBarTitle(getString(R.string.LIST_SCHEDULE__ACTION_BAR_TITLE))
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        fab.setOnClickListener { onAddScheduleButtonClicked() }
    }

    private fun setupAdapter() {
        val schedules = viewModel.schedules
        scheduleListAdapter = ScheduleListAdapter(
            context!!,
            schedules,
            this.onViewScheduleButtonClicked,
            this.onEditScheduleButtonClicked,
            this::deleteSchedule)

        if (schedules.isEmpty()) {
            schedule_list.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }

        with(schedule_list as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleListAdapter
        }
    }

    private fun deleteSchedule(scheduleToDelete: Schedule) {
        viewModel.remove(scheduleToDelete)
        setupAdapter()
    }

    fun setOnAddScheduleButtonClicked(onAddScheduleButtonClicked: () -> Unit) {
        this.onAddScheduleButtonClicked = onAddScheduleButtonClicked
    }

    fun setOnViewScheduleButtonClicked(onViewScheduleButtonClicked: (Schedule) -> Unit) {
        this.onViewScheduleButtonClicked = onViewScheduleButtonClicked
    }

    fun setOnEditScheduleButtonClicked(onEditScheduleButtonClicked: (Schedule) -> Unit) {
        this.onEditScheduleButtonClicked = onEditScheduleButtonClicked
    }

    private fun loadTaskList(dataSnapshot: DataSnapshot) {

        val schedules = dataSnapshot.children.iterator()
        if (schedules.hasNext()) {
            viewModel.schedules.clear()
            val listIndex = schedules.next()
            val itemsIterator = listIndex.children.iterator()
            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val map = currentItem.getValue() as HashMap<String, Any>
                val schedule = Schedule.buildFromDatabase(map)
                viewModel.schedules!!.add(schedule)
            }
        }
        scheduleListAdapter.notifyDataSetChanged()
        if (viewModel.schedules.isNotEmpty()) {
            schedule_list.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
    }

    var scheduleListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            loadTaskList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }


}