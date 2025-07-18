package com.github.nick45chen.draggablebutton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Composable that renders the draggable button with touch handling.
 * Follows Separation of Concerns by handling only UI rendering.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DraggableButtonCompose(
    configuration: DraggableButtonConfiguration,
    dragController: DragController,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    
    val widthDp = with(density) { configuration.width.toDp() }
    val heightDp = with(density) { configuration.height.toDp() }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Show close target when dragging
        if (dragController.isDragging) {
            val closeTargetPosition = dragController.getCloseTargetPosition()
            closeTargetPosition?.let { position ->
                CloseTarget(
                    isOverlapping = dragController.isOverlappingCloseTarget,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = position.x.roundToInt(),
                                y = position.y.roundToInt()
                            )
                        }
                )
            }
        }
        
        // Draggable button
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = dragController.currentPosition.x.roundToInt(),
                        y = dragController.currentPosition.y.roundToInt()
                    )
                }
                .size(widthDp, heightDp)
                .pointerInteropFilter { event ->
                    dragController.handleTouchEvent(event)
                }
        ) {
            if (configuration.composeContent != null) {
                // Custom content provided - wrap it in a FloatingActionButton
                FloatingActionButton(
                    onClick = { /* Handled by touch handler */ },
                    modifier = Modifier.size(widthDp, heightDp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    configuration.composeContent.invoke()
                }
            } else {
                // No custom content - use default appearance
                DefaultDraggableButton(
                    modifier = Modifier.size(widthDp, heightDp)
                )
            }
        }
    }
}

/**
 * Default button appearance when no custom content is provided.
 */
@Composable
private fun DefaultDraggableButton(
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { /* Handled by touch handler */ },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        // Empty content - just a colored circle
    }
}