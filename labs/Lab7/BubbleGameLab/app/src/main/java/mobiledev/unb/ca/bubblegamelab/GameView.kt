package mobiledev.unb.ca.bubblegamelab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.AudioManager
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import androidx.core.graphics.scale
import kotlin.time.Duration.Companion.milliseconds

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val REFRESH_RATE = 60f
    }

    // Tracks current bubbles on the screen
    private val bubbles = CopyOnWriteArrayList<Bubble>()

    // Tracks current active explosions on screen
    private val explosions = CopyOnWriteArrayList<ParticleExplosion>()

    // Dedicated primitive paint brush reused across all active bubbles
    private val painter = Paint(Paint.ANTI_ALIAS_FLAG)

    // Dedicated primitive paint brush reused across all active particle streams
    private val particlePainter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var animationJob: Job? = null
    private val viewScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Listener event for updating the count when bubbles are added or removed
    // The actions to be performed are defined in MainActivity.setupGameEventListeners()
    var bubbleCountListener: ((Int) -> Unit)? = null

    // Listener event for handling sounds when the bubble is popped
    // The actions to be performed are defined in MainActivity.setupGameEventListeners()
    var onBubblePoppedListener: (() -> Unit)? = null

    private val baseBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.b64)

    fun startAnimation() {
        animationJob?.cancel()
        animationJob = viewScope.launch {
            while (isActive) {
                // Process Active Bubbles
                val bubbleIterator = bubbles.iterator()
                var listChanged = false

                while (bubbleIterator.hasNext()) {
                    // TODO
                    //  Implement move logic
                    //  1. Obtain an instance of the current bubble from the iterator
                    //  2. Move the bubble
                    //      HINT: Inspect the available functions for the bubble for the one to use
                    //  3. Check to see if the bubble is off the screen (again check the available bubble functions)
                    //      HINT: You can use the build in View attributes of width and height here
                    //  4. If the bubble is off the screen remove it from the list of bubbles
                    //     and set listChanged to true
                }

                if (listChanged) {
                    // Trigger an update to the number of bubbles text view as defined by MainActivity
                    bubbleCountListener?.invoke(bubbles.size)
                }

                // Process Explosions
                val explosionIterator = explosions.iterator()
                while (explosionIterator.hasNext()) {
                    val explosion = explosionIterator.next()
                    if (!explosion.update()) {
                        explosions.remove(explosion)
                    }
                }

                invalidate()
                delay(16.milliseconds)
            }
        }
    }

    fun stopAnimation() {
        animationJob?.cancel()
        animationJob = null
    }

    fun handleTap(x: Float, y: Float) {
        val tappedBubble = bubbles.firstOrNull { bubble ->
            bubble.intersects(x, y)
        }

        if (tappedBubble != null) {
            // Trigger particle blast centered over the exact bubble placement vector
            explosions.add(ParticleExplosion(tappedBubble.x, tappedBubble.y, tappedBubble.radius))

            // TODO
            //  1. Remove the bubble object from the bubbles list
            //  2. Invoke the onBubblePoppedListener
            //  3. Invoke the bubbleCountListener with current size of the bubbles list
            //     HINT - See startAnimation for an example should you need a reference
        } else {
            createBubbleAt(x, y)
        }
    }

    fun handleFling(x: Float, y: Float, vX: Float, vY: Float) {
        bubbles.forEach { bubble ->
            // TODO
            //  If a fling gesture intersects at points (x,y) adjust the bubble's
            //  speed and direction based on vX and vY velocity values from this gesture
        }
    }

    // Change the bubble's speed and direction
    fun deflect(bubble: Bubble, vX: Float, vY: Float) {
        bubble.dx = vX / REFRESH_RATE
        bubble.dy = vY / REFRESH_RATE
    }

    private fun createBubbleAt(startX: Float, startY: Float) {
        val sizeMultiplier = (2..4).random()
        val size = 64 * sizeMultiplier
        val radius = size / 2f

        val scaledBmp = baseBitmap.scale(size, size)

        var dx = 0f; var dy = 0f
        while (dx == 0f && dy == 0f) {
            // TODO
            //  Set dx and dy to indicate movement direction and speed
            //  Limit speed in the x and y direction to [-3..3] pixels per movement
        }

        // TODO
        //  Update the value of rotationSpeed from 0 to be in the range of [1..5]
        val rotationSpeed = 0f

        bubbles.add(Bubble(
            x = startX, y = startY, radius = radius, diameter = size.toFloat(),
            dx = dx, dy = dy, rotation = 0f, rotationSpeed = rotationSpeed,
            bitmap = scaledBmp
        ))
        bubbleCountListener?.invoke(bubbles.size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw Active Floating Bubbles
        bubbles.forEach { bubble ->
            // canvas.withSave is an extension function which safely saves and
            // restores a Canvas state around a block of drawing code.
            // Any Canvas functions can be referenced directly without needing the qualifier (canvas.)
            //  For example: canvas.drawCircle can be written as drawCircle inside the withSave block
            canvas.withSave {
                // TODO
                //  Rotate the bubble
                //   HINT - You can use Canvas's builtin rotate function

                // TODO
                //  Draw the bitmap at it's new location
                //   HINT - You can use Canvas's builtin drawBitmap function
            }
        }

        // Draw Multi-Particle Explosions
        explosions.forEach { explosion ->
            explosion.particles.forEach { particle ->
                particlePainter.apply {
                    color = particle.color
                    alpha = particle.alpha // Dynamically inject individual alpha frame properties
                }
                canvas.drawCircle(particle.x, particle.y, particle.radius, particlePainter)
            }
        }
    }

    // Perform cleanup when the view is removed
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
        baseBitmap.recycle()
        bubbles.forEach { bubble ->
            bubble.bitmap.recycle()
        }
        bubbles.clear()
        explosions.clear()
    }
}
