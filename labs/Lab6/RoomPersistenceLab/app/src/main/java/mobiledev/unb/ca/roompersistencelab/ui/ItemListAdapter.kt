package mobiledev.unb.ca.roompersistencelab.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobiledev.unb.ca.roompersistencelab.R
import mobiledev.unb.ca.roompersistencelab.db.entities.Item

class ItemListAdapter : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(ItemDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.item_text)
        private val numberTextView: TextView = itemView.findViewById(R.id.number_text)

        fun bind(item: Item) {
            // TODO
            //  Set the nameTextView and numberTextView text
            //  using the name and number from the item
        }
    }

    // DiffUtil handles efficient item updates instead of recreating the whole list
    object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}