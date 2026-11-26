package mobiledev.unb.ca.bubblegamelab

import android.graphics.Bitmap

/**
 * Data class to used store bubble properties
 */
data class Bubble(
    var x: Float, // Center point X
    var y: Float, // Center point Y
    val radius: Float,
    val diameter: Float,
    var dx: Float,
    var dy: Float,
    var rotation: Float,
    val rotationSpeed: Float,
    val bitmap: Bitmap
) {
    fun move() {
        x += dx
        y += dy
        rotation += rotationSpeed
    }

    fun isOffScreen(screenWidth: Int, screenHeight: Int): Boolean {
        return (x + radius < 0 || x - radius > screenWidth ||
                y + radius < 0 || y - radius > screenHeight)
    }

    fun intersects(touchX: Float, touchY: Float): Boolean {
        val deltaX = x - touchX
        val deltaY = y - touchY

        // TODO
        //  Update the return statement to return true if the Bubble 
        //  intersects position (touchX,touchY) otherwise false
        return true
    }
}
