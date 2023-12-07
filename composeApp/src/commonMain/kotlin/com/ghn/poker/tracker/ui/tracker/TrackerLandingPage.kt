package com.ghn.poker.tracker.ui.tracker

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.presentation.session.SessionListViewModel
import com.ghn.poker.tracker.ui.theme.Dimens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrackerLandingPage() {
    SessionList()
//    SessionEntryScreen()
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SessionList(viewModel: SessionListViewModel = SessionListViewModel()) {
    val state = viewModel.state.collectAsState().value
    val color = remember { Animatable(Color.Gray) }
    LaunchedEffect(Unit) {
        while (true) {
            color.animateTo(Color.Green, animationSpec = tween(1000))
            color.animateTo(Color.Gray, animationSpec = tween(500))
        }
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Spacer(Modifier.height(20.dp))
        }

        when (val sessions = state.sessions) {
            LoadableDataState.Empty ->
                item {
                    CircularProgressIndicator()
                }
            LoadableDataState.Error -> TODO()
            is LoadableDataState.Loaded -> {
                items(sessions.data) { session ->

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.grid_2_5)
                                .border(
                                    width = 0.3.dp,
                                    color = MaterialTheme.colors.primary,
                                    shape = RoundedCornerShape(size = 6.dp)
                                )
                                .shadow(
                                    elevation = 16.dp,
                                    spotColor = Color(0x24384F6F),
                                    ambientColor = Color(0x24384F6F)
                                )
                                .padding(vertical = Dimens.grid_2_5, horizontal = 12.dp),
                        //                            .background(MaterialTheme.colors.onBackground),
//                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Image(painterResource("magic-city-casino.xml"), null)
                            Text("$3545", color = Color.Green)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(session.formattedDate)
                            Text("start")
                            Text(session.startAmount.orEmpty())
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
}
