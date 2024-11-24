package com.amaurysdm.codequest

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable

@Composable
fun TitleAnimation():Pair<Float, Float>{
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        label = "rotation logo",
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                -15f at 0 using FastOutLinearInEasing
                15f at 1500
                -15f at 3000 using FastOutSlowInEasing
            }
        )
    )


    val bobbing = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        label = "bobbing logo",
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                50f at 0
                -50f at 1500 using FastOutLinearInEasing
                50f at 3000
            }
        )
    )/* */

    return Pair(bobbing.value, rotation.value)

}