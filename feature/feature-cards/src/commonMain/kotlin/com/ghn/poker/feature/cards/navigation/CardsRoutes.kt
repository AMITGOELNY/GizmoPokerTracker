package com.ghn.poker.feature.cards.navigation

import com.ghn.poker.core.common.navigation.TabDestination
import kotlinx.serialization.Serializable

@Serializable
data object CardsHome : TabDestination {
    override val tabIndex: Int = 2
}
