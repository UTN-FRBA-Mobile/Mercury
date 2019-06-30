package ar.edu.utn.frba.mobile.a2019c1.mercury

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.mercury.model.Visit
import kotlinx.android.synthetic.main.visit_item_view.view.*

class ScheduleEditionAdapter(
    private val visits: MutableList<Pair<Int, Visit>>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ScheduleEditionAdapter.ViewHolder>() {

    interface OnItemClickListener{
         fun onDeleteItem(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.visit_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = visits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val dayNumber = visits[position].first
        val visit = visits[position].second
        itemView.day_number.text = dayNumber.toString()
        itemView.client_name_list.text = visit.client.name
        itemView.client_visit_time.text = visit.timeToVisit.toString()
            itemView.deleteVisitButton.setOnClickListener{
            onItemClickListener.onDeleteItem(position)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
