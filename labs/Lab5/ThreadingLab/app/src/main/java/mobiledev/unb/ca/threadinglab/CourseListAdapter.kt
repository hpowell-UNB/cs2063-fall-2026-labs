package mobiledev.unb.ca.threadinglab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobiledev.unb.ca.threadinglab.models.Course

/**
 * Class used to populate each row of the RecyclerView
 * NOTE: This makes use of a Callback Lambda for the listener
 *       which allows you to delegate the click logic back to LoadDataTask
 * @param onRowClicked The onClickListener behavior for each row
 */
class CourseListAdapter(
    private val onRowClicked: (Course) -> Unit
) : ListAdapter<Course, CourseListAdapter.CourseViewHolder>(CourseDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return CourseViewHolder(view, onRowClicked)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    class CourseViewHolder(itemView: View, private val onRowClicked: (Course) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val courseIdTextView: TextView = itemView.findViewById(R.id.courseId)
        val courseNameTextView: TextView = itemView.findViewById(R.id.courseName)

        fun bind(course: Course) {
            // TODO 2
            //  Set the text for TextViews to use the id and name of the course

            // Set the onClickListener to the onRowClicked function using the current course details
            itemView.setOnClickListener { onRowClicked(course) }
        }
    }

    // DiffUtil handles efficient item updates instead of recreating the whole list
    object CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}