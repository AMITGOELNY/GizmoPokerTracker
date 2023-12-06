package com.ghn.poker.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ghn.poker.tracker.ui.App
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.GizmoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButton_Preview() {
    GizmoTheme {
        Surface(Modifier.padding(vertical = Dimens.grid_2)) {
            PrimaryButton("Log in", Modifier.padding(horizontal = Dimens.grid_2), onClick = {})
        }
    }
}

