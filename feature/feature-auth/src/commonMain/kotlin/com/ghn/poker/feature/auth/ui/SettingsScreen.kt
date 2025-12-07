package com.ghn.poker.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.components.GizmoPrimaryButton
import com.ghn.poker.core.ui.preview.SurfacePreview
import com.ghn.poker.core.ui.theme.AntiqueBrass
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.core.ui.theme.GizmoGradients
import com.ghn.poker.core.ui.theme.GizmoShapes
import com.ghn.poker.core.ui.theme.Graphite
import com.ghn.poker.core.ui.theme.Obsidian
import com.ghn.poker.core.ui.theme.Onyx
import com.ghn.poker.core.ui.theme.Pewter
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Silver
import com.ghn.poker.core.ui.theme.Slate
import com.ghn.poker.core.ui.theme.logoStyle
import gizmopoker.feature.feature_auth.generated.resources.Res
import gizmopoker.feature.feature_auth.generated.resources.settings_account
import gizmopoker.feature.feature_auth.generated.resources.settings_app_info
import gizmopoker.feature.feature_auth.generated.resources.settings_appearance
import gizmopoker.feature.feature_auth.generated.resources.settings_build
import gizmopoker.feature.feature_auth.generated.resources.settings_dark_mode
import gizmopoker.feature.feature_auth.generated.resources.settings_dark_mode_desc
import gizmopoker.feature.feature_auth.generated.resources.settings_sign_out
import gizmopoker.feature.feature_auth.generated.resources.settings_sign_out_desc
import gizmopoker.feature.feature_auth.generated.resources.settings_title
import gizmopoker.feature.feature_auth.generated.resources.settings_version
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val APP_VERSION = "1.0.0"
private const val BUILD_NUMBER = "1"

@Composable
fun SettingsScreen(
    onSignOutClick: () -> Unit
) {
    SettingsScreenContent(onSignOutClick = onSignOutClick)
}

@Composable
private fun SettingsScreenContent(
    onSignOutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GizmoGradients.backgroundSurface)
    ) {
        AmbientBackgroundElements()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = ChampagneGold,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "GIZMO",
                                style = MaterialTheme.typography.logoStyle,
                                color = Platinum
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .padding(horizontal = Dimens.grid_2)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimens.grid_2_5)
            ) {
                Text(
                    text = stringResource(Res.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Platinum,
                    modifier = Modifier.padding(vertical = Dimens.grid_1)
                )

                AppearanceSection()

                AppInfoSection()

                AccountSection(onSignOutClick = onSignOutClick)

                Spacer(Modifier.height(Dimens.grid_4))
            }
        }
    }
}

@Composable
private fun AmbientBackgroundElements() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-80).dp, y = 60.dp)
                .alpha(0.08f)
                .blur(70.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(ChampagneGold, Color.Transparent)
                        ),
                        radius = size.width / 2
                    )
                }
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 40.dp)
                .alpha(0.06f)
                .blur(50.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Slate, Color.Transparent)
                        ),
                        radius = size.width / 2
                    )
                }
        )
    }
}

@Composable
private fun AppearanceSection() {
    var darkModeEnabled by remember { mutableStateOf(true) }

    SettingsSection(
        title = stringResource(Res.string.settings_appearance),
        icon = Icons.Default.DarkMode
    ) {
        SettingsRow(
            title = stringResource(Res.string.settings_dark_mode),
            subtitle = stringResource(Res.string.settings_dark_mode_desc),
            trailing = {
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Obsidian,
                        checkedTrackColor = ChampagneGold,
                        checkedBorderColor = AntiqueBrass,
                        uncheckedThumbColor = Silver,
                        uncheckedTrackColor = Graphite,
                        uncheckedBorderColor = Onyx
                    )
                )
            }
        )
    }
}

@Composable
private fun AppInfoSection() {
    SettingsSection(
        title = stringResource(Res.string.settings_app_info),
        icon = Icons.Default.Info
    ) {
        Column {
            SettingsRow(
                title = stringResource(Res.string.settings_version),
                trailing = {
                    Text(
                        text = APP_VERSION,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ChampagneGold
                    )
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Dimens.grid_2),
                color = Onyx.copy(alpha = 0.5f)
            )
            SettingsRow(
                title = stringResource(Res.string.settings_build),
                trailing = {
                    Text(
                        text = BUILD_NUMBER,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver
                    )
                }
            )
        }
    }
}

@Composable
private fun AccountSection(onSignOutClick: () -> Unit) {
    SettingsSection(
        title = stringResource(Res.string.settings_account),
        icon = Icons.AutoMirrored.Filled.ExitToApp,
        iconTint = Ruby.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.grid_2),
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
        ) {
            Text(
                text = stringResource(Res.string.settings_sign_out_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = Silver
            )
            GizmoPrimaryButton(
                text = stringResource(Res.string.settings_sign_out),
                onClick = onSignOutClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    iconTint: Color = ChampagneGold,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                iconTint.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = MaterialTheme.typography.labelLarge.letterSpacing * 1.5
                ),
                color = Silver
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = GizmoShapes.sessionCard,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Graphite.copy(alpha = 0.7f),
                                Slate.copy(alpha = 0.4f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Onyx.copy(alpha = 0.6f),
                                Onyx.copy(alpha = 0.2f)
                            )
                        ),
                        shape = GizmoShapes.sessionCard
                    )
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String? = null,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.grid_2, vertical = Dimens.grid_1_5),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Platinum
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Pewter
                )
            }
        }
        trailing()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() = SurfacePreview {
    SettingsScreenContent(onSignOutClick = {})
}
