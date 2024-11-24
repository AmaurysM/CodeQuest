package com.amaurysdm.codequest.ui.level

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun LevelView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    levelViewModel: LevelViewmodel = viewModel()
) {
    var tileSize by remember{ mutableStateOf(0.dp) }
    var playerPosition by remember { mutableStateOf(levelViewModel.getLevel().playerPosition) }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        tileSize = maxWidth / 10
        levelViewModel.getLevel().path.forEach {
            Box(
                modifier = Modifier
                    .offset(tileSize * it.first, tileSize * it.second)
                    .size(tileSize)
                    .background(Color.Red)
            )
        }

        Box(
            modifier = Modifier.offset(
                tileSize * playerPosition.first
                , tileSize * playerPosition.second
            )
                .size(tileSize)
                .background(Color.Blue)

        )
    }

    Button(onClick = {
        levelViewModel.movePlayer(playerPosition,'d')
    }) {
        Text(text = "Down")
    }

}