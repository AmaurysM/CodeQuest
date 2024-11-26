package com.amaurysdm.codequest.ui.level

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.CreateText
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.model.Directions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LevelView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    levelViewModel: LevelViewmodel = viewModel()
) {
    var tileSize by remember { mutableStateOf(0.dp) }
    val coroutineScope = rememberCoroutineScope()
    var dragBoxItem by remember { /*CharArray(1) */ mutableStateOf<Directions>(Directions.Down)}
    var boxes = remember {
        ArrayList<Directions>(levelViewModel.countTurnsInLevel())
            .apply {
                repeat(levelViewModel.countTurnsInLevel()) { add(Directions.Nothing) }
            }
    }

    //var pannableScreenStartPosition by remember { mutableStateOf(Pair(0f, 0f)) }
    var pannableScreenOffsetX by remember { mutableStateOf(0f) }
    var pannableScreenOffsetY by remember { mutableStateOf(0f) }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(10.dp)
                    .horizontalScroll(
                        state = rememberScrollState(),
                        enabled = true,
                        flingBehavior = null,
                    )
            ) {
                levelViewModel.uniqueDirection.forEach { it ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(MaterialTheme.colorScheme.secondary)
                            .requiredSize(100.dp)
                    ) {
                        Icon(imageVector = ImageVector
                            .vectorResource(id = /*levelViewModel.getImage(it)*/ it.icon),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            dragBoxItem = it
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(10.dp)

                    .clickable(
                        enabled = !levelViewModel.isAnimating
                    ) {
                        coroutineScope.launch(Dispatchers.IO) {
                            levelViewModel.movePlayer(boxes, navController)
                        }
                        pannableScreenOffsetX = 0f
                        pannableScreenOffsetY = 0f
                        levelViewModel.isAnimating = true
                    }
            ) {
                CreateText("GO", modifier = Modifier.align(Alignment.Center))
            }
        }
    }, topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .background(MaterialTheme.colorScheme.secondary)
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .horizontalScroll(
                        state = rememberScrollState(),
                        enabled = true,
                        flingBehavior = null,
                    )
            ) {
                repeat(boxes.size) { index ->
                    var droped by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .padding(10.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(BorderStroke(1.dp, Color.Black))
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event
                                        .mimeTypes()
                                        .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            Log.i("DragAndDrop", "onDrop")
                                            droped = true
                                            boxes[index] = dragBoxItem
                                            return true
                                        }
                                    }
                                }
                            ),
                    )
                    {
                        this@Row.AnimatedVisibility(
                            visible = droped,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = boxes[index].icon/*levelViewModel.getImage(
                                        boxes[index]
                                    )*/
                                ), contentDescription = null, modifier = Modifier
                                    .fillMaxSize()
                                    .dragAndDropSource {
                                        detectTapGestures(
                                            onLongPress = { offset ->
                                                droped = false
                                                dragBoxItem = boxes[index]
                                                startTransfer(
                                                    transferData = DragAndDropTransferData(
                                                        clipData = ClipData.newPlainText(
                                                            "text",
                                                            ""
                                                        )
                                                    )
                                                )
                                            }
                                        )

                                    }
                            )
                        }
                    }
                }
            }
        }
    }
    ) {
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        pannableScreenOffsetX += dragAmount.x
                        pannableScreenOffsetY += dragAmount.y
                    }
                }
        )
        {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight
            )


            BoxWithConstraints(
                modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(10.dp)
                    .offset(
                        x = pannableScreenOffsetX.dp / 3.5f,
                        y = pannableScreenOffsetY.dp / 3.5f
                    )


            ) {
                tileSize = maxWidth / 10
                levelViewModel.currentState.path.forEach {
                    Box(
                        modifier = Modifier
                            .offset(tileSize * it.movement.first, tileSize * it.movement.second)
                            .size(tileSize)
                            .background(Color.Red)
                    )
                }


                Box(
                    modifier = Modifier
                        .offset(
                            x = tileSize * levelViewModel.positionX,
                            y = tileSize * levelViewModel.positionY
                        )
                        .size(tileSize)
                        .background(Color.Blue)
                )
                Box(
                    modifier = Modifier
                        .offset(
                            tileSize * levelViewModel.currentState.path.last().movement.first,
                            tileSize * levelViewModel.currentState.path.last().movement.second
                        )
                        .size(tileSize)
                        .background(Color.Yellow)
                )
            }

        }
    }
}