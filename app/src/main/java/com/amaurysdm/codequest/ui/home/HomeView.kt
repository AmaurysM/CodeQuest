package com.amaurysdm.codequest.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.CreateText
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.TitleAnimation

@Composable
fun HomeView(
    navController: NavHostController = rememberNavController(),
    homeViewmodel: HomeViewmodel = viewModel()
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Button(
                    onClick = { homeViewmodel.settings(navController) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.size(100.dp, 100.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Settings Button",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Button(
                    onClick = { homeViewmodel.startGame(navController) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp, 100.dp)
                ) {
                    Text(
                        text = "Start Game",
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }

            }

            Button(
                onClick = { homeViewmodel.logout(navController) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.size(100.dp, 100.dp)
            ) { Text(text = "Logout") }

        }
    }
}