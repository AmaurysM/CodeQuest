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
import androidx.compose.runtime.collectAsState
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
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.customcomposables.CreateText

@Preview(showBackground = true)
@Composable
fun LevelSelectView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    levelSelectViewModel: LevelSelectViewmodel = viewModel()
) {

    val levelFlow = levelSelectViewModel.levels.collectAsState(emptyList())
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
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineLarge,
                    textSize = 80f,
                    outlineWidth = 20f,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3)
                ) {
                    items(
                        levelFlow.value.size
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(10.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .border(15.dp, MaterialTheme.colorScheme.primary)
                                .background(if (levelSelectViewModel.getLevel(it).completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                                .clickable {
                                    levelSelectViewModel.navigateToLevel(navController, it)
                                }
                        ) {
                            CreateText(
                                text = (it + 1).toString(),
                                textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.headlineLarge,
                                textSize = 80f,
                                outlineWidth = 20f,
                                modifier = Modifier.align(Alignment.Center)
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


