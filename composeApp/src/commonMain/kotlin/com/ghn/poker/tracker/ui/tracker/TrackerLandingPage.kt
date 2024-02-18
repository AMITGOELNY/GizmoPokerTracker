package com.ghn.poker.tracker.ui.tracker

import androidx.compose.animation.Animatable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.runtime.LaunchedEffect
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
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.presentation.session.SessionListViewModel
import com.ghn.poker.tracker.ui.shared.LoadingAnimation
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.title200
import gizmopoker.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
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
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Sign out")
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
                    0 -> SessionList(viewModel, padding)
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
fun SessionList(viewModel: SessionListViewModel, padding: PaddingValues) {
    val state = viewModel.state.collectAsState().value
    val color = remember { Animatable(Color.Gray) }
    LaunchedEffect(Unit) {
        while (true) {
            color.animateTo(Color.Green, animationSpec = tween(1000))
            color.animateTo(Color.Gray, animationSpec = tween(500))
        }
    }

    AnimatedContent(
        targetState = state.sessions,
        transitionSpec = { fadeIn(tween(3000)) togetherWith fadeOut(tween(3000)) },
        label = "Animated Content"
    ) { targetState ->
        when (targetState) {
            LoadableDataState.Empty -> Unit
            LoadableDataState.Error -> Unit
            is LoadableDataState.Loaded -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(targetState.data) { session ->
                        SessionListItem(session)
                        Spacer(Modifier.height(Dimens.grid_2))
                    }
                }
            }

            LoadableDataState.Loading ->
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    LoadingAnimation()
                }
        }
    }
}
