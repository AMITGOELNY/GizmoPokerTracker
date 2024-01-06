package com.ghn.poker.tracker.ui.login

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.ui.theme.title200
import gizmopoker.generated.resources.Res
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(onSplashScreenFinished: () -> Unit) {
//    val systemUiController: SystemUiController = rememberSystemUiController()
//    systemUiController.setStatusBarColor(Color.Black)
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (!startAnimation) .75f else 1.4f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 20
        ),
        label = "scale"
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(450)
        onSplashScreenFinished()
    }
    val color = remember { Animatable(Color.Black) }
    val primary = MaterialTheme.colorScheme.background
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFAFA21D),
        targetValue = Color(0xFFE21221),
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "color"
    )
    val infiniteTransition2 = rememberInfiniteTransition(label = "infinite transition")

    LaunchedEffect(Unit) {
//        while (true) {
        color.animateTo(primary, animationSpec = tween(5000))
//            color.animateTo(Color.Gray, animationSpec = tween(500))
//        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(color.value),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.title200.copy(
                color = animatedColor,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }
        )
//        Image(
//            painter = painterResource(background),
//            contentDescription = null,
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier
//                .scale(scaleAnimation)
//                .fillMaxSize()
//        )

//        Image(
//            painter = painterResource(R.drawable.ic_logo),
//            contentDescription = "",
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = Dimens.grid_6)
//        )
    }
}
