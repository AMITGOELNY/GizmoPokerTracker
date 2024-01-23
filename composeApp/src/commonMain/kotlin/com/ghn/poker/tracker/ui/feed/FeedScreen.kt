package com.ghn.poker.tracker.ui.feed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.domain.model.FeedItem
import com.ghn.poker.tracker.presentation.feed.FeedActions
import com.ghn.poker.tracker.presentation.feed.FeedViewModel
import com.ghn.poker.tracker.presentation.feed.FeedsContainer
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.ui.shared.LoadingAnimation
import com.ghn.poker.tracker.ui.theme.Dimens
import com.seiko.imageloader.ui.AutoSizeImage
import gizmopoker.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinInject(),
    onFeedItemClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                Modifier.fillMaxWidth().padding(vertical = Dimens.grid_1_5),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.news_feed),
                    style = MaterialTheme.typography.titleLarge.copy(color = Color(0xffea940b))
                )
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = state.value.feed,
            transitionSpec = { fadeIn(tween(3000)) togetherWith fadeOut(tween(3000)) },
            label = "Animated Content"
        ) { targetState ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(Dimens.grid_2_5)
            ) {
                when (targetState) {
                    LoadableDataState.Empty -> TODO()
                    LoadableDataState.Error -> TODO()
                    is LoadableDataState.Loaded ->
                        NewsItemList(
                            feed = targetState.data,
                            tabIndex = state.value.tabIndex,
                            selectedFeed = state.value.selectedFeed,
                            onFeedItemClick = onFeedItemClick
                        ) {
                            viewModel.dispatch(FeedActions.OnTabItemClick(it))
                        }

                    LoadableDataState.Loading ->
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            LoadingAnimation()
                        }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsItemList(
    feed: FeedsContainer,
    tabIndex: Int,
    selectedFeed: List<FeedItem>,
    onFeedItemClick: (String) -> Unit,
    onTabItemClick: (Int) -> Unit
) {
    val tabs = listOf(Res.string.articles, Res.string.strategy)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_2),
        modifier = Modifier.padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = Dimens.grid_2)
    ) {
        item {
            Text(
                text = stringResource(Res.string.trending_news),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 20.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(feed.featured) {
                    Box(
                        modifier = Modifier
                            .height(240.dp)
                            .aspectRatio(1.33f)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { onFeedItemClick(it.link) }

                    ) {
                        AutoSizeImage(
                            url = it.image,
                            contentDescription = null,
                            modifier = Modifier
                                .matchParentSize()
                                .shadow(elevation = Dimens.grid_1, MaterialTheme.shapes.medium)
                                .background(Color.White, MaterialTheme.shapes.medium)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop,
                            placeholderPainter = { painterResource(Res.drawable.ic_placeholder) },
                            errorPainter = { painterResource(Res.drawable.ic_placeholder) }
                        )

                        Column(
                            Modifier.matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        0f to Color(0xFF000000).copy(alpha = 0f),
//                                        .4f to Color(0xFF000000).copy(alpha = .5f),
                                        1f to Color(0xFF000000).copy(alpha = .8f),
                                    ),
                                    MaterialTheme.shapes.medium
                                )
                                .clip(MaterialTheme.shapes.medium)
                        ) {}

                        Column(
                            Modifier.fillMaxHeight(.5f)
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = Dimens.grid_2)
                                .padding(bottom = Dimens.grid_2),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 20.8.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 3,
                                minLines = 3
                            )
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = it.site,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                    ),
                                )

                                Text(
                                    text = it.dateFormatted,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }

        stickyHeader {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.grid_1),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabIndex])
                            .height(Dimens.grid_0_5)
                            .background(color = Color(0xFFFF6861))
                    )
                },
                divider = {
                    Box(
                        modifier = Modifier
                            .height(Dimens.grid_0_25)
                            .background(color = Color(0xFF2A2B39))
                    )
                },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = stringResource(title),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            )
                        },
                        selected = tabIndex == index,
                        onClick = { onTabItemClick(index) }
                    )
                }
            }
        }

        items(selectedFeed, key = { it.link }) {
            Row(
                Modifier.fillMaxWidth()
                    .border(1.dp, Color(0xFF2A2B39), RoundedCornerShape(size = 8.dp))
                    .background(Color(0xFF23252F), RoundedCornerShape(size = 8.dp))
                    .clickable { onFeedItemClick(it.link) }
                    .animateItemPlacement(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1_5)
            ) {
                AutoSizeImage(
                    url = it.image,
                    contentDescription = null,
                    modifier = Modifier
                        .width(140.dp)
                        .height(140.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = Dimens.grid_1,
                                bottomStart = Dimens.grid_1
                            )
                        ),
                    contentScale = ContentScale.Crop,
                    placeholderPainter = { painterResource(Res.drawable.ic_placeholder) },
                    errorPainter = { painterResource(Res.drawable.ic_placeholder) }
                )
                Column(
                    modifier = Modifier.height(140.dp)
                        .padding(end = Dimens.grid_1_5)
                        .padding(vertical = Dimens.grid_1_5),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        it.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            it.site,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 11.sp,
                                lineHeight = 16.5.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFFFF6861)
                        )

                        Text(
                            it.dateFormatted,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 11.sp,
                                lineHeight = 16.5.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f)
                        )
                    }
                }
            }
        }
    }
}
