package com.amaurysdm.codequest.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.customcomposables.ParallaxBackground

@Composable
fun HomeView(
    navController: NavHostController = rememberNavController(),
    homeViewmodel: HomeViewmodel = viewModel()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        ParallaxBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.code_quest_logo_nback),
                contentDescription = "Code Quest Logo",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { homeViewmodel.settings(navController) },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_lightbulb_outline_24),
                        contentDescription = "Settings",
                        modifier = Modifier.size(36.dp)
                    )
                }

                Button(
                    onClick = { homeViewmodel.startGame(navController) },
                    modifier = Modifier
                        .weight(2f)
                        .aspectRatio(2f),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "Start Game",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

            }
        }
    }
}
