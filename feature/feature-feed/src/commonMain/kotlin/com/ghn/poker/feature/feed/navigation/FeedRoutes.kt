package com.ghn.poker.feature.feed.navigation

import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.core.common.navigation.TabDestination
import kotlinx.serialization.Serializable

@Serializable
data object FeedHome : TabDestination {
    override val tabIndex: Int = 1
}

@Serializable
data class WebView(val url: String) : GizmoNavKey
