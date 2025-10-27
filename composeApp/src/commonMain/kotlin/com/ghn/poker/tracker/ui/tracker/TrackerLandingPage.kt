package com.ghn.poker.tracker.ui.tracker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.presentation.session.SessionListAction
import com.ghn.poker.tracker.presentation.session.SessionListViewModel
import com.ghn.poker.tracker.ui.preview.SurfacePreview
import com.ghn.poker.tracker.ui.shared.LoadingAnimation
import com.ghn.poker.tracker.ui.theme.Dimens
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.app_name
import gizmopoker.composeapp.generated.resources.charts
import gizmopoker.composeapp.generated.resources.charts_coming_soon
import gizmopoker.composeapp.generated.resources.connection_issue
import gizmopoker.composeapp.generated.resources.create_session
import gizmopoker.composeapp.generated.resources.no_sessions_yet
import gizmopoker.composeapp.generated.resources.recent_sessions
import gizmopoker.composeapp.generated.resources.sessions
import gizmopoker.composeapp.generated.resources.start_poker_journey
import gizmopoker.composeapp.generated.resources.total_pl
import gizmopoker.composeapp.generated.resources.try_again
import gizmopoker.composeapp.generated.resources.unable_load_sessions
import gizmopoker.composeapp.generated.resources.win_rate
import gizmopoker.composeapp.generated.resources.working_on_charts
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import kotlin.time.Clock

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
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E1A),
                        Color(0xFF131629),
                        Color(0xFF1A1F35)
                    )
                )
            )
    ) {
        // Animated background elements
        AnimatedBackgroundElements()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
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
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(Res.string.app_name),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF6B7BFF),
                                            Color(0xFF4F5FFF)
                                        )
                                    )
                                )
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onSignOutClick,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF1F2438).copy(alpha = 0.5f))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ExitToApp,
                                contentDescription = "Sign out",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
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
                // Stats Summary Card (only show when data is loaded)
                AnimatedVisibility(
                    visible = state.sessions is LoadableDataState.Loaded,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    if (state.sessions is LoadableDataState.Loaded) {
                        StatsSummaryCard(state.sessions.data)
                    }
                }

                // Modern Tab Row
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
                                .padding(horizontal = 24.dp)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            Color(0xFF7B8BFF)
                                        )
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
                                    color = if (tabIndex == index) {
                                        MaterialTheme.colorScheme.onBackground
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    }
                                )
                            },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
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

                    // Premium FAB
                    ExtendedFloatingActionButton(
                        onClick = onCreateSessionClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = Dimens.grid_2_5, end = Dimens.grid_2_5),
                        text = {
                            Text(
                                text = stringResource(Res.string.create_session),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp,
                                )
                            )
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedBackgroundElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val rotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation1"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Subtle circular glow elements
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = 100.dp)
                .alpha(0.15f)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF5265FF),
                                Color.Transparent
                            )
                        ),
                        radius = size.width / 2
                    )
                }
        )

        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .alpha(0.1f)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF7B4FFF),
                                Color.Transparent
                            )
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2438).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5265FF).copy(alpha = 0.1f),
                            Color(0xFF7B4FFF).copy(alpha = 0.05f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5265FF).copy(alpha = 0.3f),
                            Color(0xFF7B4FFF).copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(Dimens.grid_2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.AttachMoney,
                    label = stringResource(Res.string.total_pl),
                    value = if (totalProfit >= 0) "+$${totalProfit.toInt()}" else "-$${(-totalProfit).toInt()}",
                    valueColor = if (totalProfit >= 0) Color(0xFF4CAF50) else Color(0xFFEF5350),
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(60.dp)
                        .background(Color.White.copy(alpha = 0.1f))
                )

                StatItem(
                    icon = Icons.AutoMirrored.Filled.EventNote,
                    label = stringResource(Res.string.sessions),
                    value = totalSessions.toString(),
                    valueColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(60.dp)
                        .background(Color.White.copy(alpha = 0.1f))
                )

                StatItem(
                    icon = if (winRate >= 50) Icons.AutoMirrored.Default.TrendingUp else Icons.AutoMirrored.Default.TrendingDown,
                    label = stringResource(Res.string.win_rate),
                    value = "$winRate%",
                    valueColor = if (winRate >= 50) Color(0xFF4CAF50) else Color(0xFFEF5350),
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
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
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
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ShowChart,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = stringResource(Res.string.charts_coming_soon),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(Res.string.working_on_charts),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
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
        label = "Animated Content"
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
                        // Extra padding for FAB
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
                ) {
                    items(targetState.data) { session ->
                        SessionListItem(session)
                    }
                }
            }

            LoadableDataState.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingAnimation()
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
            // Animated icon container
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }

            Spacer(Modifier.height(Dimens.grid_1))

            Text(
                text = stringResource(Res.string.no_sessions_yet),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(Res.string.start_poker_journey),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(Modifier.height(Dimens.grid_1))

            // Decorative cards
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.alpha(0.4f)
            ) {
                listOf("♠", "♥", "♦", "♣").forEach { suit ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1F2438))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = suit,
                            fontSize = 20.sp,
                            color = if (suit == "♥" || suit == "♦") {
                                Color(0xFFEF5350)
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
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
            // Error icon with pulsing animation
            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(1000),
                label = "error_scale"
            )

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFEF5350).copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFFEF5350).copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = Color(0xFFEF5350)
                )
            }

            Spacer(Modifier.height(Dimens.grid_1))

            Text(
                text = stringResource(Res.string.connection_issue),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(Res.string.unable_load_sessions),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(Dimens.grid_2))

            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(25.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = Dimens.grid_1)
                        .size(20.dp)
                )
                Text(
                    stringResource(Res.string.try_again),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

// Previews
@Preview
@Composable
private fun EmptySessionsStatePreview() = SurfacePreview {
    EmptySessionsState()
}

@Preview
@Composable
private fun ErrorSessionsStatePreview() = SurfacePreview {
    ErrorSessionsState(onRetry = {})
}

@Preview
@Composable
private fun SessionListLoadingPreview() = SurfacePreview {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LoadingAnimation()
    }
}

@Preview
@Composable
private fun SessionListLoadedPreview() = SurfacePreview {
    val sampleSessions = listOf(
        SessionData(
            date = Clock.System.now(),
            startAmount = "500",
            endAmount = "1200",
            venue = Venue.HARD_ROCK_FL
        ),
        SessionData(
            date = Clock.System.now(),
            startAmount = "1000",
            endAmount = "800",
            venue = Venue.MAGIC_CITY
        ),
        SessionData(
            date = Clock.System.now(),
            startAmount = "300",
            endAmount = "650",
            venue = Venue.HIALEAH_PARK
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Dimens.grid_2,
            end = Dimens.grid_2,
            top = Dimens.grid_2,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
    ) {
        items(sampleSessions) { session ->
            SessionListItem(session)
        }
    }
}
