package com.ghn.poker.feature.cards.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ghn.poker.feature.cards.ui.CardsScreen

fun NavGraphBuilder.cardsNavGraph() {
    composable<CardsHome> {
        CardsScreen()
    }
}
