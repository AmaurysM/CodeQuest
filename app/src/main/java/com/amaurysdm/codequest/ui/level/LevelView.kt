package com.amaurysdm.codequest.ui.level

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.draganddrop.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.customcomposables.OutlinedText
import com.amaurysdm.codequest.customcomposables.ParallaxBackground
import com.amaurysdm.codequest.customcomposables.UpDownKeys
import com.amaurysdm.codequest.model.Directions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LevelView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    levelViewModel: LevelViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val density = LocalDensity.current

    // Animation states for enhanced UI
    val topBarAnimation = remember { Animatable(0f) }
    val bottomBarAnimation = remember { Animatable(0f) }
    val gameAreaAnimation = remember { Animatable(0f) }

    // Launch enter animations
    LaunchedEffect(Unit) {
        launch { topBarAnimation.animateTo(1f, tween(600, easing = FastOutSlowInEasing)) }
        launch { bottomBarAnimation.animateTo(1f, tween(800, 200, FastOutSlowInEasing)) }
        launch { gameAreaAnimation.animateTo(1f, tween(1000, 400, FastOutSlowInEasing)) }
    }

    // Enhanced gradient background
    val backgroundBrush = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
        ),
        radius = 1200f
    )

    Box {


        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            topBar = {
                EnhancedTopBar(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = -50.dp.toPx() * (1f - topBarAnimation.value)
                            alpha = topBarAnimation.value
                        },
                    levelViewModel = levelViewModel,
                    navController = navController
                )
            },
            bottomBar = {
                EnhancedBottomBar(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = 100.dp.toPx() * (1f - bottomBarAnimation.value)
                            alpha = bottomBarAnimation.value
                        },
                    levelViewModel = levelViewModel,
                    navController = navController,
                    context = context,
                    coroutineScope = coroutineScope
                )
            }
        ) { paddingValues ->

            ParallaxBackground()

            GameArea(
                modifier = Modifier
                    .padding(paddingValues)
                    .graphicsLayer {
                        scaleX = 0.8f + (0.2f * gameAreaAnimation.value)
                        scaleY = 0.8f + (0.2f * gameAreaAnimation.value)
                        alpha = gameAreaAnimation.value
                    },
                levelViewModel = levelViewModel
            )

            // Enhanced repeater overlay
            RepeaterOverlay(
                levelViewModel = levelViewModel,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun EnhancedTopBar(
    modifier: Modifier = Modifier,
    levelViewModel: LevelViewmodel,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Back button with modern styling
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .clickable { levelViewModel.back(navController) },
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Command sequence area
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                //contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                levelViewModel.topBarItems.toList().forEachIndexed() { index, item ->
                    CommandSlot(
                        item = item,
                        index = index,
                        levelViewModel = levelViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommandSlot(
    item: TopBarItem,
    index: Int,
    levelViewModel: LevelViewmodel
) {
    val isSelected = levelViewModel.clickedItem == item
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        modifier = Modifier
            .size(72.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
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
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                item.isVisible.value -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = item.isVisible.value,
                enter = scaleIn(spring()) + fadeIn(),
                exit = scaleOut(spring()) + fadeOut()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(item.direction.value.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .dragAndDropSource(block = {
                            detectTapGestures(
                                onLongPress = {
                                    // Save to draggingItem
                                    levelViewModel.draggingItem = levelViewModel.topBarItems[index]
                                    levelViewModel.topBarItems[index] = TopBarItem()

                                    // Create transfer data object
                                    startTransfer(
                                        transferData = DragAndDropTransferData(
                                            clipData = ClipData.newPlainText("text", "")
                                        )
                                    )
                                },
                                onTap = {
                                    // Toggle clickedItem
                                    levelViewModel.clickedItem =
                                        if (levelViewModel.clickedItem == levelViewModel.topBarItems[index]) {
                                            TopBarItem()
                                        } else {
                                            levelViewModel.topBarItems[index]
                                        }
                                }
                            )
                        }),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }


            // Empty slot indicator
            if (!item.isVisible.value) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                        contentDescription = "Empty slot",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedBottomBar(
    modifier: Modifier = Modifier,
    levelViewModel: LevelViewmodel,
    navController: NavHostController,
    context: android.content.Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Command palette
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(levelViewModel.bottomBarItems.size) { index ->
                    val direction = levelViewModel.bottomBarItems[index]
                    DirectionTile(direction = direction, levelViewModel = levelViewModel)
                }
            }

            // Enhanced GO button
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable(enabled = !levelViewModel.isAnimating) {
                        levelViewModel.isAnimating = true
                        coroutineScope.launch(Dispatchers.IO) {
                            levelViewModel.playButton(navController, context)
                        }.invokeOnCompletion {
                            levelViewModel.isAnimating = false
                        }
                    },
                color = if (levelViewModel.isAnimating)
                    MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.primary,
                shadowElevation = if (levelViewModel.isAnimating) 2.dp else 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (levelViewModel.isAnimating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "GO",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DirectionTile(
    direction: Directions,
    levelViewModel: LevelViewmodel
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        modifier = Modifier
            .size(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .dragAndDropSource(block = {
                detectTapGestures(
                    onLongPress = {
                        levelViewModel.draggingItem = TopBarItem(
                            mutableStateOf(direction),
                            mutableStateOf(true)
                        )
                        startTransfer(
                            DragAndDropTransferData(
                                clipData = ClipData.newPlainText("text", "")
                            )
                        )
                    }
                )
            }),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(direction.icon),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun GameArea(
    modifier: Modifier = Modifier,
    levelViewModel: LevelViewmodel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    levelViewModel.screenPosition = Offset(
                        levelViewModel.screenPosition.x + dragAmount.x,
                        levelViewModel.screenPosition.y + dragAmount.y
                    )
                }
            }
    ) {


        // Game grid
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(
                    x = levelViewModel.screenPosition.x.dp / 5,
                    y = levelViewModel.screenPosition.y.dp / 5
                )
        ) {
            val tileSize = remember(maxWidth) { maxWidth / 10 }

            // Path tiles with enhanced styling
            levelViewModel.gameState.path.forEachIndexed { index, position ->
                val isStart = index == 0
                val isEnd = index == levelViewModel.gameState.path.lastIndex

                Card(
                    modifier = Modifier
                        .offset(
                            x = tileSize * position.first,
                            y = tileSize * position.second
                        )
                        .size(tileSize),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isStart -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                            isEnd -> Color.Yellow.copy(alpha = 0.8f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center

                    ) {
                        if (isStart) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_play_arrow_24),
                                contentDescription = "Start",
                                modifier = Modifier.size(tileSize * 0.6f),
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        } else if (isEnd) {
                            Image(
                                painter = painterResource(id = R.drawable.img_1),
                                contentDescription = "End",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = ColorFilter.tint(Color.Yellow, BlendMode.Softlight)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_1),
                                contentDescription = "Tile",
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            }

            // Enhanced player with shadow and animation
            val playerScale by animateFloatAsState(
                targetValue = if (levelViewModel.isAnimating) 1.2f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )

            Box(
                modifier = Modifier
                    .offset(
                        x = tileSize * levelViewModel.animatableX.value,
                        y = tileSize * levelViewModel.animatableY.value
                    )
                    .size(tileSize)
                    .graphicsLayer {
                        scaleX = playerScale
                        scaleY = playerScale
                    }
                    .zIndex(10f),

            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(R.drawable.rocks),
                        contentDescription = "Player",
                        modifier = Modifier.size(tileSize * 0.8f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RepeaterOverlay(
    levelViewModel: LevelViewmodel,
    modifier: Modifier = Modifier
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
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Repeat Configuration",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Initialize children if empty
                if (levelViewModel.clickedItem.children.isEmpty()) {
                    levelViewModel.clickedItem.children = remember {
                        mutableStateListOf(TopBarItem(), TopBarItem())
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    levelViewModel.clickedItem.children.forEachIndexed() { index, child ->
                        RepeaterSlot(
                            item = child,
                            index = index,
                            levelViewModel = levelViewModel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                UpDownKeys(
                    modifier = Modifier,
                    initialNumber = levelViewModel.clickedItem.repeater
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RepeaterSlot(
    item: TopBarItem,
    index: Int,
    levelViewModel: LevelViewmodel
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = remember {
                    object : DragAndDropTarget {
                        override fun onDrop(event: DragAndDropEvent): Boolean {
                            levelViewModel.clickedItem.children[index] = levelViewModel.draggingItem
                            levelViewModel.draggingItem = TopBarItem()
                            return true
                        }
                    }
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isVisible.value)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = item.isVisible.value,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(item.direction.value.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .dragAndDropSource(block = {
                            detectTapGestures(
                                onLongPress = {
                                    levelViewModel.draggingItem =
                                        levelViewModel.clickedItem.children[index]
                                    levelViewModel.clickedItem.children[index] = TopBarItem()
                                    startTransfer(
                                        DragAndDropTransferData(
                                            clipData = ClipData.newPlainText("text", "")
                                        )
                                    )
                                }
                            )
                        }),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}