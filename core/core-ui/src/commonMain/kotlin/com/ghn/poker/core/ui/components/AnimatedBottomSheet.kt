package com.ghn.poker.core.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A wrapper around [ModalBottomSheet] that properly handles enter and exit animations.
 *
 * The standard ModalBottomSheet API has an issue where the sheet slides in nicely when shown,
 * but immediately disappears without animation when hidden (because the composable is removed
 * from composition). This wrapper fixes that by managing the sheet's visibility through
 * [SheetState.show] and [SheetState.hide] functions.
 *
 * Usage:
 * ```
 * AnimatedBottomSheet(
 *     isVisible = viewState.showSheet,
 *     onDismissRequest = viewModel::closeSheet,
 * ) {
 *     // Sheet content
 * }
 * ```
 *
 * Based on: https://proandroiddev.com/improving-the-modal-bottom-sheet-api-in-jetpack-compose-5ca56901ada8
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    // Make sure we dispose of the bottom sheet when it is no longer needed
    if (!sheetState.isVisible && !isVisible) {
        return
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        content = content,
    )
}

/**
 * A generic version of [AnimatedBottomSheet] that takes a nullable value instead of a boolean flag.
 * If the value is null, the sheet is hidden. If the value is non-null, the sheet is shown
 * and the content lambda receives the non-null value.
 *
 * This is useful when you want to dynamically show different content based on a sealed interface
 * or data class, with proper animations when transitioning between states.
 *
 * Usage:
 * ```
 * sealed interface SheetContent {
 *     data class CardPicker(val selectedCard: Card?) : SheetContent
 *     data class Confirmation(val message: String) : SheetContent
 * }
 *
 * AnimatedBottomSheet(
 *     value = viewState.sheetContent,
 *     onDismissRequest = viewModel::closeSheet,
 * ) { content ->
 *     when (content) {
 *         is SheetContent.CardPicker -> CardPickerContent(content)
 *         is SheetContent.Confirmation -> ConfirmationContent(content)
 *     }
 * }
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AnimatedBottomSheet(
    value: T?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.(T & Any) -> Unit,
) {
    LaunchedEffect(value != null) {
        if (value != null) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (!sheetState.isVisible && value == null) {
        return
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
    ) {
        // Remember the last non-null value: If our value becomes null and the sheet slides down,
        // we still need to show the last content during the exit animation.
        val notNullValue = lastNotNullValueOrNull(value) ?: return@ModalBottomSheet
        content(notNullValue)
    }
}

/**
 * Helper composable that remembers the last non-null value.
 * This is used to keep showing content during the exit animation when the value becomes null.
 */
@Composable
private fun <T> lastNotNullValueOrNull(value: T?): T? {
    val lastNotNullValueRef = remember { Ref<T>() }
    return value?.also {
        lastNotNullValueRef.value = it
    } ?: lastNotNullValueRef.value
}

/**
 * Simple reference holder class for storing mutable state.
 */
private class Ref<T> {
    var value: T? = null
}
