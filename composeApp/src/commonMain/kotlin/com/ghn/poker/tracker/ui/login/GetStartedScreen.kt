package com.ghn.poker.tracker.ui.login

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.shared.SecondaryButton
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.title200
import gizmopoker.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GetStartedScreen(
    onSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box {
            Image(
                painter = painterResource(Res.drawable.gizmo_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )

            Spacer(
                Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0x00181A20), Color(0xFF181A20)),
                        ),
                    ),
            )

            Column(
                Modifier
                    .padding(bottom = 48.dp)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.welcome_to),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )

                Spacer(Modifier.height(Dimens.grid_0_5))

                AnimatedAppTitle(fontSize = 40.sp, lineHeight = 40.sp)

                Spacer(Modifier.height(Dimens.grid_1))

                Text(
                    text = stringResource(Res.string.select_option_to_start),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFBBBBBB),
                        textAlign = TextAlign.Center
                    ),
                )

                Spacer(Modifier.height(Dimens.grid_2))

                SecondaryButton(
                    buttonText = stringResource(Res.string.create_account),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateAccountClick,
                )

                Spacer(Modifier.height(Dimens.grid_3))

                PrimaryButton(
                    buttonText = stringResource(Res.string.sign_in),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSignInClick,
                )
            }
        }
    }
}

@Composable
fun AnimatedAppTitle(
    initialColor: Color = Color(0xFFAFA21D),
    targetColor: Color = Color(0xFFE21221),
    fontSize: TextUnit = 28.sp,
    lineHeight: TextUnit = 32.sp,
    durationInMillis: Int = 2000,
    text: StringResource = Res.string.app_name,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = initialColor,
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(tween(durationInMillis), RepeatMode.Reverse),
        label = "color"
    )

    Text(
        text = stringResource(text),
        style = MaterialTheme.typography.title200.copy(
            color = animatedColor,
            fontSize = fontSize,
            lineHeight = lineHeight,
            fontWeight = FontWeight.Medium
        ),
    )
}
