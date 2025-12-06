package com.ghn.poker.feature.feed.navigation

import kotlinx.serialization.Serializable

@Serializable
data object FeedHome

@Serializable
data class WebView(val url: String)
