package com.ghn.poker.feature.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.components.PrimaryButton
import com.ghn.poker.core.ui.theme.AntiqueBrass
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.GizmoGradients
import com.ghn.poker.core.ui.theme.GizmoShapes
import com.ghn.poker.core.ui.theme.Graphite
import com.ghn.poker.core.ui.theme.Onyx
import com.ghn.poker.core.ui.theme.PaleGold
import com.ghn.poker.core.ui.theme.Pewter
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Silver
import com.ghn.poker.core.ui.theme.Slate
import com.ghn.poker.core.ui.theme.heroDisplay
import com.ghn.poker.core.ui.theme.inputText
import com.ghn.poker.core.ui.theme.logoStyle
import com.ghn.poker.feature.auth.presentation.LoginActions
import com.ghn.poker.feature.auth.presentation.LoginViewModel
import gizmopoker.core.core_resources.generated.resources.app_name
import gizmopoker.feature.feature_auth.generated.resources.Res
import gizmopoker.feature.feature_auth.generated.resources.hide_password
import gizmopoker.feature.feature_auth.generated.resources.ic_lock
import gizmopoker.feature.feature_auth.generated.resources.ic_message
import gizmopoker.feature.feature_auth.generated.resources.password
import gizmopoker.feature.feature_auth.generated.resources.show_password
import gizmopoker.feature.feature_auth.generated.resources.sign_in
import gizmopoker.feature.feature_auth.generated.resources.sign_in_to_continue
import gizmopoker.feature.feature_auth.generated.resources.username
import gizmopoker.feature.feature_auth.generated.resources.welcome_back
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import gizmopoker.core.core_resources.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinInject(), onBackClick: () -> Unit) {
    val state = viewModel.state.collectAsState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize().background(GizmoGradients.backgroundSurface)) {
        // Ambient background orbs
        LoginAmbientBackground(isVisible = isVisible)

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Refined back button
                    Surface(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                        shape = CircleShape,
                        color = Slate.copy(alpha = 0.5f),
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .border(1.dp, Onyx, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                                tint = ChampagneGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Logo
                    Text(
                        text = stringResource(CoreRes.string.app_name),
                        style = MaterialTheme.typography.logoStyle.copy(
                            brush = Brush.linearGradient(
                                colors = listOf(ChampagneGold, PaleGold, AntiqueBrass)
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
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(40.dp))

                // Hero title with animation
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(800)) + slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.welcome_back),
                            style = MaterialTheme.typography.heroDisplay.copy(
                                brush = Brush.linearGradient(
                                    colors = listOf(ChampagneGold, PaleGold, ChampagneGold)
                                )
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = stringResource(Res.string.sign_in_to_continue),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Silver
                        )
                    }
                }

                Spacer(Modifier.height(48.dp))

                // Form with animation
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(1000, delayMillis = 200)) + slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
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

    // Card container with refined styling
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        shadowElevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Graphite.copy(alpha = 0.8f), Slate.copy(alpha = 0.5f))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(1.dp, Onyx.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
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
                                tint = ChampagneGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                )

                Spacer(Modifier.height(32.dp))

                // Submit button
                val buttonScale by animateFloatAsState(
                    targetValue = if (loading) 0.98f else 1f,
                    animationSpec = tween(200),
                    label = "button scale"
                )

                PrimaryButton(
                    buttonText = stringResource(Res.string.sign_in),
                    modifier = Modifier.fillMaxWidth().scale(buttonScale),
                    onClick = onSubmit,
                    isEnabled = !loading,
                    showLoading = loading
                )
            }
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

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Ruby
            isFocused -> ChampagneGold
            else -> Onyx
        },
        animationSpec = tween(200),
        label = "border color"
    )

    val elevation by animateDpAsState(
        targetValue = if (isFocused) 4.dp else 0.dp,
        animationSpec = tween(200),
        label = "elevation"
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            isError -> Ruby
            isFocused -> ChampagneGold
            else -> Pewter
        },
        animationSpec = tween(200),
        label = "icon color"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = tween(200),
        label = "icon scale"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = GizmoShapes.inputField,
        shadowElevation = elevation,
        color = Color.Transparent
    ) {
        OutlinedTextField(
            value = textFieldValue,
            placeholder = {
                Text(
                    text = placeHolder,
                    style = MaterialTheme.typography.inputText,
                    color = Pewter.copy(alpha = 0.7f),
                )
            },
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.inputText.copy(color = Platinum),
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            leadingIcon = {
                Icon(
                    painter = painterResource(leadingIconId),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp).scale(iconScale)
                )
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth(),
            shape = GizmoShapes.inputField,
            singleLine = true,
            isError = isError,
            interactionSource = interactionSource,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                focusedContainerColor = Slate.copy(alpha = 0.6f),
                unfocusedContainerColor = Slate.copy(alpha = 0.3f),
                errorContainerColor = Slate.copy(alpha = 0.3f),
                focusedTextColor = Platinum,
                unfocusedTextColor = Platinum,
                errorBorderColor = Ruby,
                errorLeadingIconColor = Ruby,
                cursorColor = ChampagneGold
            ),
        )
    }
}

@Composable
internal fun LoginAmbientBackground(isVisible: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(1200),
        label = "background alpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left gold glow
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-120).dp, y = (-100).dp)
                .size(400.dp)
                .alpha(alpha * 0.12f)
                .blur(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(ChampagneGold, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        // Bottom-right gold glow
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 150.dp)
                .size(380.dp)
                .alpha(alpha * 0.10f)
                .blur(90.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(ChampagneGold, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        // Center accent
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 80.dp)
                .size(250.dp)
                .alpha(alpha * 0.06f)
                .blur(60.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(PaleGold, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
    }
}

// Legacy support
@Composable
internal fun DramaticPokerBackground(isVisible: Boolean) {
    LoginAmbientBackground(isVisible = isVisible)
}

@Composable
internal fun AnimatedBackgroundOrbs(isVisible: Boolean) {
    LoginAmbientBackground(isVisible = isVisible)
}
