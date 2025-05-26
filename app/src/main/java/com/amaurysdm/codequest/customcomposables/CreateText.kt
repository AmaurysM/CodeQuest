package com.amaurysdm.codequest.customcomposables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Enhanced outlined text component with customizable styling and animations
 */
@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = 48.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    outlineWidth: Dp = 4.dp,
    shadowOffset: Offset = Offset(4f, 4f),
    shadowColor: Color = Color.Black.copy(alpha = 0.3f),
    shadowBlur: Float = 8f,
    // Animation properties
    enableBobbing: Boolean = false,
    bobbingDuration: Int = 2000,
    bobbingRange: Dp = 8.dp,
    enableRotation: Boolean = false,
    rotationDuration: Int = 4000,
    enablePulse: Boolean = false,
    pulseDuration: Int = 1500,
    pulseScale: Float = 0.1f
) {
    val density = LocalDensity.current

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "text_animations")

    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (enableBobbing) with(density) { bobbingRange.toPx() } else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(bobbingDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (enableRotation) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(rotationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (enablePulse) 1f + pulseScale else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(pulseDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(
        modifier = modifier
            .offset(y = with(density) { bobbingOffset.toDp() })
            .rotate(rotation)
            .size(
                width = (fontSize.value * text.length * 0.6f * pulseScale).dp,
                height = (fontSize.value * 1.2f * pulseScale).dp
            )
    ) {
        drawOutlinedText(
            text = text,
            textColor = textColor,
            outlineColor = outlineColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            outlineWidth = outlineWidth,
            shadowOffset = shadowOffset,
            shadowColor = shadowColor,
            shadowBlur = shadowBlur,
            scale = pulseScale,
            density = density
        )
    }
}

/**
 * Draws outlined text with shadow effects
 */
private fun DrawScope.drawOutlinedText(
    text: String,
    textColor: Color,
    outlineColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    outlineWidth: Dp,
    shadowOffset: Offset,
    shadowColor: Color,
    shadowBlur: Float,
    scale: Float,
    density: androidx.compose.ui.unit.Density
) {
    val textSizePx = with(density) { fontSize.toPx() * scale }
    val outlineWidthPx = with(density) { outlineWidth.toPx() }

    // Create paints
    val shadowPaint = android.graphics.Paint().apply {
        this.textSize = textSizePx
        this.color = shadowColor.toArgb()
        this.style = android.graphics.Paint.Style.FILL
        this.typeface = android.graphics.Typeface.DEFAULT_BOLD
        this.maskFilter = android.graphics.BlurMaskFilter(shadowBlur, android.graphics.BlurMaskFilter.Blur.NORMAL)
    }

    val outlinePaint = android.graphics.Paint().apply {
        this.textSize = textSizePx
        this.style = android.graphics.Paint.Style.STROKE
        this.strokeWidth = outlineWidthPx
        this.color = outlineColor.toArgb()
        this.strokeCap = android.graphics.Paint.Cap.ROUND
        this.strokeJoin = android.graphics.Paint.Join.ROUND
        this.typeface = android.graphics.Typeface.DEFAULT_BOLD
        this.isAntiAlias = true
    }

    val fillPaint = android.graphics.Paint().apply {
        this.color = textColor.toArgb()
        this.textSize = textSizePx
        this.style = android.graphics.Paint.Style.FILL
        this.typeface = android.graphics.Typeface.DEFAULT_BOLD
        this.isAntiAlias = true
    }

    // Calculate text positioning
    val textBounds = android.graphics.Rect()
    fillPaint.getTextBounds(text, 0, text.length, textBounds)
    val textWidth = fillPaint.measureText(text)
    val textHeight = textBounds.height()

    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val textX = centerX - textWidth / 2f
    val textY = centerY + textHeight / 2f

    drawContext.canvas.nativeCanvas.apply {
        // Draw shadow first
        drawText(
            text,
            textX + shadowOffset.x,
            textY + shadowOffset.y,
            shadowPaint
        )

        // Draw outline
        drawText(text, textX, textY, outlinePaint)

        // Draw fill
        drawText(text, textX, textY, fillPaint)
    }
}

/**
 * Preset configurations for common use cases
 */
object OutlinedTextPresets {
    @Composable
    fun GameTitle(
        text: String,
        modifier: Modifier = Modifier
    ) = OutlinedText(
        text = text,
        modifier = modifier,
        fontSize = 64.sp,
        fontWeight = FontWeight.ExtraBold,
        textColor = Color.White,
        outlineColor = Color(0xFF4CAF50),
        outlineWidth = 6.dp,
        shadowOffset = Offset(6f, 6f),
        shadowColor = Color.Black.copy(alpha = 0.5f),
        enablePulse = true,
        pulseDuration = 2000,
        pulseScale = 0.05f
    )

    @Composable
    fun FloatingLabel(
        text: String,
        modifier: Modifier = Modifier
    ) = OutlinedText(
        text = text,
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        textColor = MaterialTheme.colorScheme.onSurface,
        outlineColor = MaterialTheme.colorScheme.primary,
        outlineWidth = 2.dp,
        enableBobbing = true,
        bobbingDuration = 3000,
        bobbingRange = 4.dp
    )

    @Composable
    fun SpinningText(
        text: String,
        modifier: Modifier = Modifier
    ) = OutlinedText(
        text = text,
        modifier = modifier,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        textColor = Color.Yellow,
        outlineColor = Color.Red,
        outlineWidth = 3.dp,
        enableRotation = true,
        rotationDuration = 3000,
        enablePulse = true,
        pulseDuration = 1000,
        pulseScale = 0.15f
    )
}

// Preview composables
@Preview(showBackground = true, backgroundColor = 0xFF1a1a1a)
@Composable
private fun OutlinedTextPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        OutlinedTextPresets.GameTitle("CODE QUEST")

        OutlinedTextPresets.FloatingLabel("Level Complete!")

        OutlinedTextPresets.SpinningText("BONUS!")

        OutlinedText(
            text = "Custom Style",
            fontSize = 28.sp,
            textColor = Color.Cyan,
            outlineColor = Color.Magenta,
            outlineWidth = 3.dp,
            shadowOffset = Offset(3f, 3f),
            shadowColor = Color.Blue.copy(alpha = 0.4f),
            enableBobbing = true,
            enablePulse = true
        )
    }
}