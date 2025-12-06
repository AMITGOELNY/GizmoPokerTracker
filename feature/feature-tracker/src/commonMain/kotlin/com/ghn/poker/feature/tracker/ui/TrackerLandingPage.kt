package com.ghn.poker.feature.tracker.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.components.GizmoIconButton
import com.ghn.poker.core.ui.components.GizmoLoadingIndicator
import com.ghn.poker.core.ui.components.GizmoPrimaryButton
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.core.ui.theme.Emerald
import com.ghn.poker.core.ui.theme.GizmoGradients
import com.ghn.poker.core.ui.theme.GizmoShapes
import com.ghn.poker.core.ui.theme.Graphite
import com.ghn.poker.core.ui.theme.Obsidian
import com.ghn.poker.core.ui.theme.Onyx
import com.ghn.poker.core.ui.theme.PaleGold
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Silver
import com.ghn.poker.core.ui.theme.Slate
import com.ghn.poker.core.ui.theme.logoStyle
import com.ghn.poker.core.ui.theme.statsValue
import com.ghn.poker.feature.tracker.domain.model.SessionData
import com.ghn.poker.feature.tracker.presentation.LoadableDataState
import com.ghn.poker.feature.tracker.presentation.SessionListAction
import com.ghn.poker.feature.tracker.presentation.SessionListViewModel
import gizmopoker.core.core_resources.generated.resources.connection_issue
import gizmopoker.core.core_resources.generated.resources.try_again
import gizmopoker.feature.feature_tracker.generated.resources.Res
import gizmopoker.feature.feature_tracker.generated.resources.charts
import gizmopoker.feature.feature_tracker.generated.resources.charts_coming_soon
import gizmopoker.feature.feature_tracker.generated.resources.create_session
import gizmopoker.feature.feature_tracker.generated.resources.no_sessions_yet
import gizmopoker.feature.feature_tracker.generated.resources.recent_sessions
import gizmopoker.feature.feature_tracker.generated.resources.sessions
import gizmopoker.feature.feature_tracker.generated.resources.start_poker_journey
import gizmopoker.feature.feature_tracker.generated.resources.total_pl
import gizmopoker.feature.feature_tracker.generated.resources.unable_load_sessions
import gizmopoker.feature.feature_tracker.generated.resources.win_rate
import gizmopoker.feature.feature_tracker.generated.resources.working_on_charts
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import gizmopoker.core.core_resources.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerLandingPage(
    viewModel: SessionListViewModel = koinInject<SessionListViewModel>(),
    onCreateSessionClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(Res.string.recent_sessions, Res.string.charts)
    val state = viewModel.state.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GizmoGradients.backgroundSurface)
    ) {
        // Ambient background elements
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
                    },
                    actions = {
                        GizmoIconButton(
                            icon = Icons.AutoMirrored.Default.ExitToApp,
                            onClick = onSignOutClick,
                            contentDescription = "Sign out",
                            tint = Silver,
                            backgroundColor = Slate.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.width(Dimens.grid_1))
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
            ) {
                // Stats Summary Card
                AnimatedVisibility(
                    visible = state.sessions is LoadableDataState.Loaded,
                    enter = slideInVertically(tween(400)) + fadeIn(tween(400)),
                    exit = slideOutVertically(tween(300)) + fadeOut(tween(300))
                ) {
                    if (state.sessions is LoadableDataState.Loaded) {
                        StatsSummaryCard(state.sessions.data)
                    }
                }

                // Tab Row
                TabRow(
                    selectedTabIndex = tabIndex,
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.grid_2),
                    indicator = { tabPositions ->
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[tabIndex])
                                .height(3.dp)
                                .padding(horizontal = 28.dp)
                                .clip(GizmoShapes.navIndicator)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(ChampagneGold, PaleGold)
                                    )
                                )
                        )
                    },
                    divider = {},
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = {
                                Text(
                                    text = stringResource(title),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (tabIndex == index) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (tabIndex == index) Platinum else Silver.copy(alpha = 0.6f)
                                )
                            },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
                    when (tabIndex) {
                        0 -> SessionList(
                            viewModel = viewModel,
                            onRetry = { viewModel.onDispatch(SessionListAction.Retry) }
                        )
                        1 -> ComingSoonView()
                    }

                    // FAB
                    ExtendedFloatingActionButton(
                        onClick = onCreateSessionClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = Dimens.grid_2_5, end = Dimens.grid_2_5),
                        text = {
                            Text(
                                text = stringResource(Res.string.create_session),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = Obsidian
                            )
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Obsidian.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Obsidian
                                )
                            }
                        },
                        containerColor = ChampagneGold,
                        contentColor = Obsidian,
                        shape = GizmoShapes.fab,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 12.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AmbientBackgroundElements() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left gold glow
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

        // Bottom-right subtle glow
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
private fun StatsSummaryCard(sessions: List<SessionData>) {
    val totalSessions = sessions.size
    val totalProfit = sessions.sumOf {
        (it.endAmount?.toDoubleOrNull() ?: 0.0) - (it.startAmount?.toDoubleOrNull() ?: 0.0)
    }
    val winRate = if (totalSessions > 0) {
        (
            sessions.count {
                (it.endAmount?.toDoubleOrNull() ?: 0.0) > (it.startAmount?.toDoubleOrNull() ?: 0.0)
            }.toFloat() / totalSessions * 100
            ).toInt()
    } else {
        0
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.grid_2),
        shape = GizmoShapes.sessionCard,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Graphite.copy(alpha = 0.8f), Slate.copy(alpha = 0.5f))
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(ChampagneGold.copy(alpha = 0.06f), Color.Transparent)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(ChampagneGold.copy(alpha = 0.25f), Onyx.copy(alpha = 0.3f))
                    ),
                    shape = GizmoShapes.sessionCard
                )
                .padding(vertical = Dimens.grid_2_5, horizontal = Dimens.grid_2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.AttachMoney,
                    label = stringResource(Res.string.total_pl),
                    value = if (totalProfit >= 0) "+$${totalProfit.toInt()}" else "-$${(-totalProfit).toInt()}",
                    valueColor = if (totalProfit >= 0) Emerald else Ruby,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider()

                StatItem(
                    icon = Icons.AutoMirrored.Filled.EventNote,
                    label = stringResource(Res.string.sessions),
                    value = totalSessions.toString(),
                    valueColor = Platinum,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider()

                StatItem(
                    icon = if (winRate >= 50) Icons.AutoMirrored.Default.TrendingUp else Icons.AutoMirrored.Default.TrendingDown,
                    label = stringResource(Res.string.win_rate),
                    value = "$winRate%",
                    valueColor = if (winRate >= 50) Emerald else Ruby,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ChampagneGold,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.statsValue,
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Silver
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(56.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Onyx, Color.Transparent)
                )
            )
    )
}

@Composable
private fun ComingSoonView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ChampagneGold.copy(alpha = 0.12f), Color.Transparent)
                        )
                    )
                    .border(1.dp, Onyx, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ShowChart,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = ChampagneGold.copy(alpha = 0.7f)
                )
            }
            Text(
                text = stringResource(Res.string.charts_coming_soon),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Platinum
            )
            Text(
                text = stringResource(Res.string.working_on_charts),
                style = MaterialTheme.typography.bodyMedium,
                color = Silver,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SessionList(viewModel: SessionListViewModel, onRetry: () -> Unit) {
    val state = viewModel.state.collectAsState().value

    AnimatedContent(
        targetState = state.sessions,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        label = "Session List"
    ) { targetState ->
        when (targetState) {
            LoadableDataState.Empty -> EmptySessionsState()
            LoadableDataState.Error -> ErrorSessionsState(onRetry = onRetry)
            is LoadableDataState.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Dimens.grid_2,
                        end = Dimens.grid_2,
                        top = Dimens.grid_2,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5)
                ) {
                    items(targetState.data) { session ->
                        SessionListItem(session)
                    }
                }
            }
            LoadableDataState.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    GizmoLoadingIndicator()
                }
        }
    }
}

@Composable
private fun EmptySessionsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2),
            modifier = Modifier.padding(Dimens.grid_3)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ChampagneGold.copy(alpha = 0.1f), Color.Transparent)
                        )
                    )
                    .border(1.5.dp, ChampagneGold.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = ChampagneGold.copy(alpha = 0.6f)
                )
            }

            Spacer(Modifier.height(Dimens.grid_1))

            Text(
                text = stringResource(Res.string.no_sessions_yet),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Platinum
            )
            Text(
                text = stringResource(Res.string.start_poker_journey),
                style = MaterialTheme.typography.bodyLarge,
                color = Silver,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(Dimens.grid_1))

            // Card suit decorations
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.alpha(0.35f)
            ) {
                listOf("\u2660", "\u2665", "\u2666", "\u2663").forEachIndexed { index, suit ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(Graphite)
                            .border(1.dp, Onyx, MaterialTheme.shapes.small),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = suit,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (index == 1 || index == 2) Ruby else Platinum
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorSessionsState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2),
            modifier = Modifier.padding(Dimens.grid_3)
        ) {
            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                label = "error_scale"
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Ruby.copy(alpha = 0.1f), Color.Transparent)
                        )
                    )
                    .border(1.5.dp, Ruby.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = Ruby.copy(alpha = 0.7f)
                )
            }

            Spacer(Modifier.height(Dimens.grid_1))

            Text(
                text = stringResource(CoreRes.string.connection_issue),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Platinum
            )
            Text(
                text = stringResource(Res.string.unable_load_sessions),
                style = MaterialTheme.typography.bodyLarge,
                color = Silver,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.grid_2))

            GizmoPrimaryButton(
                text = stringResource(CoreRes.string.try_again),
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}
