package com.amaurysdm.codequest.customcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amaurysdm.codequest.model.Directions
import com.amaurysdm.codequest.ui.level.LevelViewmodel
import com.amaurysdm.codequest.ui.level.TopBarItem

@Preview(showSystemUi = false, name = "Repeater Overlay", showBackground = true)
@Composable
fun RepeaterOverlay(
    modifier: Modifier = Modifier,
    levelViewModel: LevelViewmodel = viewModel()

) {
    AnimatedVisibility(
        visible = levelViewModel.clickedItem.direction.value == Directions.Repeat,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (levelViewModel.clickedItem.children.isEmpty()) {
                    levelViewModel.clickedItem.children = remember {
                        mutableStateListOf(TopBarItem(), TopBarItem())
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    levelViewModel.clickedItem.children.forEachIndexed { index, child ->
                        RepeaterSlot(
                            item = child,
                            index = index,
                            levelViewModel = levelViewModel
                        )
                    }
                    UpDownKeys(
                        modifier = Modifier,
                        initialNumber = levelViewModel.clickedItem.repeater
                    )
                }

            }
        }
    }
}