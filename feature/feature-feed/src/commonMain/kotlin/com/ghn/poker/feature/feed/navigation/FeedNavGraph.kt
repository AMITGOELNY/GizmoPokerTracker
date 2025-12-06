package com.ghn.poker.feature.feed.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ghn.poker.feature.feed.ui.FeedScreen
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import gizmopoker.feature.feature_feed.generated.resources.Res
import gizmopoker.feature.feature_feed.generated.resources.news_feed
import org.jetbrains.compose.resources.stringResource

fun NavGraphBuilder.feedNavGraph(
    onNavigateToWebView: (String) -> Unit,
    onBackClick: () -> Unit,
    onShowBottomBar: (Boolean) -> Unit
) {
    composable<FeedHome>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        onShowBottomBar(true)
        FeedScreen(onFeedItemClick = onNavigateToWebView)
    }

    composable<WebView> { backStackEntry ->
        val webView: WebView = backStackEntry.toRoute()
        WebViewCompose(
            url = webView.url,
            onBackClick = onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WebViewCompose(url: String, onBackClick: () -> Unit) {
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
                        text = stringResource(Res.string.news_feed),
                        style = MaterialTheme.typography.titleLarge.copy(color = Color(0xffea940b))
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            val state = rememberWebViewState(url)
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = { loadingState.progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            WebView(state, modifier = Modifier.fillMaxWidth().weight(1f))
        }
    }
}
