package com.amaurysdm.codequest.customcomposables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun FloatingParticles() {
    val particleCount = 20
    val particles = remember {
        List(particleCount) {
            ParticleState(
                x = (0..1000).random().toFloat(),
                y = (0..2000).random().toFloat(),
                speed = Random.nextFloat() * (2f - 0.5f) + 0.5f,
                size = Random.nextFloat() * (6f - 2f) + 2f,
                alpha = Random.nextFloat() * (0.8f - 0.3f) + 0.3f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawCircle(
                color = Color.White.copy(alpha = particle.alpha),
                radius = particle.size,
                center = Offset(
                    x = particle.x % size.width,
                    y = (particle.y + System.currentTimeMillis() * particle.speed * 0.001f) % size.height
                )
            )
        }
    }
}

private data class ParticleState(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Float,
    val alpha: Float
)