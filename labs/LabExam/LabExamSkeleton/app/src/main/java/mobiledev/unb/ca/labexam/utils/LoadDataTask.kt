package mobiledev.unb.ca.labexam.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mobiledev.unb.ca.labexam.Constants
import mobiledev.unb.ca.labexam.DetailActivity
import mobiledev.unb.ca.labexam.EventListAdapter
import mobiledev.unb.ca.labexam.models.Event
import kotlin.time.Duration.Companion.milliseconds

class LoadDataTask(private val parentActivity: AppCompatActivity) {
    private val appContext: Context = parentActivity.applicationContext
    private lateinit var recyclerView: RecyclerView
    private lateinit var circularProgressIndicator: CircularProgressIndicator

    fun setRecyclerView(recyclerView: RecyclerView): LoadDataTask {
        this.recyclerView = recyclerView
        return this
    }

    fun setCircularProgressIndicator(circularProgressIndicator: CircularProgressIndicator): LoadDataTask {
        this.circularProgressIndicator = circularProgressIndicator
        return this
    }

    fun execute() {
        MainScope().launch(Dispatchers.IO) {
            // TODO
            //  Show the circular progress indicator

            // TODO
            //  Load the data from the JSON assets file and retrieve its list of events

            launch(Dispatchers.Main) {
                // Simulate a long-running operation
                for (i in 0..DOWNLOAD_TIME) {
                    sleep()
                    // TODO
                    //  Update the progress attribute of the progress bar
                }

                // TODO
                //  Call the updateDisplay method update the UI with the results
            }
        }
    }

    private suspend fun sleep() {
        delay(500.milliseconds)
    }

    private fun updateRecyclerView(eventList: ArrayList<Event>) {
        val eventAdapter = EventListAdapter (
            // This will loop through each course element and apply an action for the listener attribute
            onRowClicked = { event ->
                // TODO
                //  Create an explicit intent to launch DetailActivity
                //  from the parentActivity object passed into the class constructor
                //  NOTE: Also include any necessary extra values
            }
        )

        recyclerView.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(appContext)
        }

        // TODO
        //  Push the event list into eventAdapter using submitList()
    }

    private fun updateDisplay(eventList: ArrayList<Event>) {
        // Update the RecyclerView
        updateRecyclerView(eventList)

        // TODO
        //  Hide the circular progress indicator
        
        // TODO
        //  Create a Toast indicating that the file has been loaded
    }

    companion object {
        private const val TAG = "LoadDataTask"
        private const val DOWNLOAD_TIME = 10 // Download time simulation
    }
}