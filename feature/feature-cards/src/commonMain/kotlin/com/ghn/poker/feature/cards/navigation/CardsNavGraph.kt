package com.ghn.poker.feature.cards.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.feature.cards.ui.CardsScreen

fun EntryProviderScope<GizmoNavKey>.cardsEntryBuilder() {
    entry<CardsHome> {
        CardsScreen()
    }
}
