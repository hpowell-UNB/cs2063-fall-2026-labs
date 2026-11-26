package mobiledev.unb.ca.bubblegamelab

import kotlin.math.cos
import kotlin.math.sin
import androidx.core.graphics.toColorInt

data class Particle(
    var x: Float,
    var y: Float,
    var dx: Float,
    var dy: Float,
    val radius: Float,
    val color: Int = "#80FFFFFF".toColorInt()
) {
    var alpha: Int = 255

    companion object {
        // Simulated downward pulling gravity force
        private const val GRAVITY = 0.28f
        // Simulated air resistance / drag coefficient
        private const val FRICTION = 0.95f
    }

    fun update(): Boolean {
        // Adjust the particle velocities
        dy += GRAVITY
        dx *= FRICTION
        dy *= FRICTION

        // Move particle coordinates based on calculated speed vectors
        x += dx
        y += dy

        // Fade opacity out smoothly
        alpha -= 10
        if (alpha < 0) {
            alpha = 0
        }

        return alpha > 0
    }
}

data class ParticleExplosion(
    val particles: ArrayList<Particle> = ArrayList()
) {
    companion object {
        private const val PARTICLE_COUNT = 12
    }

    // Create an explosion of shards bursting out in a circle
    constructor(originX: Float, originY: Float, bubbleRadius: Float) : this() {
        for (i in 0 until PARTICLE_COUNT) {
            // Distribute particles evenly around a 360-degree radius angle
            val angle = (i * (360f / PARTICLE_COUNT)) * (Math.PI / 180f)
            val speed = (3..8).random().toFloat()

            // Randomize particle sizing slightly
            val particleRadius = (2..5).random().toFloat()

            particles.add(
                Particle(
                    x = originX,
                    y = originY,
                    dx = (cos(angle) * speed).toFloat(),
                    dy = (sin(angle) * speed).toFloat(),
                    radius = particleRadius
                )
            )
        }
    }

    // Returns true if there are still visible particles in this explosion instance
    fun update(): Boolean {
        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val particle = iterator.next()
            if (!particle.update()) {
                iterator.remove()
            }
        }
        return particles.isNotEmpty()
    }
}
