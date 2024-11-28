package com.amaurysdm.codequest.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.customcomposables.CreateText
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.customcomposables.TitleAnimation

@Preview(showBackground = true)
@Composable
fun WelcomeView(
    navController: NavHostController = rememberNavController(),
    welcomeViewModel: WelcomeViewmodel = viewModel()
) {
    val titleAnimation = TitleAnimation()

    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                CreateText(
                    text = "CodeQuest",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineLarge,
                    textSize = 180f,
                    outlineWidth = 110f,
                    bobbing = titleAnimation.first,
                    rotation = titleAnimation.second
                )
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