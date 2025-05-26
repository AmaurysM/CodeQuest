package com.amaurysdm.codequest.customcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Preview(name = "Level 0 Preview")
@Composable
fun LevelCardPreview(){


    Row(modifier = Modifier.background(Color.White)){
        LevelCard(0,true, false, { }, 2,true)
        LevelCard(10,false, false, { }, 2,true)
        LevelCard(100,false, true, { }, 2,true)
    }

}

@Composable
fun LevelCard(
    levelNumber: Int = 0,
    isCompleted: Boolean = false,
    isLocked: Boolean = false,
    onClick: () -> Unit = {},
    animationDelay: Long = 2000,
    forceVisible: Boolean = false // <- for preview
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isVisible by remember { mutableStateOf(forceVisible) }

    // Entrance animation with delay
    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
    }

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.9f
            isVisible -> 1f
            else -> 0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isCompleted) 5f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "rotation"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .rotate(rotation)
                .clickable {
                    if (!isLocked) onClick() else {
                    }
                }
                .background(
                    color =  when {
                        isLocked -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 1f)
                        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    },
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = when {
                        isLocked -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    },
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    isLocked -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    isCompleted -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = levelNumber.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Completed",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = levelNumber.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Glowing effect for completed levels
                if (isCompleted) {
                    val glowAlpha by animateFloatAsState(
                        targetValue = 0.3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "glow"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                                RoundedCornerShape(20.dp)
                            )
                    )
                }
            }
        }
    }
}