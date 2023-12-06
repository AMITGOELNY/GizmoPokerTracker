package com.ghn.poker.tracker.ui

import Greeting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun AnimationDemo() {
    var greetingText by remember { mutableStateOf("Hello World!") }
    var showImage by remember { mutableStateOf(false) }
    Button(
        onClick = {
            greetingText = "Compose: ${Greeting().greet()}"
            showImage = !showImage
        }
    ) {
        Text(greetingText)
    }
    AnimatedVisibility(showImage) {
        Image(painterResource("compose-multiplatform.xml"), null)
    }
}
