package com.ghn.poker.tracker.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.presentation.login.LoginActions
import com.ghn.poker.tracker.presentation.login.LoginViewModel
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.title200
import gizmopoker.composeapp.generated.resources.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(viewModel: LoginViewModel = koinInject(), onBackClick: () -> Unit) {
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
                        text = stringResource(Res.string.app_name),
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
                            text = stringResource(Res.string.welcome_back),
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
                            text = stringResource(Res.string.sign_in_to_continue),
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
                        onUsernameChange = { viewModel.dispatch(LoginActions.OnUsernameChange(it)) },
                        onPasswordChange = { viewModel.dispatch(LoginActions.OnPasswordChange(it)) },
                        onSubmit = { viewModel.dispatch(LoginActions.OnSubmit) }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
internal fun FormBody(
    loading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    var usernameValue by remember { mutableStateOf(TextFieldValue("")) }
    var passwordValue by remember { mutableStateOf(TextFieldValue("")) }

    var passwordVisible by remember { mutableStateOf(false) }
    // Animated card container
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1A1D24).copy(alpha = 0.6f),
        shadowElevation = 8.dp,
        tonalElevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            // Username field
            EnhancedTextField(
                textFieldValue = usernameValue,
                onValueChange = {
                    usernameValue = it
                    onUsernameChange(it.text)
                },
                leadingIconId = Res.drawable.ic_message,
                placeHolder = stringResource(Res.string.username),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                )
            )

            Spacer(Modifier.height(20.dp))

            // Password field
            EnhancedTextField(
                textFieldValue = passwordValue,
                onValueChange = {
                    passwordValue = it
                    onPasswordChange(it.text)
                },
                leadingIconId = Res.drawable.ic_lock,
                placeHolder = stringResource(Res.string.password),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go,
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = if (passwordVisible) stringResource(Res.string.hide_password) else stringResource(Res.string.show_password),
                            tint = Color(0xFFAFA21D),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )

            Spacer(Modifier.height(32.dp))

            // Enhanced submit button
            val buttonScale by animateFloatAsState(
                targetValue = if (loading) 0.95f else 1f,
                animationSpec = tween(300)
            )

            PrimaryButton(
                buttonText = stringResource(Res.string.sign_in),
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(buttonScale),
                onClick = onSubmit,
                isEnabled = !loading,
                showLoading = loading
            )
        }
    }
}

@Composable
internal fun EnhancedTextField(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    leadingIconId: DrawableResource,
    placeHolder: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Animated border color
    val borderColor by animateColorAsState(
        targetValue = when {
            isFocused -> Color(0xFFAFA21D)
            isError -> Color(0xFFE21221)
            else -> Color(0xFF35383F)
        },
        animationSpec = tween(300)
    )

    // Animated elevation
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 4.dp else 0.dp,
        animationSpec = tween(300)
    )

    // Animated icon color
    val iconColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFFAFA21D) else Color(0xFF9E9E9E),
        animationSpec = tween(300)
    )

    // Animated icon scale
    val iconScale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = tween(300)
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = elevation,
        color = Color.Transparent
    ) {
        OutlinedTextField(
            value = textFieldValue,
            placeholder = {
                Text(
                    text = placeHolder,
                    style = MaterialTheme.typography.title200.copy(
                        letterSpacing = 0.2.sp,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                    ),
                    color = Color(0xFF9E9E9E).copy(alpha = 0.7f),
                )
            },
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.title200.copy(
                letterSpacing = 0.2.sp,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            leadingIcon = {
                Icon(
                    painter = painterResource(leadingIconId),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .size(22.dp)
                        .scale(iconScale)
                )
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            isError = isError,
            interactionSource = interactionSource,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                focusedContainerColor = Color(0xFF1A1D24).copy(alpha = 0.8f),
                unfocusedContainerColor = Color(0xFF1A1D24).copy(alpha = 0.4f),
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorBorderColor = Color(0xFFE21221),
                errorLeadingIconColor = Color(0xFFE21221),
                cursorColor = Color(0xFFAFA21D)
            ),
        )
    }
}

@Composable
internal fun DramaticPokerBackground(isVisible: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(1500)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Large golden gradient orb - top left
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-150).dp, y = (-150).dp)
                .size(450.dp)
                .alpha(alpha * 0.4f)
                .blur(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.5f),
                            Color(0xFFFFE55C).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Emerald green orb - poker felt inspired
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-100).dp, y = 100.dp)
                .size(400.dp)
                .alpha(alpha * 0.3f)
                .blur(110.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00A86B).copy(alpha = 0.4f),
                            Color(0xFF2E8B57).copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Large bottom-right golden orb
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 150.dp, y = 200.dp)
                .size(500.dp)
                .alpha(alpha * 0.35f)
                .blur(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.5f),
                            Color(0xFFD4AF37).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Purple accent orb - top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .size(350.dp)
                .alpha(alpha * 0.25f)
                .blur(90.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6B4FBB).copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Center accent glow
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 100.dp)
                .size(300.dp)
                .alpha(alpha * 0.2f)
                .blur(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Subtle overlay gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha * 0.3f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0E1A).copy(alpha = 0.7f),
                            Color.Transparent,
                            Color(0xFF0A0E1A).copy(alpha = 0.9f)
                        ),
                        startY = 0f,
                        endY = 2000f
                    )
                )
        )
    }
}

@Composable
internal fun AnimatedBackgroundOrbs(isVisible: Boolean) {
    DramaticPokerBackground(isVisible = isVisible)
}
