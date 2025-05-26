package com.amaurysdm.codequest.customcomposables

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.amaurysdm.codequest.ui.level.LevelViewmodel
import com.amaurysdm.codequest.ui.level.TopBarItem


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RepeaterSlot(
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