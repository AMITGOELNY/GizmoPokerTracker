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

val Typography.title200: TextStyle
    @Composable
    get() {
        val isSmallScreen = getScreenWidthDp() <= 360
        val fontSize = if (!isSmallScreen) 24.sp else 20.sp
        val lineHeight = if (!isSmallScreen) 28.sp else 24.sp

        return TextStyle(
            fontFamily = Kalnia,
            fontWeight = FontWeight.Light,
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = 3.sp
        )
    }
