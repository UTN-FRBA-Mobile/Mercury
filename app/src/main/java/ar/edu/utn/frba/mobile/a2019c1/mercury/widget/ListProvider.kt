package ar.edu.utn.frba.mobile.a2019c1.mercury.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import ar.edu.utn.frba.mobile.a2019c1.mercury.R
import ar.edu.utn.frba.mobile.a2019c1.mercury.db.DataSnapshotAdapter
import ar.edu.utn.frba.mobile.a2019c1.mercury.db.Database
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Schedule
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Client
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.VisitOnDate
import ar.edu.utn.frba.mobile.a2019c1.mercury.services.VisitsService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ListProvider(val context: Context, intent: Intent): RemoteViewsService.RemoteViewsFactory {
    private val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    private var visits: MutableList<VisitOnDate> = VisitsService.visits
    private var scheduleListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val data = DataSnapshotAdapter().toHashMapList(dataSnapshot)
            val schedules = data.map { Schedule.buildFromDatabase(it) } .toMutableList()
            val visits = schedules.filter { it.isActive(LocalDateTime.now()) }.flatMap { it.nextVisitDates(LocalDate.now()) }
            val visitsToShow = visits.filter { it.isFromDay(LocalDate.now()) }
                .ifEmpty { visits.filter { it.isFromDay(LocalDate.now().plusDays(1)) } }
                .filter { it.isAfter(LocalDateTime.now()) }
            VisitsService.visits.clear()
            VisitsService.visits.addAll(visitsToShow)
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }


    override fun onCreate() {
        Database.db.addValueEventListener(scheduleListener)
      //  visits = VisitsService.visits
    }

    override fun onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
    }

    override fun getCount(): Int {
        return visits.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.widget_visit)
        val visit = visits.get(position)
        remoteView.setTextViewText(R.id.client_name, visit.visit.client.name)
        remoteView.setTextViewText(R.id.client_visit_time, visit.visit.timeToVisit.toString())
        return remoteView
    }

    override fun getLoadingView(): RemoteViews? {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        Database.db.addValueEventListener(scheduleListener)
        //visits = VisitsService.visits
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}