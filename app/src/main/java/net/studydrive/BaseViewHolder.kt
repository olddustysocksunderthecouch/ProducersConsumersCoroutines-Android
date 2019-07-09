package net.studydrive

import android.view.View

open class BaseViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    open fun bind() {}
}