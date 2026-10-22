package mobiledev.unb.ca.threadinglab.utils

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mobiledev.unb.ca.threadinglab.models.Course
import kotlin.collections.ArrayList

/**
 * Class used to represent the high level JSON courses element
 */
@Serializable
data class CourseResponse(
    val courses: List<CourseItem>
)


/**
 * Class used to represent a single item in the JSON courses list
 */
@Serializable
data class CourseItem(val courseId: String,
                      val name: String,
                      val description: String)

class JsonUtils(context: Context) {
    // Reusable and thread-safe JSON configuration
    private val jsonConfig = Json {
        ignoreUnknownKeys = true // Prevents crashes if the API adds new fields later
    }

    // Publicly accessible list of courses
    // The setter method is private and can only be accessed by this class
    var courses: ArrayList<Course> = ArrayList()
        private set

    private fun processJSON(context: Context) {
        val jsonString = loadJSONFromAssets(context)
        jsonString?.let { jsonString ->
            val root = jsonConfig.decodeFromString<CourseResponse>(jsonString.trimIndent())
            root.courses.forEach { courseItem ->
                // TODO 2
                //  1. Create a Course object from the courseItem object
                //  2. Add the Course object to courses ArrayList
            }
        }
    }

    private fun loadJSONFromAssets(context: Context): String? {
        // TODO 1
        //  1. Obtain an instance of the AssetManager class from the referenced context
        //    (https://developer.android.com/reference/android/content/Context#getAssets())
        //     NOTE: Kotlin makes use of Property Syntax mapping to getAssets().  For example: « context.assets »
        //  2. Open the CS_JSON_FILE from the assets folder
        //     (https://developer.android.com/reference/android/content/res/AssetManager)
        //  3. Read the file contents into a string
        //     HINT:
        //      A BufferedReader (https://www.geeksforgeeks.org/read-from-files-using-bufferedreader-in-kotlin/)
        //      or InputStream (https://www.baeldung.com/kotlin/inputstream-to-string)
        //      works well in this case
        TODO("Not yet implemented")
    }

    companion object {
        private const val CS_JSON_FILE = "CS.json"
    }

    // Initializer to read our data source (JSON file) into an array of course objects
    init {
        processJSON(context)
    }
}