package com.amaurysdm.codequest.customcomposables

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

// Creates a text with a with a border
@Preview(showBackground = true)
@Composable
fun CreateText(
    text: String = "Amaurys",
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    style: TextStyle = MaterialTheme.typography.headlineLarge,
    textSize: Float = 100f,
    outlineWidth: Float = 20f,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    bobbing: Float = 0f,
    rotation: Float = 0f
) {


    val outlinePaint = android.graphics.Paint().apply {
        this.textSize = textSize
        this.style = android.graphics.Paint.Style.STROKE
        this.strokeWidth = outlineWidth
        this.color = outlineColor.toArgb()
    }

    val fillPaint = android.graphics.Paint().apply {
        this.color = textColor.toArgb()
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

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                text,
                (size.width - textWidth) / 2f,
                (size.height + textHeight) / 2f,
                outlinePaint
            )
        }
        drawContext.canvas.nativeCanvas
            .drawText(
                text,
                (size.width - textWidth) / 2f,
                (size.height + textHeight) / 2f,
                fillPaint
            )
    }

}

