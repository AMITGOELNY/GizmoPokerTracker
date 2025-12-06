package com.ghn.poker.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.GizmoShapes
import com.ghn.poker.core.ui.theme.GizmoTheme
import com.ghn.poker.core.ui.theme.Obsidian
import com.ghn.poker.core.ui.theme.Onyx
import com.ghn.poker.core.ui.theme.Pewter
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Slate
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Premium styled text field
 */
@Composable
fun GizmoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
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

    val iconColor by animateColorAsState(
        targetValue = when {
            isError -> Ruby
            isFocused -> ChampagneGold
            else -> Pewter
        },
        animationSpec = tween(200),
        label = "icon color"
    )

    val elevation by animateDpAsState(
        targetValue = if (isFocused) 4.dp else 0.dp,
        animationSpec = tween(200),
        label = "elevation"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = GizmoShapes.inputField,
        shadowElevation = elevation,
        color = Color.Transparent
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Pewter.copy(alpha = 0.7f)
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Box(Modifier.graphicsLayer { }) {
                        icon()
                    }
                }
            },
            trailingIcon = trailingIcon,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Platinum),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            isError = isError,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            shape = GizmoShapes.inputField,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = Ruby,
                focusedContainerColor = Slate.copy(alpha = 0.6f),
                unfocusedContainerColor = Slate.copy(alpha = 0.3f),
                errorContainerColor = Slate.copy(alpha = 0.3f),
                focusedTextColor = Platinum,
                unfocusedTextColor = Platinum,
                cursorColor = ChampagneGold,
                focusedLeadingIconColor = iconColor,
                unfocusedLeadingIconColor = iconColor,
                focusedTrailingIconColor = iconColor,
                unfocusedTrailingIconColor = iconColor,
            )
        )
    }
}

@Preview
@Composable
private fun GizmoTextFieldPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(modifier = Modifier.padding(16.dp)) {
                var text1 by remember { mutableStateOf("") }
                GizmoTextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    placeholder = "Enter username",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Pewter
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                var text2 by remember { mutableStateOf("user@example.com") }
                GizmoTextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    placeholder = "Email address",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Pewter
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                var password by remember { mutableStateOf("password123") }
                var passwordVisible by remember { mutableStateOf(false) }
                GizmoTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Pewter
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Pewter
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))

                GizmoTextField(
                    value = "Error state",
                    onValueChange = {},
                    placeholder = "With error",
                    isError = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Ruby
                        )
                    }
                )
            }
        }
    }
}
