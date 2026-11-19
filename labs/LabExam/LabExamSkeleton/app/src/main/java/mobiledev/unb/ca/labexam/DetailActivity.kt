package mobiledev.unb.ca.labexam

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TODO
        //  Get the intent that started this activity along with the extras added to it

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            // TODO
            //  Set the title of the action bar to be the event number
            setDisplayHomeAsUpEnabled(true)
        }

        // TODO
        //  Set the details for the host city text view

        // TODO
        //  Set the details for the year text view

        // TODO
        //  Set the details for the dates text view

        // TODO
        //  Set an onClickListener such that when this button is clicked, an implicit intent is started
        //  to open the wikipedia URL in a web browser. Be sure to check that there is
        //  an application installed that can handle this intent before starting it.
        //  If the intent can't be started, show a toast indicating this.
        //  Hints:
        //   https://developer.android.com/reference/android/content/Intent.html#resolveActivity(android.content.pm.PackageManager)
        //   https://developer.android.com/guide/components/intents-common.html#Browser
        //   https://developer.android.com/reference/android/net/Uri.html#parse(java.lang.String)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Destroy the activity and go back to the parent activity
        // This is specified by using android:parentActivityName=".MainActivity" in the
        // AndroidManifest.xml file
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}