package com.ghn.poker.tracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import gizmopoker.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@ReadOnlyComposable
@Composable
internal expect fun getScreenWidthDp(): Int

@OptIn(ExperimentalResourceApi::class)
private val Kalnia: FontFamily
    @Stable
    @Composable
    get() =
        FontFamily(
            Font(Res.font.kalnia_medium, FontWeight.Medium, FontStyle.Normal),
            Font(Res.font.kalnia_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
            Font(Res.font.kalnia_bold, FontWeight.Bold, FontStyle.Normal),
            Font(Res.font.kalnia_thin, FontWeight.Thin, FontStyle.Normal),
            Font(Res.font.kalnia_light, FontWeight.Light, FontStyle.Normal),
        )

@OptIn(ExperimentalResourceApi::class)
private val SourceSans: FontFamily
    @Stable
//    @ReadOnlyComposable
    @Composable
    get() =
        FontFamily(
            Font(Res.font.source_sans_three_regular, FontWeight.Normal, FontStyle.Normal),
            Font(Res.font.source_sans_three_medium, FontWeight.Medium, FontStyle.Normal),
            Font(Res.font.source_sans_three_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
            Font(Res.font.source_sans_three_bold, FontWeight.Bold, FontStyle.Normal),
            Font(Res.font.source_sans_three_light, FontWeight.Light, FontStyle.Normal),
        )

private val defaultTypography = Typography()

val Typography: Typography
    @Composable
    get() = Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = SourceSans),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = SourceSans),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = SourceSans),

        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = SourceSans),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = SourceSans),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = SourceSans),

        titleLarge = defaultTypography.titleLarge.copy(fontFamily = SourceSans),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = SourceSans),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = SourceSans),

        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = SourceSans),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = SourceSans),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = SourceSans),

        labelLarge = defaultTypography.labelLarge.copy(fontFamily = SourceSans),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = SourceSans),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = SourceSans)
    )

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
