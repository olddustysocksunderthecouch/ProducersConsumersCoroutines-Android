package net.studydrive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lets.address.R
import com.lets.address.data.entities.Location
import com.lets.address.utility.utils.DateConverter
import kotlinx.android.synthetic.main.holder_location.view.*

class EventsAdapter(
        val context: Context,
        items: List<ItemModel>

) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    var items: List<ItemModel> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_location, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun bind() {
            val location = locations[adapterPosition]
            itemView.apply {
                val geofenceEvent = "${renameEventType(location.eventType)} ${location.geofenceTriggered}"
                tvTime.text = DateConverter.readableDate(location.geofenceTime)
                tvGeofenceData.text = geofenceEvent
            }
        }
    }


}