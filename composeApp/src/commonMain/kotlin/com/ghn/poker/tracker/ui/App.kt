package com.ghn.poker.tracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.ghn.poker.tracker.domain.usecase.impl.AppState
import com.ghn.poker.tracker.domain.usecase.impl.Store
import com.ghn.poker.tracker.ui.feed.FeedScreen
import com.ghn.poker.tracker.ui.login.GetStartedScreen
import com.ghn.poker.tracker.ui.login.LoginScreen
import com.ghn.poker.tracker.ui.login.SplashScreen
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionEntryScreen
import com.ghn.poker.tracker.ui.tracker.TrackerLandingPage
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.serialization.Serializable
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
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
    data object SessionInsert : AppRoutes

    @Serializable
    data object SplashScreen : AppRoutes

    @Serializable
    data class WebView(val url: String) : AppRoutes

    @Serializable
    sealed class BottomNavItem(
        val route: String,
        val title: String,
        val icon: String,
    ) : AppRoutes {
        @Serializable
        data object Home : BottomNavItem("home", "Home", "ic_home.xml")

        @Serializable
        data object News : BottomNavItem("news", "News", "ic_baseline_assignment.xml")

        @Serializable
        data object Profile : BottomNavItem("profile", "Account", "ic_person.xml")
    }
}

@OptIn(ExperimentalTypeSafeApi::class)
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
                    swipeProperties = remember { SwipeProperties() },
                    modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
                ) {
                    scene<AppRoutes.SplashScreen>(navTransition = NavTransition()) {
                        SplashScreen(onSplashScreenFinished = viewModel::checkForToken)
                    }

                    scene<AppRoutes.Welcome>(navTransition = NavTransition()) {
                        GetStartedScreen { navigator.navigate(AppRoutes.Login) }
                    }

                    scene<AppRoutes.Login>(navTransition = NavTransition()) {
                        LoginScreen(onBackClick = navigator::goBack)
                    }

                    scene<AppRoutes.BottomNavItem.Home>(navTransition = NavTransition()) {
                        bottomBarState.value = true
                        TrackerLandingPage(
                            onCreateSessionClick = {
                                bottomBarState.value = false
                                navigator.navigate(AppRoutes.SessionInsert)
                            },
                            onSignOutClick = { viewModel.signout() }
                        )
                    }

                    scene<AppRoutes.BottomNavItem.News>(navTransition = NavTransition()) {
                        bottomBarState.value = true
                        FeedScreen {
                            navigator.navigate(AppRoutes.WebView(it))
                        }
                    }

                    scene<AppRoutes.WebView>(navTransition = NavTransition()) {
                        bottomBarState.value = false
                        Column(Modifier.fillMaxSize()) {
                            val state = rememberWebViewState(url)

                            Text(text = "${state.pageTitle}")
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

                    scene<AppRoutes.SessionInsert>(navTransition = NavTransition()) {
                        SessionEntryScreen(onBackClick = navigator::goBack)
                    }

                    scene<AppRoutes.BottomNavItem.Profile>(navTransition = NavTransition()) {
                        Column { }
                    }
                }
            }
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
                        icon = {
                            Icon(painterResource(item.icon), contentDescription = null)
                        },
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
