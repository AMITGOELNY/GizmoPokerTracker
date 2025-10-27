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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ghn.poker.tracker.domain.usecase.impl.AppState
import com.ghn.poker.tracker.domain.usecase.impl.Store
import com.ghn.poker.tracker.ui.cards.CardsScreen
import com.ghn.poker.tracker.ui.feed.FeedScreen
import com.ghn.poker.tracker.ui.login.GetStartedScreen
import com.ghn.poker.tracker.ui.login.LoginScreen
import com.ghn.poker.tracker.ui.login.SignUpScreen
import com.ghn.poker.tracker.ui.login.SplashScreen
import com.ghn.poker.tracker.ui.shared.BottomNavItem
import com.ghn.poker.tracker.ui.shared.CreateAccount
import com.ghn.poker.tracker.ui.shared.Login
import com.ghn.poker.tracker.ui.shared.SessionInsert
import com.ghn.poker.tracker.ui.shared.SplashScreen
import com.ghn.poker.tracker.ui.shared.WebView
import com.ghn.poker.tracker.ui.shared.Welcome
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionEntryScreen
import com.ghn.poker.tracker.ui.tracker.TrackerLandingPage
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.news_feed
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun App(
    viewModel: Store<AppState> = koinInject(),
    navController: NavHostController = rememberNavController()
) {
    GizmoTheme {
        val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
//                modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
            bottomBar = {
                BottomNavigationBar(navController, bottomBarState)
            },
//                containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            AppStateListener(viewModel, navController)
            NavHost(
                navController = navController,
                startDestination = SplashScreen,
                modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
            ) {
                composable<SplashScreen> {
                    SplashScreen(onSplashScreenFinished = viewModel::checkForToken)
                }

                composable<Welcome> {
                    GetStartedScreen(
                        onSignInClick = { navController.navigate(Login) },
                        onCreateAccountClick = { navController.navigate(CreateAccount) }
                    )
                }

                composable<Login> {
                    LoginScreen(onBackClick = navController::popBackStack)
                }

                composable<CreateAccount> {
                    SignUpScreen(onBackClick = navController::popBackStack)
                }

                composable<BottomNavItem.Home>(
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) {
                    bottomBarState.value = true
                    TrackerLandingPage(
                        onCreateSessionClick = {
                            bottomBarState.value = false
                            navController.navigate(SessionInsert)
                        },
                        onSignOutClick = { viewModel.signout() }
                    )
                }

                composable<BottomNavItem.News>(
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) {
                    bottomBarState.value = true
                    FeedScreen {
                        navController.navigate(WebView(it))
                    }
                }

                composable<WebView> { backStackEntry ->
//                        bottomBarState.value = false
                    val webView: WebView = backStackEntry.toRoute()
                    WebViewCompose(
                        url = webView.url,
                        onBackClick = navController::popBackStack
                    )
                }

                composable<SessionInsert> {
                    SessionEntryScreen(onBackClick = navController::popBackStack)
                }

                composable<BottomNavItem.Profile> {
                    CardsScreen()
                }
            }
        }
    }
}

@Composable
private fun AppStateListener(
    viewModel: Store<AppState>,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.userState.collect { state ->
            when (state) {
                AppState.Init -> Unit
                AppState.LoggedIn -> navController.navigate(
                    route = BottomNavItem.Home,
//                                options = NavOptions(popUpTo = PopUpTo("", inclusive = true))
                )

                AppState.LoggedOut -> navController.navigate(
                    route = Welcome,
//                                options = NavOptions(popUpTo = PopUpTo("", inclusive = true))
                )
            }
        }
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

@Composable
fun BottomNavigationBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val items = remember {
        listOf(BottomNavItem.Home, BottomNavItem.News, BottomNavItem.Profile)
    }
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(
//                backgroundColor = MaterialTheme.colors.whiteColor,
//                modifier = Modifier.height(56.dp + Dimens.grid_1) // Default nav bar height + padding
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                    ?: BottomNavItem.Home::class.qualifiedName.orEmpty()

                items.forEach { item ->
                    val selected by remember(currentRoute) {
                        derivedStateOf { currentRoute == item::class.qualifiedName }
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
                            navController.navigate(item) {
                                launchSingleTop = true
                                popUpTo(BottomNavItem.Home) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
            }
        }
    )
}
