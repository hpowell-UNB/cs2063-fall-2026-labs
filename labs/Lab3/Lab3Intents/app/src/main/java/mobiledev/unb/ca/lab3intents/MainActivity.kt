package mobiledev.unb.ca.lab3intents

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val startButton: Button = findViewById(R.id.btnStart)
        startButton.setOnClickListener {
            val intent = Intent(this, ExternalCallsActivity::class.java)
            try {
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                Log.e(TAG, "Unable to start the activity")
            }
        }
    }

    companion object {
        // String for LogCat documentation
        private const val TAG = "Main Activity"
    }
}