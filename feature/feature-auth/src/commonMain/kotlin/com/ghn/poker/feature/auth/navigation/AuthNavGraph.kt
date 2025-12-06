package com.ghn.poker.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ghn.poker.feature.auth.ui.GetStartedScreen
import com.ghn.poker.feature.auth.ui.LoginScreen
import com.ghn.poker.feature.auth.ui.SignUpScreen
import com.ghn.poker.feature.auth.ui.SplashScreen

fun NavGraphBuilder.authNavGraph(
    onSplashScreenFinished: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCreateAccount: () -> Unit,
    onBackClick: () -> Unit
) {
    composable<SplashScreen> {
        SplashScreen(onSplashScreenFinished = onSplashScreenFinished)
    }

    composable<Welcome> {
        GetStartedScreen(
            onSignInClick = onNavigateToLogin,
            onCreateAccountClick = onNavigateToCreateAccount
        )
    }

    composable<Login> {
        LoginScreen(onBackClick = onBackClick)
    }

    composable<CreateAccount> {
        SignUpScreen(onBackClick = onBackClick)
    }
}
