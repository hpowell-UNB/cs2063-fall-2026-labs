package mobiledev.unb.ca.broadcastreceiverlab

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    // Attributes for storing the file photo path
    private lateinit var currentPhotoPath: String

    // Activity listeners
    private lateinit var cameraActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionResultLauncher: ActivityResultLauncher<String>

    // Attributes for working with an alarm
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmReceiverIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cameraButton: Button = findViewById(R.id.btnCamera)
        cameraButton.setOnClickListener {
            dispatchTakePhotoIntent()
        }

        // Register the activity listener
        setCameraActivityResultLauncher()

        // TODO (NOTIFICATION): Remove when permissions are being checked
        initAlarmValues()

        // TODO (NOTIFICATION): Ensure the permissions are set for posting notifications
        // checkPermissionAndScheduleAlarm()

        // TODO: Set the battery filter intents and
        //   register the inner broadcast receiver
    }

    // Permissions functions
    private fun setRequestPermissionResultLauncher() {
        // Handle the request permission launcher result
        requestPermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "Permission granted")
                initAlarmValues()
            } else {
                Log.i(TAG, "Permission denied")
                showToast(getString(R.string.permission_denied))
            }
        }
    }

    private fun checkPermissionAndScheduleAlarm() {
        setRequestPermissionResultLauncher()

        // Check and request permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33)
            when {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                    initAlarmValues()
                }
                else -> {
                    // Request permission access
                    requestPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // No runtime permission is needed for notifications when using Android versions below 13
            initAlarmValues()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            this@MainActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    // Alarm Methods
    private fun initAlarmValues() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        // TODO - Define the alarm pending intent
        //    Hint: Use alarmReceiverIntent for this

        // Start the alarm
        startAlarm()
    }

    private fun startAlarm() {
        // TODO: Create an inexact repeating alarm which will wake up
        //  the device and fire the alarm receiver intent
        Log.i(TAG, "Alarm Started")
    }

    private fun cancelAlarm() {
        // TODO: Cancel the alarm
        Log.i(TAG, "Alarm Cancelled")
    }

    // Camera functions
    private fun setCameraActivityResultLauncher() {
        // Handle the image capture result
        cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                galleryAddPic()
            }
        }
    }

    private fun dispatchTakePhotoIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there is a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Set the File object used to save the photo
                val photoFile: File? = try {
                    createImageFile()
                } catch (_: IOException) {
                    // Error occurred while creating the File
                    Log.e(TAG, "Exception found when creating the photo save file")
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "$packageName.provider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                    // Calling this method allows us to capture the return code
                    cameraActivityResultLauncher.launch(takePictureIntent)
                }
            } ?: Toast.makeText(this,
                getString(R.string.error_loading_camera),
                Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFileName: String = "IMG_" + timeStamp + "_"  // Does this need to be a global value?

        return File.createTempFile(
            imageFileName,  // prefix
            ".jpg",  // suffix
            storageDir // directory
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
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

        val contentValues = getContentValues().apply{
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, true)
        }

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
        MediaScannerConnection.scanFile(this@MainActivity,
            arrayOf(file.toString()),
            arrayOf(file.name),
            null)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val TIME_STAMP_FORMAT = "yyyyMMdd_HHmmss"
    }
}