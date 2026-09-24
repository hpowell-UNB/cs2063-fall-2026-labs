package mobiledev.unb.ca.lab3intents

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class ExternalCallsActivity : AppCompatActivity() {
    // Attributes for storing the file photo path
    private lateinit var currentPhotoPath: String

    // Activity listeners
    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_external_calls)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Register the activity listener
        setCameraActivityResultLauncher()

        // Register the button listeners
        val cameraButton: Button = findViewById(R.id.btnCamera)
        cameraButton.setOnClickListener {
            dispatchTakePhotoIntent()
        }

        val emailButton: Button = findViewById(R.id.btnEmail)
        emailButton.setOnClickListener {
            dispatchSendEmailIntent()
        }

        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            // TODO: Add intent to navigate back to MainActivity
        }
    }

    // Email functions
    private fun dispatchSendEmailIntent() {
        // Handle the send email intent
        // TODO: Complete implementation
    }

    // Camera Functions
    private fun dispatchTakePhotoIntent() {
        // Handle the photo intent
        // TODO: Complete implementation
    }

    private fun setCameraActivityResultLauncher() {
        // TODO: Handle the image capture result
    }

    private fun galleryAddPic() {
        Log.d(TAG, "Saving image to the gallery")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 (API 29) and above
            mediaStoreAddPicToGallery()
        } else {
            // Pre Android 10
            mediaScannerAddPicToGallery()
        }
        Log.i(TAG, "Image saved!")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun mediaStoreAddPicToGallery() {
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)

        val contentValues = getContentValues()
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        contentValues.put(MediaStore.Images.Media.IS_PENDING, true)

        val resolver = contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { uri ->
            // The use function is a Kotlin extension function that automatically closes
            // the OutputStream after the block of code inside it has executed.
            // This eliminates the need for a separate try-catch block to
            // close the outputStream.
            resolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            } ?: Log.e(TAG, "Error saving the file: outputStream is null")

            contentValues.apply {
                put(MediaStore.Images.Media.IS_PENDING, false)
                resolver.update(uri, this, null, null)
            }
        }
    }

    private fun getContentValues() : ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }
    }

    private fun mediaScannerAddPicToGallery() {
        val file = File(currentPhotoPath)
        MediaScannerConnection.scanFile(this@ExternalCallsActivity,
            arrayOf(file.toString()),
            arrayOf(file.name),
            null)
    }
    companion object {
        // String for LogCat documentation
        private const val TAG = "External Calls Activity"
    }
}