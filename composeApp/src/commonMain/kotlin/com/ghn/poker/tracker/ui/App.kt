package com.ghn.poker.tracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ghn.poker.tracker.domain.usecase.impl.AppState
import com.ghn.poker.tracker.domain.usecase.impl.Store
import com.ghn.poker.tracker.ui.cards.CardsScreen
import com.ghn.poker.tracker.ui.feed.FeedScreen
import com.ghn.poker.tracker.ui.login.GetStartedScreen
import com.ghn.poker.tracker.ui.login.LoginScreen
import com.ghn.poker.tracker.ui.login.SignUpScreen
import com.ghn.poker.tracker.ui.login.SplashScreen
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionEntryScreen
import com.ghn.poker.tracker.ui.tracker.TrackerLandingPage
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import gizmopoker.generated.resources.Res
import kotlinx.serialization.Serializable
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tech.annexflow.precompose.navigation.typesafe.ExperimentalTypeSafeApi
import tech.annexflow.precompose.navigation.typesafe.Route
import tech.annexflow.precompose.navigation.typesafe.TypesafeNavHost
import tech.annexflow.precompose.navigation.typesafe.navigate
import tech.annexflow.precompose.navigation.typesafe.scene

sealed interface AppRoutes : Route {
    @Serializable
    data object Welcome : AppRoutes

    @Serializable
    data object Login : AppRoutes

    @Serializable
    data object CreateAccount : AppRoutes

    @Serializable
    data object SessionInsert : AppRoutes

    @Serializable
    data object SplashScreen : AppRoutes

    @Serializable
    data class WebView(val url: String) : AppRoutes

    @Serializable
    sealed class BottomNavItem(
        val route: String,
        val title: String
    ) : AppRoutes {
        @Serializable
        data object Home : BottomNavItem("home", "Home")

        @Serializable
        data object News : BottomNavItem("news", "News")

        @Serializable
        data object Profile : BottomNavItem("profile", "Account")

        fun icon(): DrawableResource {
            return when (this) {
                Home -> Res.drawable.ic_home
                News -> Res.drawable.ic_baseline_assignment
                Profile -> Res.drawable.ic_person
            }
        }
    }
}

@OptIn(ExperimentalTypeSafeApi::class, ExperimentalResourceApi::class)
@Composable
fun App(viewModel: Store<AppState> = koinInject()) {
    GizmoTheme {
        PreComposeApp {
            val navigator = rememberNavigator()
            val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(navigator, bottomBarState)
                },
            ) { padding ->
                LaunchedEffect(Unit) {
                    viewModel.userState.collect { state ->
                        when (state) {
                            AppState.Init -> Unit
                            AppState.LoggedIn -> navigator.navigate(
                                route = AppRoutes.BottomNavItem.Home,
                                options = NavOptions(popUpTo = PopUpTo("", inclusive = true))
                            )

                            AppState.LoggedOut -> navigator.navigate(
                                route = AppRoutes.Welcome,
                                options = NavOptions(popUpTo = PopUpTo("", inclusive = true))
                            )
                        }
                    }
                }

                TypesafeNavHost(
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = AppRoutes.SplashScreen,
//                    swipeProperties = remember { SwipeProperties() },
                    modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
                ) {
                    scene<AppRoutes.SplashScreen>(navTransition = NavTransition()) {
                        SplashScreen(onSplashScreenFinished = viewModel::checkForToken)
                    }

                    scene<AppRoutes.Welcome>(navTransition = NavTransition()) {
                        GetStartedScreen(
                            onSignInClick = { navigator.navigate(AppRoutes.Login) },
                            onCreateAccountClick = { navigator.navigate(AppRoutes.CreateAccount) }
                        )
                    }

                    scene<AppRoutes.Login>(navTransition = NavTransition()) {
                        LoginScreen(onBackClick = navigator::goBack)
                    }

                    scene<AppRoutes.CreateAccount>(navTransition = NavTransition()) {
                        SignUpScreen(onBackClick = navigator::goBack)
                    }

                    scene<AppRoutes.BottomNavItem.Home>(
                        navTransition = NavTransition(
                            createTransition = fadeIn(),
                            destroyTransition = fadeOut(),
                            pauseTransition = fadeOut(),
                            resumeTransition = fadeIn()
                        )
                    ) {
                        bottomBarState.value = true
                        TrackerLandingPage(
                            onCreateSessionClick = {
                                bottomBarState.value = false
                                navigator.navigate(AppRoutes.SessionInsert)
                            },
                            onSignOutClick = { viewModel.signout() }
                        )
                    }

                    scene<AppRoutes.BottomNavItem.News>(
                        navTransition = NavTransition(
                            createTransition = fadeIn(),
                            destroyTransition = fadeOut(),
                            pauseTransition = fadeOut(),
                            resumeTransition = fadeIn()
                        )
                    ) {
                        bottomBarState.value = true
                        FeedScreen {
                            navigator.navigate(AppRoutes.WebView(it))
                        }
                    }

                    scene<AppRoutes.WebView>(navTransition = NavTransition()) {
                        bottomBarState.value = false
                        WebViewCompose(url, onBackClick = navigator::goBack)
                    }

                    scene<AppRoutes.SessionInsert>(navTransition = NavTransition()) {
                        SessionEntryScreen(onBackClick = navigator::goBack)
                    }

                    scene<AppRoutes.BottomNavItem.Profile>(navTransition = NavTransition()) {
                        CardsScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
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
                        Icon(Icons.Rounded.ArrowBack, null)
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
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            WebView(state, modifier = Modifier.fillMaxWidth().weight(1f))
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomNavigationBar(navController: Navigator, bottomBarState: MutableState<Boolean>) {
    val items = remember {
        listOf(
            AppRoutes.BottomNavItem.Home,
            AppRoutes.BottomNavItem.News,
            AppRoutes.BottomNavItem.Profile
        )
    }
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar {
//                backgroundColor = MaterialTheme.colors.whiteColor,
//                modifier = Modifier.height(56.dp + Dimens.grid_1) // Default nav bar height + padding
//            ) {
                val navBackStackEntry = navController.currentEntry.collectAsState(null)
                val currentRoute = navBackStackEntry.value?.route

                items.forEach { item ->
                    val selected = remember(currentRoute, item) {
                        currentRoute?.route == item.route
                    }
                    NavigationBarItem(
                        icon = { Icon(painterResource(item.icon()), contentDescription = null) },
                        label = null,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        alwaysShowLabel = true,
                        selected = selected,
                        onClick = {
                            navController.navigate(
                                route = item,
                                options = NavOptions(
//                                    popUpTo = PopUpTo("", inclusive = true)
                                    launchSingleTop = true,
                                )
                            )
                        },
                    )
                }
            }
        }
    )
}
