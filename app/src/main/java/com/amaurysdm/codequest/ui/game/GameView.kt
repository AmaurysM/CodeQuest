package com.amaurysdm.codequest.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.model.GameState

@Composable
fun GameView(
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewmodel = viewModel()
){
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
