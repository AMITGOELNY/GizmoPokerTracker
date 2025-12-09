package com.ghn.poker.feature.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.feature.auth.ui.GetStartedScreen
import com.ghn.poker.feature.auth.ui.LoginScreen
import com.ghn.poker.feature.auth.ui.SettingsScreen
import com.ghn.poker.feature.auth.ui.SignUpScreen
import com.ghn.poker.feature.auth.ui.SplashScreen

fun EntryProviderScope<GizmoNavKey>.authEntryBuilder(
    onSplashScreenFinished: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCreateAccount: () -> Unit,
    onBackClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    entry<SplashScreen> {
        SplashScreen(onSplashScreenFinished = onSplashScreenFinished)
    }

    entry<Welcome> {
        GetStartedScreen(
            onSignInClick = onNavigateToLogin,
            onCreateAccountClick = onNavigateToCreateAccount
        )
    }

    entry<Login> {
        LoginScreen(onBackClick = onBackClick)
    }

    entry<CreateAccount> {
        SignUpScreen(onBackClick = onBackClick)
    }

    entry<SettingsHome> {
        SettingsScreen(onSignOutClick = onSignOutClick)
    }
}
