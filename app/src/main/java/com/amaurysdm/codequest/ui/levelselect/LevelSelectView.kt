package com.amaurysdm.codequest.ui.levelselect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.CreateText
import com.amaurysdm.codequest.R

@Preview(showBackground = true)
@Composable
fun LevelSelectView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    levelSelectViewModel: LevelSelectViewmodel = viewModel()
) {
    Box {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateText(
                    text = "Level Select",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineLarge,
                    textSize = 80f,
                    outlineWidth = 20f,
                    modifier = modifier.padding(bottom = 10.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3)
                ) {
                    items(
                        levelSelectViewModel.levels
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(10.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .border(15.dp, MaterialTheme.colorScheme.primary)
                                .background(MaterialTheme.colorScheme.background)
                                .clickable {
                                    levelSelectViewModel.navigateToLevel(navController, it)
                                }
                        ) {
                            CreateText(
                                text = it.toString(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.headlineLarge,
                                textSize = 80f,
                                outlineWidth = 20f,
                                modifier = modifier.align(Alignment.Center)
                            )
                        }

                    }
                }
            }

            Button(
                onClick = { levelSelectViewModel.leaveLevelSelect(navController) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Leave")
            }
        }
    }

}


