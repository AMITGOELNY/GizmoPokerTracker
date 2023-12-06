package com.ghn.poker.tracker.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
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
        shape = RoundedCornerShape(Dimens.grid_3),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = if (showLoading) .5f else 1f),
            disabledBackgroundColor = MaterialTheme.colors.primary.copy(alpha = .5f),
//            contentColor = MaterialTheme.colors.buttonTextColor,
//            disabledContentColor = MaterialTheme.colors.buttonTextColor.copy(alpha = .5f)
        ),
        contentPadding = contentPadding
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.alpha(if (showLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content?.let { content ->
                    content()
                }
                Text(text = buttonText)
            }
            if (showLoading) {
                CircularProgressIndicator(Modifier.size(Dimens.grid_2_5), strokeWidth = 2.dp)
            }
        }
    }
}