package com.amaurysdm.codequest.customcomposables

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable

// Animation for the title
@Composable
fun TitleAnimation(): Pair<Float, Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val rotation = infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        label = "rotation logo",
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                (-15f) at 0 using LinearOutSlowInEasing
                15f at 1000 using LinearOutSlowInEasing
                (-15f) at 2000 using LinearOutSlowInEasing
            }
        )
    )


    val bobbing = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        label = "bobbing logo",
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                50f at 0
                0f at 2000 using FastOutLinearInEasing
                50f at 4000
            }
        )
    )

    return Pair(bobbing.value, rotation.value)

}