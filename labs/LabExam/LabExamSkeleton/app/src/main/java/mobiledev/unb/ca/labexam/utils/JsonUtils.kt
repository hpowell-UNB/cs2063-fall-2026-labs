package mobiledev.unb.ca.labexam.utils

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mobiledev.unb.ca.labexam.models.Event
import java.io.IOException
import kotlin.collections.ArrayList

@Serializable
data class EventResponse(
    val events: List<EventItem>
)


@Serializable
data class EventItem(val year: String,
                     val number: String,
                     val hostCity: String,
                     val dates: String,
                     val wikipediaLink: String)

class JsonUtils(context: Context) {
    // Reusable and thread-safe JSON configuration
    private val jsonConfig = Json {
        ignoreUnknownKeys = true // Prevents crashes if the API adds new fields later
    }

    // Publicly accessible list of courses
    // The setter method is private and can only be accessed by this class
    var events: ArrayList<Event> = ArrayList()
        private set

    private fun processJSON(context: Context) {
        val jsonString = loadJSONFromAssets(context)
        jsonString?.let { jsonString ->
            val root = jsonConfig.decodeFromString<EventResponse>(jsonString.trimIndent())
            root.events.forEach { eventItem ->
                val event = Event(eventItem.year,
                    eventItem.number,
                    eventItem.hostCity,
                    eventItem.dates,
                    eventItem.wikipediaLink)

                // Add new Event to the events ArrayList
                events.add(event)
            }
        }
    }

    private fun loadJSONFromAssets(context: Context): String? {
        return try {
            context.assets.open(INPUT_JSON_FILE)
                .bufferedReader()
                .use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    companion object {
        private const val INPUT_JSON_FILE = "winter_games.json"
    }

    // Initializer to read our data source (JSON file) into an array of course objects
    init {
        processJSON(context)
    }
}