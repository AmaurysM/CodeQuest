package com.amaurysdm.codequest.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun GameView(
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewmodel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {


    }
}

/*
@Composable
fun Level(level: GameState){
    BoxWithConstraints(
        Modifier
        .fillMaxSize()
        .padding(10.dp)
        .background(MaterialTheme.colorScheme.onBackground)
    ) {
        val tileSize = maxWidth / 10
        level.path.forEach {
            Box(
                modifier = Modifier
                    .offset(tileSize * it.first, tileSize * it.second)
                    .size(tileSize)
                    .background(Color.Red)
            )
        }
    }
}*/
