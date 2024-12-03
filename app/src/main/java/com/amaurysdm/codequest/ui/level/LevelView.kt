package com.amaurysdm.codequest.ui.level

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.media.MediaPlayer
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.customcomposables.CreateText
import com.amaurysdm.codequest.customcomposables.UpDownKeys
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
    val coroutineScope = rememberCoroutineScope()
    val dropItemPlayer = MediaPlayer.create(navController.context, R.raw.drop)
    val pickItemPlayer = MediaPlayer.create(navController.context, R.raw.pickup)

    // Theres space above and below the status bar that I just cant use
    // This helps me get the height of the status bar height and navigation bar height
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    // If I just used the modifier it would leave a space above and below the status bar

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            // I used a brush to create a gradient
            // Since its in the background it looks like the top and bottom are different colors
            Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.primary
                )
            )
        )
        .padding(top = statusBarHeight, bottom = navigationBarHeight), bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9eb50d))
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(10.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp,
                            topStart = 10.dp,
                            topEnd = 10.dp
                        )
                    )
                    .horizontalScroll(
                        state = rememberScrollState(),
                        enabled = true,
                        flingBehavior = null,
                    )
            ) {
                levelViewModel.bottomBarItems.forEach { direction ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(MaterialTheme.colorScheme.secondary)
                            .requiredSize(100.dp)
                    ) {
                        Icon(imageVector = ImageVector
                            .vectorResource(id = direction.icon),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { _ ->
                                            // Should really implement
                                            // state  hoisting but I made this in a rush
                                            levelViewModel.draggingItem = TopBarItem(
                                                mutableStateOf(direction),
                                                mutableStateOf(true)
                                            )
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
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(10.dp)
                    .clickable(
                        enabled = !levelViewModel.isAnimating
                    ) {
                        // Should move this to the viewmodel
                        levelViewModel.isAnimating =
                            true // This also controls weather the 'GO' button works
                        coroutineScope
                            .launch(Dispatchers.IO) {
                                levelViewModel.playButton(navController)
                            }
                            .invokeOnCompletion {
                                levelViewModel.isAnimating = false
                            }
                    }

            ) {
                // Creates the 'GO' button using my custom composable
                CreateText(
                    "GO",
                    textColor = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }, topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9eb50d))
                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(MaterialTheme.colorScheme.secondary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.1f)
                    .size(100.dp)

                    .clip(RoundedCornerShape( topStart = 0.dp, topEnd = 100.dp, bottomStart = 5.dp, bottomEnd = 100.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
                    .clickable {
                        levelViewModel.back(navController)
                    }
            ) {
                CreateText(
                    "<",
                    textColor = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .horizontalScroll(
                        state = rememberScrollState(),
                        enabled = true,
                        flingBehavior = null,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                levelViewModel.topBarItems.forEachIndexed { index, _ ->

                    Box(
                        modifier = Modifier
                            .size(90.dp)
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
                                            // Should really implement
                                            // state hoisting but I made this in a rush
                                            // this just places the item that you are dragging in the correct position
                                            levelViewModel.topBarItems[index] =
                                                levelViewModel.draggingItem
                                            levelViewModel.draggingItem = TopBarItem()

                                            // This turns the dropped item into the clickedItem
                                            if (levelViewModel.clickedItem == levelViewModel.topBarItems[index]) {
                                                levelViewModel.clickedItem = TopBarItem()
                                            } else levelViewModel.clickedItem =
                                                levelViewModel.topBarItems[index]

                                            return true
                                        }
                                    }
                                }
                            )

                    )
                    {

                        this@Row.AnimatedVisibility(
                            visible = levelViewModel.topBarItems[index].isVisible.value,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = levelViewModel.topBarItems[index].direction.value.icon
                                ), contentDescription = null, modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .dragAndDropSource {
                                        detectTapGestures(
                                            onLongPress = { _ ->
                                                // When you long press on an item I save it to draggingItem
                                                levelViewModel.draggingItem =
                                                    levelViewModel.topBarItems[index]
                                                levelViewModel.topBarItems[index] = TopBarItem()

                                                // Creates a transfer data object
                                                startTransfer(
                                                    transferData = DragAndDropTransferData(
                                                        clipData = ClipData.newPlainText(
                                                            "text",
                                                            ""
                                                        )
                                                    )
                                                )

                                            },
                                            onTap = {
                                                // When you tap on an item I save it to clickedItem
                                                // This is used to check if you clicked on the repeater
                                                if (levelViewModel.clickedItem == levelViewModel.topBarItems[index]) {
                                                    levelViewModel.clickedItem = TopBarItem()
                                                } else levelViewModel.clickedItem =
                                                    levelViewModel.topBarItems[index]
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(vertical = paddingValues.calculateTopPadding())
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        levelViewModel.screenPosition = Offset(
                            levelViewModel.screenPosition.x + dragAmount.x,
                            levelViewModel.screenPosition.y + dragAmount.y
                        )
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
                    .offset(
                        x = levelViewModel.screenPosition.x.dp / 5,
                        y = levelViewModel.screenPosition.y.dp / 5
                    )

            ) {

                // Create Path
                val tileSize = remember(maxWidth) { maxWidth / 10 }

                levelViewModel.gameState.path.forEach { direction ->

                    Box(
                        modifier = Modifier
                            .offset(x = tileSize * direction.first, y = tileSize * direction.second)
                            .size(tileSize)
                    ) {
                        Image(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.img_1),
                            contentDescription = "background",
                            modifier = Modifier.fillMaxSize(),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }

                // Create End Square
                Box(
                    modifier = Modifier
                        .offset(
                            tileSize * levelViewModel.gameState.path.last().first,
                            tileSize * levelViewModel.gameState.path.last().second
                        )
                        .size(tileSize)
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_1),
                        contentDescription = "background",
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.Center,
                        colorFilter = ColorFilter.tint(Color.Yellow, BlendMode.Softlight)
                    )
                }

                // Create Player
                Box(
                    modifier = Modifier
                        .offset(
                            x = tileSize * levelViewModel.animatableX.value,
                            y = tileSize * levelViewModel.animatableY.value
                        )
                        .size(tileSize)
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.rocks),
                        contentDescription = "background",
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.Center,
                    )
                }
            }

            // This is used by the repeater to place items
            AnimatedVisibility(
                visible = levelViewModel.clickedItem.direction.value == Directions.Repeat,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {

                if (levelViewModel.clickedItem.children.isEmpty()) {
                    levelViewModel.clickedItem.children =
                        remember { mutableStateListOf(TopBarItem(), TopBarItem()) }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(bottomStart = 10.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .align(Alignment.TopEnd),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        levelViewModel.clickedItem.children.forEachIndexed { index, _ ->

                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
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
                                                    levelViewModel.clickedItem.children[index] =
                                                        levelViewModel.draggingItem
                                                    levelViewModel.draggingItem = TopBarItem()

                                                    return true
                                                }
                                            }
                                        }
                                    ),
                            )
                            {
                                this@Row.AnimatedVisibility(
                                    visible = levelViewModel.clickedItem.children[index].isVisible.value,
                                    enter = scaleIn() + fadeIn(),
                                    exit = scaleOut() + fadeOut()
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            id = levelViewModel.clickedItem.children[index].direction.value.icon
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .dragAndDropSource {
                                                detectTapGestures(
                                                    onLongPress = { _ ->

                                                        levelViewModel.draggingItem =
                                                            levelViewModel.clickedItem.children[index]
                                                        levelViewModel.clickedItem.children[index] =
                                                            TopBarItem()

                                                        startTransfer(
                                                            transferData = DragAndDropTransferData(
                                                                clipData = ClipData.newPlainText(
                                                                    "text",
                                                                    ""
                                                                )
                                                            )
                                                        )
                                                    },
                                                    onTap = {
                                                        if (levelViewModel.clickedItem == levelViewModel.topBarItems[index]) {
                                                            levelViewModel.clickedItem =
                                                                TopBarItem()
                                                        } else levelViewModel.clickedItem =
                                                            levelViewModel.topBarItems[index]
                                                    }
                                                )
                                            },

                                        )
                                }
                            }
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
}