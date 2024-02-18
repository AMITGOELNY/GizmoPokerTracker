package com.ghn.poker.tracker.presentation.cards.model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.domain.model.CardSuit
import gizmopoker.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

val CardSuit.drawable: DrawableResource
    get() = when (this) {
        CardSuit.HEARTS -> Res.drawable.ic_hearts
        CardSuit.DIAMONDS -> Res.drawable.ic_diamond
        CardSuit.SPADES -> Res.drawable.ic_spades
        CardSuit.CLUBS -> Res.drawable.ic_clubs
    }

val CardSuit.color: Color
    @Composable
    get() = if (this == CardSuit.HEARTS || this == CardSuit.DIAMONDS) {
        Color(0xFFc42727)
    } else {
        Color(0xFF010101)
    }

enum class CardSize(
    val width: Dp,
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val iconSize: Dp,
    val shape: Shape,
    val paddingValues: Dp,
) {
    EXTRA_SMALL(50.dp, 14.sp, 16.sp, 10.dp, RoundedCornerShape(4.dp), 4.dp),
    SMALL(75.dp, 18.sp, 20.sp, 16.dp, RoundedCornerShape(8.dp), 8.dp),
    Medium(200.dp, 35.sp, 37.5.sp, 24.dp, RoundedCornerShape(8.dp), 8.dp),
    Large(300.dp, 45.sp, 48.sp, 32.dp, RoundedCornerShape(8.dp), 8.dp);

    val rotationOffset: Float
        get() = when (this) {
            EXTRA_SMALL -> 55f
            SMALL -> 85f
            Medium -> 105f
            Large -> 125f
        }
}
