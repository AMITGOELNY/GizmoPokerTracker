package com.ghn.poker.tracker.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.GizmoTheme

@Preview(showBackground = true)
@Composable
private fun PrimaryButton_Preview() {
    GizmoTheme {
        Surface(Modifier.padding(vertical = Dimens.grid_2)) {
            PrimaryButton("Log in", Modifier.padding(horizontal = Dimens.grid_2), onClick = {})
        }
    }
}
