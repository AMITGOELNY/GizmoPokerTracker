package com.ghn.poker.tracker.ui.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.ui.theme.Dimens

@Composable
fun PrimaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    showLoading: Boolean = false,
    fillMaxWidth: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    TextButton(
        modifier = modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .height(Dimens.grid_6_5),
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.grid_2_5),
        enabled = isEnabled,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = if (showLoading) .5f else 1f),
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
//            contentColor = MaterialTheme.colors.buttonTextColor,
//            disabledContentColor = MaterialTheme.colors.buttonTextColor.copy(alpha = .5f)
        ),
        contentPadding = contentPadding,
    ) {
        Box(contentAlignment = Alignment.Center) {
            AnimatedContent(
                targetState = showLoading,
                transitionSpec = { fadeIn(tween(1000)) togetherWith fadeOut(tween(1000)) },
            ) { target ->
                when (target) {
                    false -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            content?.invoke(this)
                            Text(
                                text = buttonText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    letterSpacing = 2.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }

                    true -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Dimens.grid_2_5),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun SecondaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    showLoading: Boolean = false,
    isEnabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.inversePrimary,
    borderColor: Color = MaterialTheme.colorScheme.inversePrimary,
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth().height(Dimens.grid_6_5),
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.grid_3),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(Modifier.alpha(if (showLoading) 0f else 1f)) {
                content?.let { content ->
                    content()
                }
                Text(
                    text = buttonText,
                    modifier = Modifier.offset(y = 2.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background
                    ),
                    color = textColor.copy(alpha = if (isEnabled) 1f else .5f)
                )
            }
//            if (showLoading) {
//                CircularProgressIndicator(
//                    color = MaterialTheme.colorScheme.buttonTextColor,
//                    modifier = Modifier.size(Dimens.grid_2_5),
//                    strokeWidth = 2.dp
//                )
//            }
        }
    }
}
