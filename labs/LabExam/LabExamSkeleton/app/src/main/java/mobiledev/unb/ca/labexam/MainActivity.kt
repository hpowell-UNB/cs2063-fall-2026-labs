package mobiledev.unb.ca.labexam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mobiledev.unb.ca.labexam.helpers.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Force light mode when running the app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initSharedPreferences()

        // TODO
        //  Get a reference to the RecyclerView

        // TODO
        //  Get a reference to the circular progress indicator

        // TODO
        //  Create an instance of LoadDataTask using this activity in the constructor
        //  and use the setters to pass in the recycler view object and
        //  circular progress bar needed during execution
    }

    private fun initSharedPreferences() {
        // Initialize SharedPreferencesManager
        // NOTE: Complete the regular flow first and then complete these set of TODOs.
        //       Calling this without making any changes will not affect the application.
        SharedPreferencesHelper.init(applicationContext)
    }
}