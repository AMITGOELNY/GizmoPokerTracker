package com.ghn.poker.tracker.ui.feed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.presentation.feed.FeedViewModel
import com.ghn.poker.tracker.presentation.feed.FeedsContainer
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.ui.shared.LoadingAnimation
import com.ghn.poker.tracker.ui.theme.Dimens
import com.seiko.imageloader.ui.AutoSizeImage
import gizmopoker.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinInject(),
    onFeedItemClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsState()
    AnimatedContent(
        targetState = state.value.feed,
        transitionSpec = { fadeIn(tween(3000)) togetherWith fadeOut(tween(3000)) },
        label = "Animated Content"
    ) { targetState ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2_5)
        ) {
            Text(stringResource(Res.string.news_feed))
            when (targetState) {
                LoadableDataState.Empty -> TODO()
                LoadableDataState.Error -> TODO()
                is LoadableDataState.Loaded -> NewsItemList(targetState.data, onFeedItemClick)
                LoadableDataState.Loading ->
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        LoadingAnimation()
                    }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NewsItemList(feed: FeedsContainer, onFeedItemClick: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(feed.featured) {
                    Column(
                        modifier = Modifier.width(180.dp).clickable { onFeedItemClick(it.link) },
                        verticalArrangement = Arrangement.spacedBy(Dimens.grid_1)
                    ) {
                        AutoSizeImage(
                            url = it.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp)
                                .shadow(elevation = Dimens.grid_1, MaterialTheme.shapes.medium)
                                .background(Color.White, MaterialTheme.shapes.medium)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop,
                            placeholderPainter = { painterResource(Res.drawable.ic_placeholder) },
                            errorPainter = { painterResource(Res.drawable.ic_placeholder) }
                        )

                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 3,
                            minLines = 3
                        )
                    }
                }
            }
        }

        items(feed.items) {
            Row(
                Modifier.border(
                    width = 1.dp,
                    color = Color(0xFF2A2B39),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                    .background(
                        color = Color(0xFF23252F),
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .clickable { onFeedItemClick(it.link) },
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AutoSizeImage(
                    url = it.image,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .shadow(elevation = Dimens.grid_1, MaterialTheme.shapes.medium)
                        .background(Color.White, MaterialTheme.shapes.medium)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop,
                    placeholderPainter = { painterResource(Res.drawable.ic_placeholder) },
                    errorPainter = { painterResource(Res.drawable.ic_placeholder) }
                )
                Column(
                    modifier = Modifier.height(120.dp)
                        .padding(end = 8.dp)
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        it.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(it.pubDate.toString())
                }
            }
        }
    }
}
