package com.ghn.poker.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// =============================================================================
// GIZMOPOKER SHAPE SYSTEM
// =============================================================================
// Refined corner radii for premium feel - larger radii create softer,
// more luxurious appearance
// =============================================================================

val Shapes = Shapes(
    // Compact elements: chips, badges, small buttons
    extraSmall = RoundedCornerShape(6.dp),

    // Standard interactive elements: buttons, input fields
    small = RoundedCornerShape(12.dp),

    // Cards, dialogs, elevated containers
    medium = RoundedCornerShape(16.dp),

    // Large cards, bottom sheets
    large = RoundedCornerShape(24.dp),

    // Full-screen overlays, modal sheets
    extraLarge = RoundedCornerShape(32.dp),
)

// -----------------------------------------------------------------------------
// CUSTOM SHAPE TOKENS
// -----------------------------------------------------------------------------

object GizmoShapes {
    /** Pill shape for buttons, chips */
    val pill = RoundedCornerShape(percent = 50)

    /** Card with top corners only */
    val cardTop = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    /** Card with bottom corners only */
    val cardBottom = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 20.dp,
        bottomEnd = 20.dp
    )

    /** Session item card */
    val sessionCard = RoundedCornerShape(16.dp)

    /** Featured news card */
    val featuredCard = RoundedCornerShape(20.dp)

    /** Input fields */
    val inputField = RoundedCornerShape(14.dp)

    /** FAB */
    val fab = RoundedCornerShape(18.dp)

    /** Bottom nav indicator */
    val navIndicator = RoundedCornerShape(
        topStart = 3.dp,
        topEnd = 3.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
}
