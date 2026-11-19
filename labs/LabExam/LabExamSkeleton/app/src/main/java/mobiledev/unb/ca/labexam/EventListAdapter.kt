package mobiledev.unb.ca.labexam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobiledev.unb.ca.labexam.models.Event

/**
 * Class used to populate each row of the RecyclerView
 * NOTE: This makes use of a Callback Lambda for the listener
 *       which allows you to delegate the click logic back to LoadDataTask
 * @param onRowClicked The onClickListener behavior for each row
 */
class EventListAdapter(
    private val onRowClicked: (Event) -> Unit
) : ListAdapter<Event, EventListAdapter.EventViewHolder>(EventDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return EventViewHolder(view, onRowClicked)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    class EventViewHolder(itemView: View, private val onRowClicked: (Event) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val eventNumberTextView: TextView = itemView.findViewById(R.id.item_title_text_view)
        val eventHostTextView: TextView = itemView.findViewById(R.id.item_subtitle_text_view)
        val visitedCheckbox: CheckBox = itemView.findViewById(R.id.visited_check_box)

        fun bind(event: Event) {
            // TODO
            //  Set the TextView text values to be the number and host city of the event

            // TODO: SharedPreferences
            //  Set the value of visitedCheckbox to be checked if the
            //  value stored in SharedPreferencesHelper for the number of this
            //  EventInfo is true; unchecked otherwise.
            //  If there is no value in the shared preferences then the checkbox should not
            //  be checked (i.e., assume a default value of false for anything not in
            //  the shared preferences).

            // This method is called when a CheckBox is clicked, and its status
            // changes from checked to not checked, or from not checked to checked.
            // isChecked will be true if the CheckBox is now checked, and false if
            // the CheckBox is now not checked.
            visitedCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                // TODO: SharedPreferences
                //   Save the CheckBox selected state using SharedPreferencesHelper
            }

            // Set the onClickListener to the onRowClicked function using the current event info details
            // as selected when the row is clicked
            itemView.setOnClickListener { onRowClicked(event) }
        }
    }

    // DiffUtil handles efficient item updates instead of recreating the whole list
    object EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}