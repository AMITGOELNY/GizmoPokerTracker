package com.ghn.poker.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import gizmopoker.core.core_ui.generated.resources.Res
import gizmopoker.core.core_ui.generated.resources.kalnia_bold
import gizmopoker.core.core_ui.generated.resources.kalnia_light
import gizmopoker.core.core_ui.generated.resources.kalnia_medium
import gizmopoker.core.core_ui.generated.resources.kalnia_semi_bold
import gizmopoker.core.core_ui.generated.resources.kalnia_thin
import gizmopoker.core.core_ui.generated.resources.source_sans_three_bold
import gizmopoker.core.core_ui.generated.resources.source_sans_three_light
import gizmopoker.core.core_ui.generated.resources.source_sans_three_medium
import gizmopoker.core.core_ui.generated.resources.source_sans_three_regular
import gizmopoker.core.core_ui.generated.resources.source_sans_three_semi_bold
import org.jetbrains.compose.resources.Font

// Kalnia: Display/decorative font for headlines and branding
// Source Sans 3: Body/UI font for readability
// =============================================================================

@ReadOnlyComposable
@Composable
internal expect fun getScreenWidthDp(): Int

// -----------------------------------------------------------------------------
// FONT FAMILIES
// -----------------------------------------------------------------------------

/** Kalnia - Elegant display font for headlines and branding */
private val Kalnia: FontFamily
    @Stable
    @Composable
    get() = FontFamily(
        Font(Res.font.kalnia_thin, FontWeight.Thin, FontStyle.Normal),
        Font(Res.font.kalnia_light, FontWeight.Light, FontStyle.Normal),
        Font(Res.font.kalnia_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.kalnia_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.kalnia_bold, FontWeight.Bold, FontStyle.Normal),
    )

/** Source Sans 3 - Clean, readable body font */
private val SourceSans: FontFamily
    @Stable
    @Composable
    get() = FontFamily(
        Font(Res.font.source_sans_three_light, FontWeight.Light, FontStyle.Normal),
        Font(Res.font.source_sans_three_regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.source_sans_three_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.source_sans_three_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.source_sans_three_bold, FontWeight.Bold, FontStyle.Normal),
    )

// -----------------------------------------------------------------------------
// MATERIAL 3 TYPOGRAPHY
// -----------------------------------------------------------------------------

private val defaultTypography = Typography()

val Typography: Typography
    @Composable
    get() = Typography(
        // Display styles - large headlines, hero text
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = Kalnia,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1.5).sp
        ),
        displayMedium = defaultTypography.displayMedium.copy(
            fontFamily = Kalnia,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp
        ),
        displaySmall = defaultTypography.displaySmall.copy(
            fontFamily = Kalnia,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        ),

        // Headline styles - section headers
        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.25).sp
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        ),
        headlineSmall = defaultTypography.headlineSmall.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        ),

        // Title styles - card headers, list items
        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        ),
        titleMedium = defaultTypography.titleMedium.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp
        ),
        titleSmall = defaultTypography.titleSmall.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp
        ),

        // Body styles - paragraphs, descriptions
        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.25.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = defaultTypography.bodyMedium.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp
        ),

        // Label styles - buttons, captions, tags
        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        ),
        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
    )

// -----------------------------------------------------------------------------
// CUSTOM TYPOGRAPHY EXTENSIONS
// -----------------------------------------------------------------------------

/** Brand logo style - wide tracking, light weight */
val Typography.logoStyle: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = Kalnia,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        letterSpacing = 4.sp
    )

/** Hero headline - dramatic display text */
val Typography.heroDisplay: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = Kalnia,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = (-1).sp,
        lineHeight = 52.sp
    )

/** Stats value - large number display */
val Typography.statsValue: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    )

/** Profit/Loss display - financial values */
val Typography.moneyDisplay: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    )

/** Button text style */
val Typography.buttonText: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 1.sp
    )

/** Card title */
val Typography.cardTitle: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 22.sp
    )

/** Input field text */
val Typography.inputText: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    )

/** Tab label */
val Typography.tabLabel: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    )

/** Caption/metadata */
val Typography.caption: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    )

// Legacy support
val Typography.title200: TextStyle
    @Composable
    get() {
        val isSmallScreen = getScreenWidthDp() <= 360
        val fontSize = if (!isSmallScreen) 24.sp else 20.sp
        val lineHeight = if (!isSmallScreen) 28.sp else 24.sp

        return TextStyle(
            fontFamily = SourceSans,
            fontWeight = FontWeight.Light,
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = 3.sp
        )
    }
