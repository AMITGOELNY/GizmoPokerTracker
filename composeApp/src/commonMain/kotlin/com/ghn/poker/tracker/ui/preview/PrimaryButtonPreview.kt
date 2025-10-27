package com.ghn.poker.tracker.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.Dimens
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun PrimaryButton_Preview() = SurfacePreview {
    Column(Modifier.padding(vertical = Dimens.grid_2)) {
        PrimaryButton("Log in", Modifier.padding(horizontal = Dimens.grid_2), onClick = {})
    }
}
