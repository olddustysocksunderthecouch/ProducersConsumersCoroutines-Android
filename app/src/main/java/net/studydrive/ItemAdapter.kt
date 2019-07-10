package net.studydrive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.holder_item.view.*

class ItemAdapter(items: List<ItemModel>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var items: List<ItemModel> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_item, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun bind() {
            val itemModel = items[adapterPosition]
            itemView.apply {
                item_count_textview.text = itemModel.count.toString()
                item_time_textview.text = itemModel.time
            }
        }
    }

}