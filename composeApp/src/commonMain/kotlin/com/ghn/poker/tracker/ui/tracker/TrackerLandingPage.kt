package com.ghn.poker.tracker.ui.tracker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.ghn.poker.tracker.ui.theme.title200
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.app_name
import gizmopoker.composeapp.generated.resources.charts
import gizmopoker.composeapp.generated.resources.create_session
import gizmopoker.composeapp.generated.resources.recent_sessions
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerLandingPage(
    viewModel: SessionListViewModel = koinInject<SessionListViewModel>(),
    onCreateSessionClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(Res.string.recent_sessions, Res.string.charts)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.titleLarge.copy(color = Color(0xffea940b))
                    )
                },
                actions = {
                    IconButton(onClick = onSignOutClick) {
                        Icon(Icons.AutoMirrored.Default.ExitToApp, contentDescription = "Sign out")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabIndex])
                            .height(Dimens.grid_0_5)
                            .padding(horizontal = 32.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                },
                divider = {},
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = stringResource(title),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
                when (tabIndex) {
                    0 -> SessionList(
                        viewModel = viewModel,
                        onRetry = { viewModel.onDispatch(SessionListAction.Retry) }
                    )

                    1 -> Column { }
                }

                ExtendedFloatingActionButton(
                    onClick = onCreateSessionClick,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .padding(bottom = Dimens.grid_2_5, end = Dimens.grid_2_5),
                    text = {
                        Text(
                            text = stringResource(Res.string.create_session),
                            style = MaterialTheme.typography.title200.copy(
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.sp,
                            )
                        )
                    },
                    icon = { Icon(Icons.Filled.Add, null) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            }
        }
    }
//    SessionEntryScreen()
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
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
        ) {
            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                modifier = Modifier.height(120.dp).fillMaxWidth(),
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
            Text(
                text = "No Sessions Yet",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = "Start tracking your poker sessions\nby creating your first one",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
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
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.height(120.dp).fillMaxWidth(),
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
            )
            Text(
                text = "Unable to Load Sessions",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            Text(
                text = "Something went wrong while loading\nyour poker sessions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.grid_1))
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(horizontal = Dimens.grid_2)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.padding(end = Dimens.grid_1)
                )
                Text("Try Again")
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
            date = kotlin.time.Clock.System.now(),
            startAmount = "500",
            endAmount = "1200",
            venue = Venue.HARD_ROCK_FL
        ),
        SessionData(
            date = kotlin.time.Clock.System.now(),
            startAmount = "1000",
            endAmount = "800",
            venue = Venue.MAGIC_CITY
        ),
        SessionData(
            date = kotlin.time.Clock.System.now(),
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
