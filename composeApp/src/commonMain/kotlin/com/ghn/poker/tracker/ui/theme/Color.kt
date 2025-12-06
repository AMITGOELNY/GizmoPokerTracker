package com.ghn.poker.tracker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =============================================================================
// GIZMOPOKER DESIGN SYSTEM - "NOIR LUXE" PALETTE
// =============================================================================
// A refined, dark casino aesthetic with champagne gold accents
// =============================================================================

// -----------------------------------------------------------------------------
// CORE PALETTE - Foundation Colors
// -----------------------------------------------------------------------------

/** Pure obsidian black - deepest background */
val Obsidian = Color(0xFF050507)

/** Warm charcoal with subtle blue undertone - primary surface */
val Charcoal = Color(0xFF0D0F14)

/** Elevated surface - cards, dialogs */
val Slate = Color(0xFF151820)

/** Higher elevation surface */
val Graphite = Color(0xFF1C1F2A)

/** Subtle borders and dividers */
val Onyx = Color(0xFF252836)

// -----------------------------------------------------------------------------
// GOLD SPECTRUM - Premium Accent System
// -----------------------------------------------------------------------------

/** Primary gold - CTAs, highlights */
val ChampagneGold = Color(0xFFD4AF37)

/** Lighter gold - hover states, gradients */
val PaleGold = Color(0xFFE8D48B)

/** Deeper gold - pressed states, shadows */
val AntiqueBrass = Color(0xFFB8860B)

/** Warm highlight - subtle glows */
val Amber = Color(0xFFFFC107)

// -----------------------------------------------------------------------------
// NEUTRAL SPECTRUM - Typography & UI Elements
// -----------------------------------------------------------------------------

/** Primary text - high emphasis */
val Platinum = Color(0xFFE8E6E3)

/** Secondary text - medium emphasis */
val Silver = Color(0xFFA8A5A0)

/** Tertiary text - low emphasis, placeholders */
val Pewter = Color(0xFF6B6966)

/** Disabled states */
val Ash = Color(0xFF45433F)

// -----------------------------------------------------------------------------
// SEMANTIC COLORS - Status & Feedback
// -----------------------------------------------------------------------------

/** Profit, success, positive */
val Emerald = Color(0xFF10B981)
val EmeraldMuted = Color(0xFF059669)

/** Loss, error, negative */
val Ruby = Color(0xFFEF4444)
val RubyMuted = Color(0xFFDC2626)

/** Warning, caution */
val Topaz = Color(0xFFF59E0B)

/** Info, neutral action */
val Sapphire = Color(0xFF3B82F6)

// -----------------------------------------------------------------------------
// POKER-SPECIFIC COLORS - Suits
// -----------------------------------------------------------------------------

object SuitColors {
    val Spades = Platinum
    val Clubs = Platinum
    val Hearts = Color(0xFFE53935)
    val Diamonds = Color(0xFFE53935)
}

// -----------------------------------------------------------------------------
// GRADIENT DEFINITIONS
// -----------------------------------------------------------------------------

object GizmoGradients {
    /** Primary background gradient - subtle depth */
    val backgroundSurface: Brush
        @Composable
        get() = Brush.verticalGradient(
            colors = listOf(
                Obsidian,
                Charcoal,
                Slate.copy(alpha = 0.5f)
            )
        )

    /** Gold shimmer for premium elements */
    val goldShimmer: Brush
        @Composable
        get() = Brush.horizontalGradient(
            colors = listOf(
                ChampagneGold,
                PaleGold,
                ChampagneGold
            )
        )

    /** Subtle gold glow for buttons */
    val goldGlow: Brush
        @Composable
        get() = Brush.horizontalGradient(
            colors = listOf(
                AntiqueBrass.copy(alpha = 0.8f),
                ChampagneGold,
                AntiqueBrass.copy(alpha = 0.8f)
            )
        )

    /** Card surface gradient - subtle elevation */
    val cardSurface: Brush
        @Composable
        get() = Brush.verticalGradient(
            colors = listOf(
                Graphite,
                Slate
            )
        )

    /** Profit indicator gradient */
    val profitGradient: Brush
        @Composable
        get() = Brush.horizontalGradient(
            colors = listOf(
                Emerald.copy(alpha = 0.15f),
                Color.Transparent
            )
        )

    /** Loss indicator gradient */
    val lossGradient: Brush
        @Composable
        get() = Brush.horizontalGradient(
            colors = listOf(
                Ruby.copy(alpha = 0.15f),
                Color.Transparent
            )
        )

    /** Radial glow for ambient effects */
    fun ambientGlow(color: Color = ChampagneGold): Brush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.3f),
            color.copy(alpha = 0.1f),
            Color.Transparent
        )
    )
}

// -----------------------------------------------------------------------------
// EXTENDED COLOR SCHEME
// -----------------------------------------------------------------------------

@Immutable
data class GizmoColors(
    // Surfaces
    val background: Color = Obsidian,
    val surface: Color = Charcoal,
    val surfaceElevated: Color = Slate,
    val surfaceHighest: Color = Graphite,
    val border: Color = Onyx,

    // Gold Accents
    val gold: Color = ChampagneGold,
    val goldLight: Color = PaleGold,
    val goldDark: Color = AntiqueBrass,

    // Text
    val textPrimary: Color = Platinum,
    val textSecondary: Color = Silver,
    val textTertiary: Color = Pewter,
    val textDisabled: Color = Ash,

    // Semantic
    val profit: Color = Emerald,
    val profitMuted: Color = EmeraldMuted,
    val loss: Color = Ruby,
    val lossMuted: Color = RubyMuted,
    val warning: Color = Topaz,
    val info: Color = Sapphire,

    // Interactive
    val primary: Color = ChampagneGold,
    val primaryContainer: Color = AntiqueBrass,
    val onPrimary: Color = Obsidian,
)

val LocalGizmoColors = staticCompositionLocalOf { GizmoColors() }

// -----------------------------------------------------------------------------
// MATERIAL 3 COLOR SCHEME - Integration
// -----------------------------------------------------------------------------

internal val colors = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = Obsidian,
    primaryContainer = AntiqueBrass,
    onPrimaryContainer = PaleGold,
    secondary = Silver,
    onSecondary = Obsidian,
    secondaryContainer = Graphite,
    onSecondaryContainer = Platinum,
    tertiary = Sapphire,
    onTertiary = Platinum,
    background = Obsidian,
    onBackground = Platinum,
    surface = Charcoal,
    onSurface = Platinum,
    surfaceVariant = Slate,
    onSurfaceVariant = Silver,
    surfaceContainerLowest = Obsidian,
    surfaceContainerLow = Charcoal,
    surfaceContainer = Slate,
    surfaceContainerHigh = Graphite,
    surfaceContainerHighest = Onyx,
    error = Ruby,
    onError = Platinum,
    errorContainer = RubyMuted,
    onErrorContainer = Platinum,
    outline = Onyx,
    outlineVariant = Ash,
    inverseSurface = Platinum,
    inverseOnSurface = Obsidian,
    inversePrimary = AntiqueBrass,
)

internal val alternateDarkColors = colors
