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
        val visit = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.visit_for_schedule_detail, parent, false)
        }
        val clientName = convertView!!.findViewById(R.id.client_name) as TextView
        val visitTime = convertView.findViewById(R.id.client_visit_time) as TextView
        clientName.text = visit.client.name
        visitTime.text = visit.timeToVisit.toString()
        return convertView
    }
}