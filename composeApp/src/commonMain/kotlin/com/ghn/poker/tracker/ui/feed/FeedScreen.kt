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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.core.ui.components.GizmoLoadingIndicator
import com.ghn.poker.core.ui.components.GizmoPrimaryButton
import com.ghn.poker.tracker.domain.model.FeedItem
import com.ghn.poker.tracker.presentation.feed.FeedActions
import com.ghn.poker.tracker.presentation.feed.FeedViewModel
import com.ghn.poker.tracker.presentation.feed.FeedsContainer
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import com.ghn.poker.tracker.ui.preview.SurfacePreview
import com.ghn.poker.tracker.ui.theme.ChampagneGold
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.GizmoGradients
import com.ghn.poker.tracker.ui.theme.GizmoShapes
import com.ghn.poker.tracker.ui.theme.Graphite
import com.ghn.poker.tracker.ui.theme.Obsidian
import com.ghn.poker.tracker.ui.theme.Onyx
import com.ghn.poker.tracker.ui.theme.PaleGold
import com.ghn.poker.tracker.ui.theme.Platinum
import com.ghn.poker.tracker.ui.theme.Ruby
import com.ghn.poker.tracker.ui.theme.Silver
import com.ghn.poker.tracker.ui.theme.Slate
import com.ghn.poker.tracker.ui.theme.cardTitle
import com.ghn.poker.tracker.ui.theme.logoStyle
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.articles
import gizmopoker.composeapp.generated.resources.featured
import gizmopoker.composeapp.generated.resources.feed_empty_message
import gizmopoker.composeapp.generated.resources.feed_empty_title
import gizmopoker.composeapp.generated.resources.feed_error_message
import gizmopoker.composeapp.generated.resources.feed_error_title
import gizmopoker.composeapp.generated.resources.feed_refresh
import gizmopoker.composeapp.generated.resources.feed_section_empty_message
import gizmopoker.composeapp.generated.resources.feed_section_empty_title
import gizmopoker.composeapp.generated.resources.ic_placeholder
import gizmopoker.composeapp.generated.resources.news_feed
import gizmopoker.composeapp.generated.resources.strategy
import gizmopoker.composeapp.generated.resources.trending_news
import gizmopoker.composeapp.generated.resources.try_again
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel(),
    onFeedItemClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GizmoGradients.backgroundSurface)
    ) {
        // Ambient background
        AmbientBackground()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Newspaper,
                                contentDescription = null,
                                tint = ChampagneGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = stringResource(Res.string.news_feed),
                                style = MaterialTheme.typography.logoStyle,
                                color = Platinum
                            )
                        }
                    }
                )
            }
        ) { padding ->
            AnimatedContent(
                targetState = state.value.feed,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "Feed Content"
            ) { targetState ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.grid_2_5)
                ) {
                    when (targetState) {
                        LoadableDataState.Empty ->
                            EmptyState(onRetry = { viewModel.dispatch(FeedActions.Init) })

                        LoadableDataState.Error ->
                            ErrorState(onRetry = { viewModel.dispatch(FeedActions.Init) })

                        is LoadableDataState.Loaded ->
                            NewsItemList(
                                feed = targetState.data,
                                tabIndex = state.value.tabIndex,
                                selectedFeed = state.value.selectedFeed,
                                isRefreshing = state.value.isRefreshing,
                                onFeedItemClick = onFeedItemClick,
                                onTabItemClick = { viewModel.dispatch(FeedActions.OnTabItemClick(it)) },
                                onRefresh = { viewModel.dispatch(FeedActions.Refresh) }
                            )

                        LoadableDataState.Loading ->
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                GizmoLoadingIndicator()
                            }
                    }
                }
            }
        }
    }
}

@Composable
private fun AmbientBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-80).dp, y = 100.dp)
                .alpha(0.07f)
                .blur(60.dp)
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
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-30).dp)
                .alpha(0.05f)
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
private fun EmptyState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = GizmoShapes.featuredCard,
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
                    .border(1.dp, Onyx, GizmoShapes.featuredCard)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        ChampagneGold.copy(alpha = 0.12f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.dp, ChampagneGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = ChampagneGold.copy(alpha = 0.7f),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = stringResource(Res.string.feed_empty_title),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Platinum
                    )

                    Text(
                        text = stringResource(Res.string.feed_empty_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    GizmoPrimaryButton(
                        text = stringResource(Res.string.feed_refresh),
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = GizmoShapes.featuredCard,
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
                            colors = listOf(Ruby.copy(alpha = 0.06f), Color.Transparent)
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(Ruby.copy(alpha = 0.25f), Onyx.copy(alpha = 0.3f))
                        ),
                        shape = GizmoShapes.featuredCard
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Ruby.copy(alpha = 0.12f), Color.Transparent)
                                )
                            )
                            .border(1.dp, Ruby.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Ruby.copy(alpha = 0.7f),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = stringResource(Res.string.feed_error_title),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Platinum
                    )

                    Text(
                        text = stringResource(Res.string.feed_error_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    GizmoPrimaryButton(
                        text = stringResource(Res.string.try_again),
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionEmptyState(sectionName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = GizmoShapes.featuredCard,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Graphite.copy(alpha = 0.7f), Slate.copy(alpha = 0.4f))
                    )
                )
                .border(1.dp, Onyx, GizmoShapes.featuredCard)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(ChampagneGold.copy(alpha = 0.1f), Color.Transparent)
                            )
                        )
                        .border(1.dp, ChampagneGold.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = ChampagneGold.copy(alpha = 0.6f),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = stringResource(Res.string.feed_section_empty_title, sectionName),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Platinum
                )

                Text(
                    text = stringResource(Res.string.feed_section_empty_message, sectionName),
                    style = MaterialTheme.typography.bodySmall,
                    color = Silver,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsItemList(
    feed: FeedsContainer,
    tabIndex: Int,
    selectedFeed: List<FeedItem>,
    isRefreshing: Boolean,
    onFeedItemClick: (String) -> Unit,
    onTabItemClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    val tabs = listOf(Res.string.articles, Res.string.strategy)

    val context = LocalPlatformContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.padding(top = 16.dp).align(Alignment.TopCenter),
                containerColor = ChampagneGold,
                color = Obsidian
            )
        }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2),
            modifier = Modifier.padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = Dimens.grid_2)
        ) {
            // Section header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        ChampagneGold.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.dp, ChampagneGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = stringResource(Res.string.trending_news),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Platinum,
                        maxLines = 1
                    )
                }
            }

            // Featured carousel
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(feed.featured) { item ->
                        FeaturedCard(
                            item = item,
                            imageLoader = imageLoader,
                            onClick = { onFeedItemClick(item.link) }
                        )
                    }
                }
            }

            // Tab row (sticky)
            stickyHeader {
                TabRow(
                    selectedTabIndex = tabIndex,
                    containerColor = Obsidian.copy(alpha = 0.95f),
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.grid_1),
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
                            onClick = { onTabItemClick(index) },
                        )
                    }
                }
            }

            // Feed items
            if (selectedFeed.isEmpty()) {
                item {
                    SectionEmptyState(
                        sectionName = stringResource(if (tabIndex == 0) Res.string.articles else Res.string.strategy)
                    )
                }
            } else {
                items(selectedFeed, key = { it.link }) { item ->
                    FeedItemCard(
                        item = item,
                        imageLoader = imageLoader,
                        onClick = { onFeedItemClick(item.link) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedCard(
    item: FeedItem,
    imageLoader: ImageLoader,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(240.dp)
            .aspectRatio(1.4f)
            .clickable(onClick = onClick),
        shape = GizmoShapes.featuredCard,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.image,
                imageLoader = imageLoader,
                placeholder = painterResource(Res.drawable.ic_placeholder),
                error = painterResource(Res.drawable.ic_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay
            Box(
                Modifier.fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.4f to Obsidian.copy(alpha = 0.2f),
                            1f to Obsidian.copy(alpha = 0.9f),
                        )
                    )
            )

            // Content
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(Dimens.grid_2),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Badge
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(ChampagneGold)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.featured),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Obsidian
                        )
                    )
                }

                // Title and metadata
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.cardTitle,
                        color = Platinum,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.site,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ChampagneGold
                        )
                        Text(
                            text = item.dateFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            color = Silver
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedItemCard(
    item: FeedItem,
    imageLoader: ImageLoader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = GizmoShapes.sessionCard,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Graphite.copy(alpha = 0.7f), Slate.copy(alpha = 0.5f))
                    )
                )
                .border(1.dp, Onyx.copy(alpha = 0.5f), GizmoShapes.sessionCard)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1_5)
            ) {
                // Image
                Card(
                    modifier = Modifier.size(110.dp),
                    shape = GizmoShapes.sessionCard,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    AsyncImage(
                        model = item.image,
                        imageLoader = imageLoader,
                        placeholder = painterResource(Res.drawable.ic_placeholder),
                        error = painterResource(Res.drawable.ic_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Content
                Column(
                    modifier = Modifier
                        .height(110.dp)
                        .weight(1f)
                        .padding(end = Dimens.grid_2)
                        .padding(vertical = Dimens.grid_1_5),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.cardTitle,
                        color = Platinum,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .background(ChampagneGold.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                item.site,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = ChampagneGold
                            )
                        }

                        Text(
                            item.dateFormatted,
                            style = MaterialTheme.typography.bodySmall,
                            color = Silver
                        )
                    }
                }
            }
        }
    }
}

private class FeedsContainerProvider : PreviewParameterProvider<FeedsContainer> {
    override val values = sequenceOf(
        FeedsContainer(
            featured = listOf(
                FeedItem(
                    title = "Breaking: Poker Tournament Results",
                    link = "https://example.com/1",
                    image = "https://via.placeholder.com/300x200?text=Featured1",
                    site = "PokerNews",
                    pubDate = LocalDate(2023, 10, 26),
                    category = NewsCategory.NEWS
                )
            ),
            items = emptyList(),
            articles = listOf(
                FeedItem(
                    title = "In-depth Analysis of Recent Games",
                    link = "https://example.com/4",
                    image = "https://via.placeholder.com/300x200?text=Article1",
                    site = "AnalysisHub",
                    pubDate = LocalDate(2023, 10, 23),
                    category = NewsCategory.NEWS
                )
            ),
            strategy = listOf(
                FeedItem(
                    title = "Advanced Bluffing Techniques",
                    link = "https://example.com/6",
                    image = "https://via.placeholder.com/300x200?text=Strategy1",
                    site = "ProTips",
                    pubDate = LocalDate(2023, 10, 21),
                    category = NewsCategory.STRATEGY
                )
            )
        )
    )
}

@Preview
@Composable
private fun NewsItemListPreview(@PreviewParameter(FeedsContainerProvider::class) feed: FeedsContainer) =
    SurfacePreview {
        NewsItemList(
            feed = feed,
            tabIndex = 0,
            selectedFeed = feed.articles,
            isRefreshing = false,
            onFeedItemClick = {},
            onTabItemClick = {},
            onRefresh = {}
        )
    }
