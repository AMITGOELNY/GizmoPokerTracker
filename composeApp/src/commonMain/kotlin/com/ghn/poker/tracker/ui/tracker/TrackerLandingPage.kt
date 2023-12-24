package com.ghn.poker.tracker.ui.tracker

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.domain.usecase.netAmountColor
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.presentation.session.SessionListViewModel
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.title200
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun TrackerLandingPage(onCreateSessionClick: () -> Unit) {
    SessionList(onCreateSessionClick)
//    SessionEntryScreen()
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SessionList(
    onCreateSessionClick: () -> Unit
) {
    val viewModel: SessionListViewModel = koinInject<SessionListViewModel>()
    val state = viewModel.state.collectAsState().value
    val color = remember { Animatable(Color.Gray) }
    LaunchedEffect(Unit) {
        while (true) {
            color.animateTo(Color.Green, animationSpec = tween(1000))
            color.animateTo(Color.Gray, animationSpec = tween(500))
        }
    }

    Box {
        LazyColumn(Modifier.fillMaxSize()) {
            when (val sessions = state.sessions) {
                LoadableDataState.Empty ->
                    item {
                        CircularProgressIndicator()
                    }

                LoadableDataState.Error -> TODO()
                is LoadableDataState.Loaded -> {
                    items(sessions.data) { session ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.grid_2_5)
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(size = 6.dp)
                                )
                                .border(
                                    width = 0.3.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(size = 6.dp)
                                )
//                                .shadow(
//                                    elevation = 16.dp,
//                                    spotColor = Color(0x24384F6F),
//                                    ambientColor = Color(0x24384F6F)
//                                )
                                .padding(vertical = Dimens.grid_2_5, horizontal = Dimens.grid_1_5),
                            verticalArrangement = Arrangement.spacedBy(
                                space = Dimens.grid_1,
                                alignment = Alignment.CenterVertically
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(res = "magic-city-casino.xml"),
                                    contentDescription = null,
                                    modifier = Modifier.height(18.dp)
                                )
                                Text(session.netProfit, color = session.netAmountColor)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    session.formattedDate,
                                    style = MaterialTheme.typography.title200.copy(
                                        fontSize = 14.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 0.sp,
                                        fontFamily = FontFamily.Default
                                    )
                                )
//                                Text("start")
//                                Text(session.startAmount.orEmpty())
                            }
                        }
                        Spacer(Modifier.height(Dimens.grid_2))
                    }
                }

                LoadableDataState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onCreateSessionClick,
            modifier = Modifier.align(Alignment.BottomEnd).padding(
                bottom = Dimens.grid_2_5,
                end = Dimens.grid_2_5
            ),
            text = {
                Text(
                    "Create Session",
                    style = MaterialTheme.typography.title200.copy(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.sp,
                        fontFamily = FontFamily.Default
                    )
                )
            },
            icon = {
                Icon(Icons.Filled.Add, null)
            },
            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.secondary
        )
    }
}
