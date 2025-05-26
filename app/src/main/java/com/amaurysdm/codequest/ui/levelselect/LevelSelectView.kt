package com.amaurysdm.codequest.ui.levelselect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.customcomposables.FloatingParticles
import com.amaurysdm.codequest.customcomposables.LevelCard
import com.amaurysdm.codequest.customcomposables.OutlinedText
import com.amaurysdm.codequest.model.Level
import kotlinx.coroutines.delay
import kotlin.random.Random
import com.amaurysdm.codequest.customcomposables.ParallaxBackground

@Preview(showBackground = true)
@Composable
fun LevelSelectView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    levelSelectViewModel: LevelSelectViewmodel = viewModel()
) {
    val levelFlow = levelSelectViewModel.levels.collectAsState(emptyList())
    var isVisible by remember { mutableStateOf(false) }

    // Entrance animation
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Enhanced background with parallax effect
        ParallaxBackground()

        // Glass morphism overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                initialOffsetY = { it }
            ),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header section with back button and title
                HeaderSection(
                    onBackClick = { levelSelectViewModel.leaveLevelSelect(navController) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Progress indicator
                ProgressSection(
                    completedLevels = levelFlow.value.count { it.completed },
                    totalLevels = levelFlow.value.size
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Level grid
                LevelGrid(
                    levels = levelFlow.value,
                    onLevelClick = { index ->
                        levelSelectViewModel.navigateToLevel(navController, index)
                    },
                    getLevelData = { index -> levelSelectViewModel.getLevel(index) }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Floating particles effect
        FloatingParticles()
    }
}


@Composable
private fun HeaderSection(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button with glass effect
        Surface(
            onClick = onBackClick,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Animated title
        OutlinedText(
            text = "LEVEL SELECT",
            fontSize = 32.sp,
            textColor = Color.White,
            outlineColor = MaterialTheme.colorScheme.primary,
            outlineWidth = 3.dp,
            shadowOffset = Offset(4f, 4f),
            shadowColor = Color.Black.copy(alpha = 0.5f),
            enablePulse = true,
            pulseDuration = 3000,
            pulseScale = 0.03f
        )

        // Placeholder for symmetry
        Spacer(modifier = Modifier.size(56.dp))
    }
}

@Composable
private fun ProgressSection(completedLevels: Int, totalLevels: Int) {
    val progress = if (totalLevels > 0) completedLevels.toFloat() / totalLevels else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Progress: $completedLevels / $totalLevels",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Custom progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50),
                                    Color(0xFF8BC34A),
                                    Color(0xFFCDDC39)
                                )
                            )
                        )
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
private fun LevelGrid(
    levels: List<Level>, // Replace with your actual Level type
    onLevelClick: (Int) -> Unit,
    getLevelData: (Int) -> Level // Replace with your actual Level type
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(levels) { index, _ ->
            val currentLevel = getLevelData(index)
            val isCompleted = currentLevel.completed
            val isLocked = if (index == 0) {
                false // First level is never locked
            } else {
                !getLevelData(index - 1).completed // Locked if previous is not completed
            }

            LevelCard(
                levelNumber = index + 1,
                isCompleted = isCompleted,
                isLocked = isLocked,
                onClick = {
                    onLevelClick(index);
                },
                animationDelay = index * 50L
            )
        }
    }
}
