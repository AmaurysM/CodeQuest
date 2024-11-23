package com.amaurysdm.codequest.ui.welcome

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R

@Preview(showBackground = true)
@Composable
fun WelcomeView(
    navController: NavHostController = rememberNavController(),
    welcomeViewModel: WelcomeViewmodel = viewModel()
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillHeight
        )

        val infiniteTransition = rememberInfiniteTransition()
        val bobbing = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            label = "bobbing logo",
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 3000
                    50f at 0
                    -50f at 1500 with FastOutLinearInEasing
                    50f at 3000
                }
            )
        )

        val rotation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 15f,
            label = "rotation logo",
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 3000
                    -15f at 0 with FastOutLinearInEasing
                    15f at 1500
                    -15f at 3000 with FastOutSlowInEasing
                }
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                val TEXT = "CodeQuest"
                val TEXT_SIZE = 180f
                val OUTLINE_WIDTH = 110f
                val X_POSITION = 50f
                val Y_POSITION = 0f

                val outlinePaint = android.graphics.Paint().apply {
                    textSize = TEXT_SIZE
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = OUTLINE_WIDTH
                    color = MaterialTheme.colorScheme.primary.toArgb()
                }

                val fillPaint = android.graphics.Paint().apply {
                    color = MaterialTheme.colorScheme.onPrimary.toArgb()
                    textSize = TEXT_SIZE
                    style = android.graphics.Paint.Style.FILL

                }

                Canvas(modifier = Modifier
                    .offset( y = bobbing.value.dp)
                    .rotate(rotation.value)
                ) {
                    drawContext.canvas.nativeCanvas.drawText(
                        TEXT,
                        X_POSITION,
                        Y_POSITION,
                        outlinePaint
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        TEXT,
                        X_POSITION,
                        Y_POSITION,
                        fillPaint
                    )
                }
            }


            Button(
                onClick = { welcomeViewModel.navigateLogin(navController) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Button(
                onClick = { welcomeViewModel.navigateRegister(navController) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register")
            }
        }
    }
}