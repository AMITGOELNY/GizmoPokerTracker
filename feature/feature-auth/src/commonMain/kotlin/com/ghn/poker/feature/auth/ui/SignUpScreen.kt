package com.ghn.poker.feature.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.core.ui.theme.title200
import com.ghn.poker.feature.auth.presentation.CreateAccountActions
import com.ghn.poker.feature.auth.presentation.CreateAccountViewModel
import gizmopoker.core.core_resources.generated.resources.app_name
import gizmopoker.feature.feature_auth.generated.resources.Res
import gizmopoker.feature.feature_auth.generated.resources.create_account
import gizmopoker.feature.feature_auth.generated.resources.join_gizmopoker
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import gizmopoker.core.core_resources.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: CreateAccountViewModel = koinInject(),
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0E1A))) {
        // Dramatic animated background
        DramaticPokerBackground(isVisible = isVisible)

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Glassmorphic back button
                    Surface(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.1f),
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.15f),
                                            Color.White.copy(alpha = 0.05f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Golden logo
                    Text(
                        text = stringResource(CoreRes.string.app_name),
                        style = MaterialTheme.typography.title200.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFD700),
                                    Color(0xFFFFE55C),
                                    Color(0xFFD4AF37)
                                )
                            )
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(32.dp))

                // Hero title with dramatic animation
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(1000)) + slideInVertically(
                        initialOffsetY = { -60 },
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.create_account),
                            style = MaterialTheme.typography.title200.copy(
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFFD700),
                                        Color(0xFFFFE55C),
                                        Color(0xFFD4AF37)
                                    )
                                )
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = stringResource(Res.string.join_gizmopoker),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp
                            ),
                            color = Color(0xFFB0B0B0)
                        )
                    }
                }

                Spacer(Modifier.height(48.dp))

                // Dramatic form with animation
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(1200, delayMillis = 300)) + slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(1200, easing = FastOutSlowInEasing)
                    )
                ) {
                    FormBody(
                        loading = state.value.authenticating,
                        onUsernameChange = { viewModel.dispatch(CreateAccountActions.OnUsernameChange(it)) },
                        onPasswordChange = { viewModel.dispatch(CreateAccountActions.OnPasswordChange(it)) },
                        onSubmit = { viewModel.dispatch(CreateAccountActions.OnSubmit) }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
