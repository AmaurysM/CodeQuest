package com.amaurysdm.codequest

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun CreateText(
    text: String = "Amaurys"
    , color: Color = MaterialTheme.colorScheme.onPrimary
    , style: TextStyle = MaterialTheme.typography.headlineLarge
    , textSize: Float = 100f
    , outlineWidth: Float = 20f
    , modifier: Modifier = Modifier
    , bobbing: Float = 0f
    , rotation: Float = 0f
){


    val outlinePaint = android.graphics.Paint().apply {
        this.textSize = textSize
        this.style = android.graphics.Paint.Style.STROKE
        this.strokeWidth = outlineWidth
        this.color = MaterialTheme.colorScheme.primary.toArgb()
    }

    val fillPaint = android.graphics.Paint().apply {
        this.color = MaterialTheme.colorScheme.onPrimary.toArgb()
        this.textSize = textSize
        this.style = android.graphics.Paint.Style.FILL

    }


    Canvas(
        modifier = modifier
            .offset(y = bobbing.dp)
            .rotate(rotation)
    ) {
        val textBounds = android.graphics.Rect()
        fillPaint.getTextBounds(text, 0, text.length, textBounds)
        val textWidth = fillPaint.measureText(text)
        val textHeight = textBounds.height()

        drawContext.canvas.nativeCanvas.apply{
            drawText(
                text,
                (size.width - textWidth)/2f,
                (size.height + textHeight)/2f,
                outlinePaint
            )
        }
        drawContext.canvas.nativeCanvas
            .drawText(
            text,
            (size.width - textWidth)/2f,
            (size.height + textHeight)/2f,
            fillPaint
            )
    }

}

