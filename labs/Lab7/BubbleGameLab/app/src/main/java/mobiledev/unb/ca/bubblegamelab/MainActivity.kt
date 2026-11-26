package mobiledev.unb.ca.bubblegamelab

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val SOUND_POOL_MAX_STREAMS = 10
        private const val STREAM_TYPE = AudioManager.STREAM_MUSIC
    }

    // Custom View used for the game
    private lateinit var gameView: GameView
    private lateinit var bubbleCountTextView: TextView
    private lateinit var gestureDetector: GestureDetector

    private var soundPool: SoundPool? = null
    private var poppingSoundId = 0
    private var streamVolume = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Bind the custom view and UI elements
        gameView = findViewById(R.id.game_view)
        bubbleCountTextView = findViewById(R.id.bubbles_text)

        // Set up the GameView listeners to handle audio and UI feedback
        setupGameEventListeners()
    }

    private fun setupGameEventListeners() {
        // Automatically updates the text whenever a bubble spawns or pops/drifts away
        gameView.bubbleCountListener = { count ->
            // TODO
            //  Set the listener action to update the bubbleCountTextView text to display
            //  the number of bubbles
        }

        // Plays the sound effect when a bubble is tapped
        gameView.onBubblePoppedListener = {
            // TODO
            //  Set the listener action to play the popping sound when the bubble is
            //  popped by user
            //  HINT: Use the streamVolume for left and right volume parameters
        }
    }

    override fun onResume() {
        super.onResume()
        initSound(applicationContext)
    }

    override fun onPause() {
        super.onPause()
        // Stop animations immediately to preserve system resources
        gameView.stopAnimation()

        // TODO
        //  Release all SoundPool resources (including sounds)
    }

    private fun initSound(context: Context) {
        createNewSoundPool()
        setStreamVolume(context)

        // TODO
        //  Load the sound from R.raw.bubble_pop with a priority of 1
        //  Store the value in poppingSoundId
    }

    private fun createNewSoundPool() {
        // TODO
        //  Make a new sound pool allowing up to SOUND_POOL_MAX_STREAMS streams
        //  Store the object value in soundPool

        // Set a SoundPool OnLoadCompletedListener that calls setupGestureDetector()
        soundPool?.setOnLoadCompleteListener { _: SoundPool?, _: Int, status: Int ->
             // TODO
             //  If the status returns 0 call setGestureDetector
             //  Else log a message that the sound could not be loaded and finish
         }
    }

    private fun setStreamVolume(context: Context) {
        val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        val currentVolumeIndex = audioManager.getStreamVolume(STREAM_TYPE).toFloat()
        val maxVolumeIndex = audioManager.getStreamMaxVolume(STREAM_TYPE).toFloat()

        streamVolume = if (maxVolumeIndex > 0) {
            currentVolumeIndex / maxVolumeIndex
        } else {
            0f
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return ::gestureDetector.isInitialized && gestureDetector.onTouchEvent(event)
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(motionEvent: MotionEvent): Boolean = true

            override fun onFling(
                event1: MotionEvent?, event2: MotionEvent,
                velocityX: Float, velocityY: Float,
            ): Boolean {
                // TODO
                //  Forward the flings directly to the game view's handleFling function
                return true
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                // TODO
                //  Forward single taps directly to the game view's handleTap function
                return true
            }
        })
    }
}
