package com.ghn.poker.feature.tracker.navigation

import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.core.common.navigation.TabDestination
import kotlinx.serialization.Serializable

@Serializable
data object TrackerHome : TabDestination {
    override val tabIndex: Int = 0
}

@Serializable
data object SessionInsert : GizmoNavKey
