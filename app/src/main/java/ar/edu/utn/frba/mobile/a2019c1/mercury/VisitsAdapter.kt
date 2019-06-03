package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.content.Context
import android.widget.ArrayAdapter
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit

import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class VisitsAdapter(context: Context, visits: MutableList<Visit>) : ArrayAdapter<Visit>(context, 0, visits) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val visit = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.visit_for_schedule_detail, parent, false)
        }
        // Lookup view for data population
        val clientName = convertView!!.findViewById(R.id.client_name) as TextView
        val visitTime = convertView.findViewById(R.id.client_visit_time) as TextView
        // Populate the data into the template view using the data object
        clientName.text = visit.client.name
        visitTime.text = visit.timeToVisit.toString()
        // Return the completed view to render on screen
        return convertView
    }
}