package com.ghn.poker.tracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Stable
@ReadOnlyComposable
@Composable
internal expect fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font

@ReadOnlyComposable
@Composable
internal expect fun getScreenWidthDp(): Int

private val Kalnia: FontFamily
    @Stable
    @ReadOnlyComposable
    @Composable
    get() =
        FontFamily(
            font(
                "Kalnia Regular",
                "kalnia_regular",
                FontWeight.Normal,
                FontStyle.Normal
            ),
            font(
                "Kalnia Medium",
                "kalnia_medium",
                FontWeight.Medium,
                FontStyle.Normal
            ),
            font(
                "Kalnia SemiBold",
                "kalnia_semi_bold",
                FontWeight.SemiBold,
                FontStyle.Normal
            ),
            font(
                "Kalnia Bold",
                "kalnia_bold",
                FontWeight.Bold,
                FontStyle.Normal
            ),
            font(
                "Kalnia Thin",
                "kalnia_thin",
                FontWeight.Thin,
                FontStyle.Normal
            ),
            font(
                "Kalnia Light",
                "kalnia_light",
                FontWeight.Light,
                FontStyle.Normal
            ),
        )

private val SourceSans: FontFamily
    @Stable
    @ReadOnlyComposable
    @Composable
    get() =
        FontFamily(
            font(
                "Source Sans3 Regular",
                "source_sans_three_regular",
                FontWeight.Normal,
                FontStyle.Normal
            ),
            font(
                "Source Sans3 Medium",
                "source_sans_three_medium",
                FontWeight.Medium,
                FontStyle.Normal
            ),
            font(
                "Source Sans3 SemiBold",
                "source_sans_three_semi_bold",
                FontWeight.SemiBold,
                FontStyle.Normal
            ),
            font(
                "Source Sans3 Bold",
                "source_sans_three_bold",
                FontWeight.Bold,
                FontStyle.Normal
            ),
            font(
                "Source Sans3 Light",
                "source_sans_three_light",
                FontWeight.Light,
                FontStyle.Normal
            ),
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
